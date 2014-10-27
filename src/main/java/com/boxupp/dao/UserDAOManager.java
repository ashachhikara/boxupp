package com.boxupp.dao;

import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.JsonNode;

import com.boxupp.db.DAOProvider;
import com.boxupp.db.beans.ProjectBean;
import com.boxupp.db.beans.UserDetailBean;
import com.boxupp.db.beans.UserProjectMapping;
import com.boxupp.responseBeans.StatusBean;
import com.boxupp.responseBeans.UserAuthenticationResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.j256.ormlite.dao.Dao;

public class UserDAOManager implements DAOImplInterface{

	private static Logger logger = LogManager.getLogger(UserDAOManager.class.getName());
	protected static Dao<UserDetailBean, Integer>userDetailDao = null;
	protected static Dao<UserProjectMapping, Integer> userProjectMappingDao = null;
	private static UserDAOManager userDBManager = null;
	
	public static UserDAOManager getInstance(){
		if(userDetailDao == null){
			userDBManager = new UserDAOManager();
		}
		return userDBManager;
		
	}
	
	private  UserDAOManager() {
		userDetailDao = (Dao<UserDetailBean, Integer>) DAOProvider.getInstance().fetchDao(UserDetailBean.class);
		userProjectMappingDao = (Dao<UserProjectMapping, Integer>) DAOProvider.getInstance().fetchDao(UserProjectMapping.class);
	}
	
	@Override
	public StatusBean create(JsonNode newData) {
		
		UserAuthenticationResponse response = new UserAuthenticationResponse();
		UserDetailBean userDetailBean  = null;
		Gson userData = new GsonBuilder().setDateFormat("yyyy'-'MM'-'dd HH':'mm':'ss").create();
		userDetailBean = userData.fromJson(newData.toString(), UserDetailBean.class);
		userDetailBean.setIsDisabled(false);
		userDetailBean.setUserType(1); //admin
		
		try {
			userDetailDao.create(userDetailBean);
		} catch (SQLException e) {
			logger.error("Error registering new user : " + e.getMessage());
			response.setStatusCode(1);
			response.setStatusMessage("Error registering new user : " + e.getMessage());
		}
		response.setStatusCode(0);
		response.setStatusMessage("User created successfully");
		response.setUserID(userDetailBean.getUserId());
		return response;
	}

	
	@Override
	public StatusBean update(JsonNode updatedData) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StatusBean delete(String userId) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public <T>T read(String userId) {
		UserDetailBean userDetail = null;
		try{
			userDetail = userDetailDao.queryForId(Integer.parseInt(userId));
		}catch(SQLException e){
			logger.error("Error querying the user from DB : " + e.getMessage());
		}
		return  (T)userDetail;
	}

	public <T> T populateMappingBean(T mappingBean, String userId) {

		UserProjectMapping userProjectMappingBean = null;
		try {
			System.out.println(Integer.parseInt(userId));
//			userProjectMappingBean = new UserProjectMapping(userDetailDao.queryForId(Integer.parseInt(ids[0])), (ProjectBean) mappingBean);
			userProjectMappingBean = new UserProjectMapping(userDetailDao.queryForId(Integer.parseInt(userId)), (ProjectBean) mappingBean);
			userProjectMappingDao.create(userProjectMappingBean);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (NumberFormatException e1) {
			e1.printStackTrace();
		}
		return (T) userProjectMappingBean;
	}

}
