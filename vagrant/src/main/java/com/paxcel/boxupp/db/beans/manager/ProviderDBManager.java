package com.paxcel.boxupp.db.beans.manager;

import java.sql.SQLException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.JsonNode;

import com.boxupp.db.beans.ProjectProviderMappingBean;
import com.boxupp.db.beans.ProviderBean;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.stmt.query.In;
import com.paxcel.boxupp.db.DAOProvider;
import com.paxcel.responseBeans.StatusBean;

public class ProviderDBManager implements DBBeansManager {
	private static Logger logger = LogManager.getLogger(ProjectDBManager.class.getName());

	protected static Dao<ProviderBean, Integer> providerDao = null;
	protected static Dao<ProjectProviderMappingBean, Integer> providerMappingDao = null;
	private static ProviderDBManager providerDBManager;
	
	public static ProviderDBManager getInstance(){
		if(providerDBManager == null){
			providerDBManager = new ProviderDBManager();
		}
		return providerDBManager;
	}
	
	private ProviderDBManager() {
		providerDao = DAOProvider.getInstance().fetchDao(ProviderBean.class);
		providerMappingDao = DAOProvider.getInstance().fetchDao(ProjectProviderMappingBean.class);
	}
	
	
	public <E> List<E> read() {
		List<ProviderBean> providerList = null;
		try{
			providerList = providerDao.queryForAll();
		}catch(SQLException e){
			logger.error("Error querying the provider list from DB : " + e.getMessage());
		}
		return (List<E>) providerList;
	}

	
	public <T>T readProvider(String id) {
		ProviderBean providerBean= null;
		try{
			providerBean = providerDao.queryForId(Integer.parseInt(id));
		}catch(SQLException e){
			logger.error("Error querying the providers list from DB : " + e.getMessage());
		}
		return (T) providerBean;
	}

	@Override
	public StatusBean create(String MappedId, JsonNode newData) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StatusBean update(JsonNode updatedData) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StatusBean delete(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <E> List<E> read(String MappedId) {
		// TODO Auto-generated method stub
		return null;
	}
	public String getProviderForProject(String projectId){
		String provider= null;
		try {
			Integer providerId = providerMappingDao.queryForEq("projectId", Integer.parseInt(projectId)).get(0).getProjectId();
			 provider = providerDao.queryForId(providerId).getName();
		} catch (NumberFormatException e) {
			logger.error("Error in finding provider for project :"+e.getMessage());
		} catch (SQLException e) {
			logger.error("Error in sql to finding provider for project :"+e.getMessage());
		}
		return provider;
	}
}
