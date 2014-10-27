package com.boxupp.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.JsonNode;

import com.boxupp.db.DAOProvider;
import com.boxupp.db.beans.ProjectBean;
import com.boxupp.db.beans.ProjectProviderMappingBean;
import com.boxupp.db.beans.UserProjectMapping;
import com.boxupp.responseBeans.StatusBean;
import com.boxupp.utilities.Utilities;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.SelectArg;

public class ProjectDAOManager implements DAOImplInterface  {
	
	private static Logger logger = LogManager.getLogger(ProjectDAOManager.class.getName());
	public static Dao<ProjectBean, Integer> projectDao = null;
	protected static Dao<ProjectProviderMappingBean, Integer> projectProviderMappingDao = null;
	protected static Dao<UserProjectMapping, Integer> userProjectMappingDao = null;
	private static ProjectDAOManager projectDBManager;
	private static PreparedQuery<ProjectBean> userProjectsQuery = null;
	private PreparedQuery<ProjectBean> projectForUserQuery = null;


	
	public static ProjectDAOManager getInstance(){
		if(projectDBManager == null){
			projectDBManager = new ProjectDAOManager();
		}
		return projectDBManager;
	}
	
	private ProjectDAOManager() {
		projectDao = DAOProvider.getInstance().fetchDao(ProjectBean.class);
		projectProviderMappingDao = DAOProvider.getInstance().fetchDao(ProjectProviderMappingBean.class);
		userProjectMappingDao = DAOProvider.getInstance().fetchDao(UserProjectMapping.class);
		userProjectsQuery = makeProjectForUserQuery();
	}
	
	@Override
	public StatusBean create(JsonNode newData) {
		ProjectBean projectBean = new ProjectBean();
		Gson projectData = new GsonBuilder().setDateFormat("yyyy'-'MM'-'dd HH':'mm':'ss").create();
		projectBean = projectData.fromJson(newData.toString(),ProjectBean.class);
		StatusBean statusBean = new StatusBean();
		try{
			int rowsUpdated = projectDao.create(projectBean);
			System.out.println(projectBean.getProjectId());
//			if(rowsUpdated == 1) Utilities.getInstance().changeActiveDirectory(projectBean.getProjectId());
			UserDAOManager.getInstance().populateMappingBean(projectBean, newData.get("owner").getValueAsText());
			statusBean.setStatusCode(0);
			statusBean.setData(projectBean);
		}catch(SQLException e){
			logger.error("Error creating a new project : " + e.getMessage());
			statusBean.setStatusCode(1);
			statusBean.setStatusMessage("Error creating project : "+e.getMessage());
			e.printStackTrace();
		}
		System.out.println("Id assigned to new project : "+projectBean.getProjectId() + " creation time : "+projectBean.getCreationTime());
		return statusBean;
	}

	@Override
	public StatusBean update(JsonNode updateddata) {
		ProjectBean projectBean  = null;
		Gson projectData = new GsonBuilder().setDateFormat("yyyy'-'MM'-'dd HH':'mm':'ss").create();
		projectBean = projectData.fromJson(updateddata.toString(), ProjectBean.class);
		StatusBean statusBean = new StatusBean();
		try {
			projectDao.update(projectBean);
		} catch (SQLException e) {
			logger.error("Error updating a  project : " + e.getMessage());
			statusBean.setStatusCode(1);
			statusBean.setStatusMessage("Error updating a project : "+e.getMessage());
			e.printStackTrace();
		}
		statusBean.setStatusCode(0);
		statusBean.setStatusMessage("Project updated successfully");
		return statusBean;
	}

	@Override
	public <T>T read(String projectId) {
		
		ProjectBean project = null;
		try{
			project = projectDao.queryForId(Integer.parseInt(projectId));
		}catch(SQLException e){
			logger.error("Error querying the project from DB : " + e.getMessage());
		}
		return (T) project;
	}

