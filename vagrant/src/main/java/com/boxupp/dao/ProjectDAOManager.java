package com.boxupp.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.JsonNode;

import com.boxupp.db.beans.ProjectBean;
import com.boxupp.db.beans.ProjectProviderMappingBean;
import com.boxupp.db.beans.ProviderBean;
import com.boxupp.db.beans.UserProjectMapping;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.SelectArg;
import com.paxcel.boxupp.db.DAOProvider;
import com.paxcel.boxupp.utilities.Utilities;
import com.paxcel.responseBeans.StatusBean;

public class ProjectDAOManager implements DAOImplInterface  {
	
	private static Logger logger = LogManager.getLogger(ProjectDAOManager.class.getName());
	protected static Dao<ProjectBean, Integer> projectDao = null;
	protected static Dao<ProjectProviderMappingBean, Integer> projectProviderMappingDao = null;
	protected static Dao<UserProjectMapping, Integer> userProjectMappingDao = null;
	private static ProjectDAOManager projectDBManager;
	private static PreparedQuery<ProjectBean> userProjectsQuery = null;
	
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
			if(rowsUpdated == 1) Utilities.getInstance().changeActiveDirectory(projectBean.getProjectId());
			Utilities.getInstance().createProjectDirectory(projectBean);
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

	
	
	public <E> List<E> readAllDB() {
		List<ProjectBean> projectsList = null;
		try{
			projectsList = projectDao.queryForAll();
		}catch(SQLException e){
			logger.error("Error querying the projects list from DB : " + e.getMessage());
		}
		return (List<E>) projectsList;
	}

	@Override
	public StatusBean delete(String id) {
		StatusBean statusBean = new StatusBean();
		try {
			Utilities.getInstance().deleteProjectFile(projectDao.queryForId(Integer.parseInt(id)).getName());
			projectDao.deleteById(Integer.parseInt(id));
			List<ProjectProviderMappingBean> projectMappping = projectProviderMappingDao.queryForEq("projectId", Integer.parseInt(id));
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

	
	@Override
	public <T>T read(String id) {
		ProjectBean project = null;
		try{
			project = projectDao.queryForId(Integer.parseInt(id));
		}catch(SQLException e){
			logger.error("Error querying the project from DB : " + e.getMessage());
		}
		return (T) project;
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

	
	public <E> List<E> readMappedData(String mappedId) {
		List<ProjectBean> projectList = new ArrayList<ProjectBean>();
		try {
			
			List<UserProjectMapping> users = userProjectMappingDao.queryForAll();
			for(UserProjectMapping user : users){
				System.out.println(user.getId());
			}
			userProjectsQuery.setArgumentHolderValue(0,UserDAOManager.getInstance().userDetailDao.queryForId(Integer.parseInt(mappedId)));
			projectList = projectDao.query(userProjectsQuery);
			for(ProjectBean project : projectList){
				
			}
		} catch (NumberFormatException e) {
			logger.error("Error parsing user ID : "+e.getMessage());
		} catch (SQLException e) {
			logger.error("Error fetching projects for user "+mappedId+ " : "+e.getMessage());
		}
		return (List<E>)projectList;
	}

	
	public <T> T populateMappingBean( T mappingBean, String...ids) {
		ProjectProviderMappingBean projectProviderMapping = new ProjectProviderMappingBean();
		projectProviderMapping.setProjectId(Integer.parseInt(ids[0]));
		projectProviderMapping.setProviderId(((ProviderBean) mappingBean).getProviderId());
		return (T) projectProviderMapping;
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
}
