package com.boxupp.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.JsonNode;

import com.boxupp.db.DAOProvider;
import com.boxupp.db.beans.UserCredentials;
import com.boxupp.db.beans.UserDetailBean;
import com.boxupp.db.beans.UserProjectMapping;
import com.boxupp.responseBeans.UserAuthenticationResponse;
import com.google.gson.Gson;
import com.j256.ormlite.dao.Dao;

public class LoginDAOManager {
	
	
	private static Logger logger = LogManager.getLogger(LoginDAOManager.class.getName());
	protected static Dao<UserDetailBean, Integer> userDetailDao = null;
	protected static Dao<UserProjectMapping, Integer> userProjectMappingDao = null;
	private static LoginDAOManager loginDBManager;
	
	public static LoginDAOManager getInstance(){
		if(loginDBManager == null){
			loginDBManager = new LoginDAOManager();
		}
		return loginDBManager;
	}
	
	private LoginDAOManager() {
		userDetailDao = DAOProvider.getInstance().fetchDao(UserDetailBean.class);
	}
	
	public UserAuthenticationResponse loginAuthorization(JsonNode loginCredentials){
		UserAuthenticationResponse authResponse = new UserAuthenticationResponse();
		UserCredentials loginBean = null;
		Gson loginParam = new Gson();
		loginBean = loginParam.fromJson(loginCredentials.toString(),UserCredentials.class);
		try{
			List<UserDetailBean> userData = userDetailDao.queryBuilder().where().eq("mailId", loginBean.getLoginId()).and().eq("password", loginBean.getPassword()).query();
			if(userData.size() == 1){
				authResponse.setStatusCode(0);
				authResponse.setUserID(userData.get(0).getUserId());
				authResponse.setStatusMessage("User authenticated successfully");
			}else{
				authResponse.setStatusCode(1);
				authResponse.setStatusMessage("User could not be authenticated");
			}
		}
		catch(SQLException e){
			logger.error("Error authorizing user : "+e.getMessage());
			e.printStackTrace();
			authResponse.setStatusCode(1);
			authResponse.setStatusMessage("Error authorizing user credentials");
		}
		return authResponse;
	}
	

}