	@Override
	public StatusBean delete(String projectId) {
		StatusBean statusBean = new StatusBean();
		try {
			Utilities.getInstance().deleteProjectFile(projectDao.queryForId(Integer.parseInt(projectId)).getName());
			projectDao.deleteById(Integer.parseInt(projectId));
			List<ProjectProviderMappingBean> projectMappping = projectProviderMappingDao.queryForEq("projectId", Integer.parseInt(projectId));
				for(ProjectProviderMappingBean shellScript : projectMappping){
					projectProviderMappingDao.delete(shellScript);
				}
				
		} catch (SQLException e) {
			logger.error("Error updating a shell script : " + e.getMessage());
			statusBean.setStatusCode(1);
			statusBean.setStatusMessage("Error deleting  shell script : "+e.getMessage());
			e.printStackTrace();
		}
		statusBean.setStatusCode(0);
		statusBean.setStatusMessage("Shell script deleted successfully");
		return statusBean;
	}
	
	public StatusBean createMappedDB(JsonNode newData) {
		ProjectBean projectBean = new ProjectBean();
		Gson projectData = new GsonBuilder().setDateFormat("yyyy'-'MM'-'dd HH':'mm':'ss").create();
		projectBean = projectData.fromJson(newData.toString(),ProjectBean.class);
		StatusBean statusBean = new StatusBean();
		try{
			int rowsUpdated = projectDao.create(projectBean);
			System.out.println(projectBean.getProjectId());
//			if(rowsUpdated == 1) Utilities.getInstance().changeActiveDirectory(projectBean.getProjectId());
			UserDAOManager.getInstance().populateMappingBean(projectBean, newData.get("owner").getValueAsText());
			statusBean.setStatusCode(0);
		}catch(SQLException e){
			logger.error("Error creating a new project : " + e.getMessage());
			statusBean.setStatusCode(1);
			statusBean.setStatusMessage("Error creating project : "+e.getMessage());
			e.printStackTrace();
		}
		System.out.println("Id assigned to new project : "+projectBean.getProjectId() + " creation time : "+projectBean.getCreationTime());
		return statusBean;
	}

	public <E> List<E> retriveProjectsForUser(String UserId) {
		List<ProjectBean> projectList = new ArrayList<ProjectBean>();
		try {
			
			List<UserProjectMapping> users = userProjectMappingDao.queryForAll();
			userProjectsQuery.setArgumentHolderValue(0,UserDAOManager.getInstance().userDetailDao.queryForId(Integer.parseInt(UserId)));
			projectList = projectDao.query(userProjectsQuery);
			
		} catch (NumberFormatException e) {
			logger.error("Error parsing user ID : "+e.getMessage());
		} catch (SQLException e) {
			logger.error("Error fetching projects for user "+UserId+ " : "+e.getMessage());
		}
		return (List<E>)projectList;
	}
	
	private PreparedQuery<ProjectBean> makeProjectForUserQuery(){
		
		QueryBuilder<UserProjectMapping, Integer> userPostQb = userProjectMappingDao.queryBuilder();

		userPostQb.selectColumns(UserProjectMapping.PROJECT_ID_FIELD_NAME);
		SelectArg userSelectArg = new SelectArg();
		QueryBuilder<ProjectBean, Integer> projectQb = null;
		try{
			userPostQb.where().eq(UserProjectMapping.USER_ID_FIELD_NAME, userSelectArg);
			projectQb = projectDao.queryBuilder();
			projectQb.where().in(ProjectBean.ID_FIELD_NAME, userPostQb);
			return projectQb.prepare();
		}
		catch(SQLException e){
			logger.error("Error creating prepared query for fetching user mapped projects : "+e.getMessage());
		}
		return null;
	}
	public String getProviderForProject(String projectId){
		String provider= null;
		try {
			Integer providerId = projectProviderMappingDao.queryForEq("projectId", Integer.parseInt(projectId)).get(0).getProjectId();
			 provider = ProviderDAOManager.getInstance().providerDao.queryForId(providerId).getName();
		} catch (NumberFormatException e) {
			logger.error("Error in finding provider for project :"+e.getMessage());
		} catch (SQLException e) {
			logger.error("Error in sql to finding provider for project :"+e.getMessage());
		}
		return provider;
	}
	

}
