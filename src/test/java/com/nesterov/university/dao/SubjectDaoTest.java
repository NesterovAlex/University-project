package com.nesterov.university.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Properties;
import org.dbunit.Assertion;
import org.dbunit.DBTestCase;
import org.dbunit.IDatabaseTester;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.PropertiesBasedJdbcDatabaseTester;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import com.nesterov.university.model.Subject;

class SubjectDaoTest extends DBTestCase{

	private static final String PROPERTY = "config.properties";
	private final String CREATE_TABLE = "DROP TABLE IF EXISTS subjects; CREATE TABLE subjects(id SERIAL NOT NULL, name VARCHAR(20) NOT NULL);";

	private ApplicationContext context;
	private JdbcTemplate template;
	private SubjectDao dao;
	private Properties properties;
	protected IDatabaseTester tester;
	
	public SubjectDaoTest() {
		context = new ClassPathXmlApplicationContext("persistence.xml");
		template = (JdbcTemplate) context.getBean("jdbcTemplate");
		template.execute(CREATE_TABLE);
		dao = context.getBean(SubjectDao.class);
		properties = com.nesterov.university.Utils.readPropertiesFile(PROPERTY);
		System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_DRIVER_CLASS, properties.getProperty("driver"));
		System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_CONNECTION_URL, properties.getProperty("url"));
		System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_USERNAME, properties.getProperty("username"));
		System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_PASSWORD, properties.getProperty("password"));
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		tester = new JdbcDatabaseTester(properties.getProperty("driver"), properties.getProperty("url"),
				properties.getProperty("username"), properties.getProperty("password"));
		tester.setDataSet(getDataSet());
		tester.onSetup();
	}

	@Test
	void givenDataSet_whenCreate_thenTableHasNewSubject() throws Exception {
	
		dao.create(new Subject("Mathematic"));
	
		IDatabaseConnection connection = getConnection();
		IDataSet databaseDataSet = connection.createDataSet();
		ITable actualTable = databaseDataSet.getTable("subjects");
		IDataSet expectedDataSet = new FlatXmlDataSetBuilder()
				.build(Thread.currentThread().getContextClassLoader().getResourceAsStream("subjects_expected.xml"));
		ITable expectedTable = expectedDataSet.getTable("SUBJECTS");
		
		Assertion.assertEquals(expectedTable, actualTable);
	}

	@Test
	void givenDataSet_whenDeleteSubject_thenTableHasEqualsDataSet() throws Exception {
		dao.create(new Subject("Mathematic"));
		dao.create(new Subject("Geography"));
		
		dao.delete(2);	
		
		IDatabaseConnection connection = getConnection();
		IDataSet databaseDataSet = connection.createDataSet();
		ITable actualTable = databaseDataSet.getTable("SUBJECTS");
		IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(
                Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("subjects_expected.xml"));
		ITable expectedTable = expectedDataSet.getTable("SUBJECTS");

		Assertion.assertEquals(expectedTable, actualTable);
	}
	
	@Test
	void givenExpectedResult_whenRead_ThenActualResultEqualExpectedReturned() {
		dao.create(new Subject("Mathematic"));
		
		String actual = dao.read(1).getName();
		
		assertEquals("Mathematic", actual);
	}

	@Override
	protected IDataSet getDataSet() throws Exception {
		return null;
	}
}
