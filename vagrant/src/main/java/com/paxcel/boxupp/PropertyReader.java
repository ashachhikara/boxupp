package com.paxcel.boxupp;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PropertyReader {
	private static Logger logger = LogManager.getLogger(PropertyReader.class.getName());
	private static Properties propertyFile = null;
	
	public static Properties getInstance(){
		if(propertyFile == null){
			try{
				new PropertyReader();
			}
			catch(Exception e){
				logger.error("Error reading config.properties file");
			}
		}
		return propertyFile;
	}
	public PropertyReader() throws IOException{
		propertyFile = new Properties();
		InputStream propertiesStream = getClass().getResourceAsStream("/config.properties");
		propertyFile.load(propertiesStream);
	}
	public static void main(String args[]) throws IOException{
		PropertyReader.getInstance();
		
	}
}


