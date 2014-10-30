package com.boxupp.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.JsonNode;

import com.boxupp.db.DAOProvider;
import com.boxupp.db.beans.ProviderBean;
import com.boxupp.responseBeans.StatusBean;
import com.j256.ormlite.dao.Dao;

public class ProviderDAOManager implements DAOImplInterface {
	private static Logger logger = LogManager.getLogger(ProjectDAOManager.class.getName());

	protected static Dao<ProviderBean, Integer> providerDao = null;
//	protected static Dao<ProviderBean, Integer> providerMappingDao = null;
	private static ProviderDAOManager providerDAOManager;
	
	public static ProviderDAOManager getInstance(){
		if(providerDAOManager == null){
			providerDAOManager = new ProviderDAOManager();
		}
		return providerDAOManager;
	}
	
	private ProviderDAOManager() {
		providerDao = DAOProvider.getInstance().fetchDao(ProviderBean.class);
//		providerMappingDao = DAOProvider.getInstance().fetchDao(ProviderBean.class);
	}
	@Override
	public StatusBean create(JsonNode newData) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public <E> List<E> retireveProviders() {
		List<ProviderBean> providerList = null;
		try{
			providerList = providerDao.queryForAll();
		}catch(SQLException e){
			logger.error("Error querying the provider list from DB : " + e.getMessage());
		}
		return (List<E>) providerList;
	}
	
	@Override
	public <T>T read(String providerID) {
		ProviderBean providerBean= null;
		try{
			providerBean = providerDao.queryForId(Integer.parseInt(providerID));
		}catch(SQLException e){
			logger.error("Error querying the providers list from DB : " + e.getMessage());
		}
		return (T) providerBean;
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
	
}
