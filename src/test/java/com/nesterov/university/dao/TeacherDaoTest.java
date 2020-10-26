package com.nesterov.university.dao;

import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import com.nesterov.university.model.Gender;
import com.nesterov.university.model.Teacher;

class TeacherDaoTest extends DBTestCase {

	private static final String PROPERTY = "config.properties";
	private final String CREATE_TABLE = "DROP TABLE IF EXISTS teachers; CREATE TABLE teachers (id SERIAL  NOT NULL,"
			+ " first_name VARCHAR(20) NOT NULL, last_name VARCHAR(20) NOT NULL, bith_date DATE NOT NULL,"
			+ " address VARCHAR(20) NOT NULL, email VARCHAR(20) NOT NULL, phone VARCHAR(20) NOT NULL,"
			+ " gender VARCHAR(20) NOT NULL, PRIMARY KEY (id))";

	private ApplicationContext context;
	private JdbcTemplate template;
	private TeacherDao dao;
	private Properties properties;
	protected IDatabaseTester tester;
	
	public TeacherDaoTest() {
		context = new ClassPathXmlApplicationContext("persistence.xml");
		template = (JdbcTemplate) context.getBean("jdbcTemplate");
		template.execute(CREATE_TABLE);
		dao = context.getBean(TeacherDao.class);
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
	void givenDataSet_whenCreate_thenTableHasNewTeacher() throws Exception {
	
		dao.create(new Teacher("Alex", "Nesterov", LocalDate.now(), "Kiev", "malibu8770@gmail.com", "+380930890658",
				Gender.valueOf("MALE")));
	
		IDatabaseConnection connection = getConnection();
		IDataSet databaseDataSet = connection.createDataSet();
		ITable actualTable = databaseDataSet.getTable("TEACHERS");
		IDataSet expectedDataSet = new FlatXmlDataSetBuilder()
				.build(Thread.currentThread().getContextClassLoader().getResourceAsStream("teachers-expected.xml"));
		ITable expectedTable = expectedDataSet.getTable("TEACHERS");
		
		Assertion.assertEquals(expectedTable, actualTable);
	}

	@Test
	void givenDataSet_whenDeleteTeacher_thenTableHasEqualsDataSet() throws Exception {
		dao.create(new Teacher("Alex", "Nesterov", LocalDate.now(), "Kiev", "malibu8770@gmail.com", "+380930890658",
				Gender.valueOf("MALE")));
		dao.create(new Teacher("Alice", "Nesterova", LocalDate.now(), "Kiev", "alice@gmail.com", "+380930890658",
				Gender.valueOf("FEMALE")));
		
		dao.delete(2);	
		
		IDatabaseConnection connection = getConnection();
		IDataSet databaseDataSet = connection.createDataSet();
		ITable actualTable = databaseDataSet.getTable("TEACHERS");
		IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(
                Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("teachers-expected.xml"));
		ITable expectedTable = expectedDataSet.getTable("TEACHERS");

		Assertion.assertEquals(expectedTable, actualTable);
	}
	
	@Test
	void givenExpectedEmailOfTeacher_whenRead_thenExpectedEmailReturned() {
		dao.create(new Teacher("Alex", "Nesterov", LocalDate.now(), "Kiev", "malibu8770@gmail.com", "+380930890658",
				Gender.valueOf("MALE")));
		dao.create(new Teacher("Alice", "Nesterova", LocalDate.now(), "Kiev", "alice@gmail.com", "+380930890658",
				Gender.valueOf("FEMALE")));
		Teacher expected = new Teacher("Alex", "Nesterov", LocalDate.now(), "Kiev", "malibu8770@gmail.com", "+380930890658",
				Gender.valueOf("MALE"));
		
		String actual = dao.read(1).getEmail();
		
		assertEquals("malibu8770@gmail.com", actual);
	}

	@Override
	protected IDataSet getDataSet() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}
