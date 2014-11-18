package com.boxupp.db;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DerbyConfig {
	
//	public static final String DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";
	public static final String JDBC_URL = "jdbc:derby:db;create=true";
	
	private static Logger logger = LogManager.getLogger(DerbyConfig.class.getName());

}
