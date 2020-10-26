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
import com.nesterov.university.model.Audience;

class AudienceDaoTest extends DBTestCase{

	private static final String PROPERTY = "config.properties";
	private final String CREATE_TABLE = "DROP TABLE IF EXISTS audiences; CREATE TABLE audiences(id SERIAL NOT NULL, room_number INTEGER NOT NULL, capacity INTEGER NOT NULL);";

	private ApplicationContext context;
	private JdbcTemplate template;
	private AudienceDao dao;
	private Properties properties;
	protected IDatabaseTester tester;
	
	public AudienceDaoTest() {
		context = new ClassPathXmlApplicationContext("persistence.xml");
		template = (JdbcTemplate) context.getBean("jdbcTemplate");
		template.execute(CREATE_TABLE);
		dao = context.getBean(AudienceDao.class);
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
	
		dao.create(new Audience(14, 87));
	
		IDatabaseConnection connection = getConnection();
		IDataSet databaseDataSet = connection.createDataSet();
		ITable actualTable = databaseDataSet.getTable("AUDIENCES");
		IDataSet expectedDataSet = new FlatXmlDataSetBuilder()
				.build(Thread.currentThread().getContextClassLoader().getResourceAsStream("audiences_expected.xml"));
		ITable expectedTable = expectedDataSet.getTable("AUDIENCES");
		
		Assertion.assertEquals(expectedTable, actualTable);
	}

	@Test
	void givenDataSet_whenDeleteAudience_thenTableHasEqualsDataSet() throws Exception {
		dao.create(new Audience(14, 87));
		dao.create(new Audience(45, 87));
		
		dao.delete(2);	
		
		IDatabaseConnection connection = getConnection();
		IDataSet databaseDataSet = connection.createDataSet();
		ITable actualTable = databaseDataSet.getTable("AUDIENCES");
		IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(
                Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("audiences_expected.xml"));
		ITable expectedTable = expectedDataSet.getTable("AUDIENCES");

		Assertion.assertEquals(expectedTable, actualTable);
	}
	
	@Test
	void givenExpectedCapacityOfRoom_whenRead_thenActualEqualExpectedCapacityReturned() {
		dao.create(new Audience(14, 87));
		
		int actual = dao.read(1).getCapacity();
		
		assertEquals(87, actual);
	}
	
	@Test
	void givenExpectedRoomNumderOfRoom_whenRead_thenActualEqualExpectedRoomNumberReturned() {
		dao.create(new Audience(14, 87));
		
		int actual = dao.read(1).getRoomNumber();
		
		assertEquals(14, actual);
	}

	@Override
	protected IDataSet getDataSet() throws Exception {
		return null;
	}
}
