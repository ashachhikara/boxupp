package com.paxcel.boxupp.db.beans.manager;

import java.sql.SQLException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.JsonNode;

import com.boxupp.db.beans.LoginBean;
import com.boxupp.db.beans.UserDetailBean;
import com.google.gson.Gson;
import com.j256.ormlite.dao.Dao;
import com.paxcel.boxupp.db.DAOProvider;
import com.paxcel.responseBeans.StatusBean;

public class LoginDBManager {
	
	
	private static Logger logger = LogManager.getLogger(LoginDBManager.class.getName());
	protected static Dao<UserDetailBean, Integer> userDetailDao = null;
	private static LoginDBManager loginDBManager;
	
	public static LoginDBManager getInstance(){
		if(loginDBManager == null){
			loginDBManager = new LoginDBManager();
		}
		return loginDBManager;
	}
	
	private LoginDBManager() {
		userDetailDao = DAOProvider.getInstance().fetchDao(UserDetailBean.class);
	}
	
	public StatusBean loginAuthorization(JsonNode loginCredentials){
		StatusBean statusBean = new StatusBean();
		LoginBean loginBean = null;
		Gson loginParam = new Gson();
		loginBean = loginParam.fromJson(loginParam.toString(), LoginBean.class);
		Integer id = null ;
		try {
			List<UserDetailBean> userdata = userDetailDao.queryBuilder().where().eq("mailId", loginBean.getLoginId()).and().eq("password", loginBean.getPassword()).query();
			statusBean.setId(userdata.get(0).getUserId());
			
		} catch (SQLException e) {
			statusBean.setStatusCode(1);
			statusBean.setStatusMessage("Error in user login :"+e.getMessage());
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		statusBean.setStatusCode(0);
		statusBean.setStatusMessage("User Login successfully");
		return statusBean;
		
	}
	

}
