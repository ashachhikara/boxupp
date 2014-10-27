package com.paxcel.boxupp.db.beans.manager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.JsonNode;

import com.boxupp.db.beans.ProjectBean;
import com.boxupp.db.beans.UserDetailBean;
import com.boxupp.db.beans.UserProjectMapping;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.j256.ormlite.dao.Dao;
import com.paxcel.boxupp.db.DAOProvider;
import com.paxcel.responseBeans.StatusBean;

public class UserDBManager implements DBBeansManager{

	private static Logger logger = LogManager.getLogger(UserDBManager.class.getName());
	protected static Dao<UserDetailBean, Integer>userDetailDao = null;
	protected static Dao<UserProjectMapping, Integer> userProjectMappingDao = null;
	private static UserDBManager userDBManager = null;
	
	public static UserDBManager getInstance(){
		if(userDetailDao == null){
			userDBManager = new UserDBManager();
		}
		return userDBManager;
		
	}
	
	private  UserDBManager() {
		userDetailDao = (Dao<UserDetailBean, Integer>) DAOProvider.getInstance().fetchDao(UserDetailBean.class);
		userProjectMappingDao = (Dao<UserProjectMapping, Integer>) DAOProvider.getInstance().fetchDao(UserProjectMapping.class);
	}
	
	
	public StatusBean create(JsonNode newData) {
		
		UserDetailBean userDetailBean  = null;
		Gson userData = new GsonBuilder().setDateFormat("yyyy'-'MM'-'dd HH':'mm':'ss").create();
		userDetailBean = userData.fromJson(newData.toString(), UserDetailBean.class);
		StatusBean statusBean = new StatusBean();
		try {
			userDetailDao.create(userDetailBean);
		} catch (SQLException e) {
			logger.error("Error saving a new user : " + e.getMessage());
			statusBean.setStatusCode(1);
			statusBean.setStatusMessage("Error saving a new user : "+e.getMessage());
			e.printStackTrace();
		}
		statusBean.setStatusCode(0);
		statusBean.setStatusMessage("User detail saved successfully");
		return statusBean;
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

	/*@Override
	public <E> List<E> readAllDB() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StatusBean updateDB(JsonNode updatedData) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StatusBean deleteDB(String id) {
		// TODO Auto-generated method stub
		return null;
	}
*/
	/*@Override
	public <T>T readDB(String id) {
		UserDetailBean userDetail = null;
		try{
			userDetail = userDetailDao.queryForId(Integer.parseInt(id));
		}catch(SQLException e){
			logger.error("Error querying the user from DB : " + e.getMessage());
		}
		return (T) userDetail;
	}
*/
	/*@Override
	public StatusBean createMappedDB(String MappedId, JsonNode newData) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <E> List<E> readMappedData(String MappedId) {
		// TODO Auto-generated method stub
		return null;
	}*/

	/*@Override
	public <T> T populateMappingBean(T mappingBean, String...ids) {
		// TODO Auto-generated method stub
		//System.out.println("******************************"+ids[0]);
		UserProjectMapping userProjectMappingBean = null;
		try {
			userProjectMappingBean = new UserProjectMapping(userDetailDao.queryForId(Integer.parseInt(ids[0])), (ProjectBean) mappingBean);
			userProjectMappingDao.create(userProjectMappingBean);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NumberFormatException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return (T) userProjectMappingBean;
	}*/

}
