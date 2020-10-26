package com.nesterov.university;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Utils {

	private Utils() {
	}
	
	public static Properties readPropertiesFile(String propertiesFile) {
		Properties prop = new Properties();
		try (InputStream input = Utils.class.getClassLoader().getResourceAsStream(propertiesFile)) {
			prop.load(input);
		} catch (IOException e) {
			e.getMessage();
		}
		return prop;
	}
}
