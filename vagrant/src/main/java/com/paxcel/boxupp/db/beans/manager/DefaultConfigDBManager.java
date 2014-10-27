package com.paxcel.boxupp.db.beans.manager;

import java.sql.SQLException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.JsonNode;

import com.boxupp.db.beans.DefaultConfigMapping;
import com.boxupp.db.beans.DefaultConfigurationBean;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.j256.ormlite.dao.Dao;
import com.paxcel.boxupp.db.DAOProvider;
import com.paxcel.responseBeans.StatusBean;

public class DefaultConfigDBManager implements DBBeansManager{
	private static Logger logger = LogManager.getLogger(DefaultConfigDBManager.class.getName());
	protected static Dao<DefaultConfigurationBean, Integer> defaultConfigDao = null;
	protected static Dao<DefaultConfigMapping, Integer> defaultConfigMappingDao = null;
	private static DefaultConfigDBManager defaultConfigDBManager = null;
	
	public static DefaultConfigDBManager getInstance(){
		if(defaultConfigDBManager == null){
			defaultConfigDBManager = new DefaultConfigDBManager();
		}
		return defaultConfigDBManager;
		
	}
	
	private DefaultConfigDBManager(){
		defaultConfigDao = (Dao<DefaultConfigurationBean, Integer>) DAOProvider.getInstance().fetchDao(DefaultConfigurationBean.class);
		defaultConfigMappingDao = (Dao<DefaultConfigMapping, Integer>) DAOProvider.getInstance().fetchDao(DefaultConfigMapping.class);
		
	}

	@Override
	public StatusBean create(String mappedId, JsonNode newData) {
		DefaultConfigurationBean defaultConfigBean  = null;
		Gson defaultConfigData = new GsonBuilder().setDateFormat("yyyy'-'MM'-'dd HH':'mm':'ss").create();
		defaultConfigBean = defaultConfigData.fromJson(newData.toString(), DefaultConfigurationBean.class);
		StatusBean statusBean = new StatusBean();
		try {
			
			defaultConfigDao.create(defaultConfigBean);
		} catch (SQLException e) {
			logger.error("Error saving a default configuration : " + e.getMessage());
			statusBean.setStatusCode(1);
			statusBean.setStatusMessage("Error saving a default configuration : "+e.getMessage());
			e.printStackTrace();
		}
		statusBean.setStatusCode(0);
		statusBean.setStatusMessage("Default Configuration saved successfully");
		return statusBean;
	}



	
	/*public  <E> List<E> read(String mappedId) {
		DefaultConfigurationBean defaultConfig = null;
		try{
			defaultConfig = defaultConfigDao.queryForId(Integer.parseInt(mappedId));
		}catch(SQLException e){
			logger.error("Error querying the default Configuration  from DB : " + e.getMessage());
		}
		return  defaultConfig;
		
	}*/

	@Override
	public StatusBean update(JsonNode updatedData) {
		DefaultConfigurationBean defaultConfigBean  = null;
		Gson defaultConfigData = new GsonBuilder().setDateFormat("yyyy'-'MM'-'dd HH':'mm':'ss").create();
		defaultConfigBean = defaultConfigData.fromJson(updatedData.toString(), DefaultConfigurationBean.class);
		StatusBean statusBean = new StatusBean();
		try {
			defaultConfigDao.update(defaultConfigBean);
		} catch (SQLException e) {
			logger.error("Error updating a default Configuration : " + e.getMessage());
			statusBean.setStatusCode(1);
			statusBean.setStatusMessage("Error updating a default configuration : "+e.getMessage());
			e.printStackTrace();
		}
		statusBean.setStatusCode(0);
		statusBean.setStatusMessage("default configuration updated successfully");
		return statusBean;
	}

	@Override
	public StatusBean delete(String id) {
		return null;
		
	}

	@Override
	public <E> List<E> read(String MappedId) {
		// TODO Auto-generated method stub
		return null;
	}


}
