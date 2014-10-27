package com.paxcel.boxupp.db;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.boxupp.db.beans.MachineConfigurationBean;
import com.boxupp.db.beans.MachineProjectMapping;
import com.boxupp.db.beans.ProjectBean;
import com.boxupp.db.beans.ProviderBean;
import com.boxupp.db.beans.ShellScriptBean;
import com.boxupp.db.beans.ShellScriptMachineConfigMapping;
import com.boxupp.db.beans.UserDetailBean;
import com.boxupp.db.beans.UserProjectMapping;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.table.TableUtils;
import com.paxcel.boxupp.db.beans.manager.ProjectDBManager;

public class DAOProvider {
	
	private static Logger logger = LogManager.getLogger(DAOProvider.class.getName());
	private static DAOProvider daoController;
	
	public static DAOProvider getInstance(){
		if(daoController == null){
			daoController = new DAOProvider();
		}
		return daoController;
	}
	
	public DAOProvider(){
		
	}
	public  <T> Dao<T, Integer> fetchDao(Class<T> T){
		Dao<T, Integer> projectDao = null;
		try{
			projectDao = DaoManager.createDao(DBConnectionManager.getInstance().fetchDBConnection(), T);
		}catch(SQLException e){
			logger.error("Error initializing DAO access object for"+T +" "+e.getMessage());
		}
		return projectDao;
	}
	
	public Dao<ProjectBean,Integer> fetchProjectDao(){
		Dao<ProjectBean, Integer> projectDao = null;
		try{
			projectDao = DaoManager.createDao(DBConnectionManager.getInstance().fetchDBConnection(), ProjectBean.class);
		}catch(SQLException e){
			logger.error("Error initializing DAO access object for ProjectBean.class : "+e.getMessage());
		}
		return projectDao;
	}
	
	public Dao<ProviderBean,Integer> fetchProviderDao(){
		Dao<ProviderBean, Integer> providerDao = null;
		try{
			
			providerDao = DaoManager.createDao(DBConnectionManager.getInstance().fetchDBConnection(), ProviderBean.class);
		}catch(SQLException e){
			logger.error("Error initializing DAO access object for ProviderBean.class : "+e.getMessage());
		}
		return providerDao;
	}
	public Dao<MachineConfigurationBean,Integer> fetchMachineConfigDao(){
		Dao<MachineConfigurationBean, Integer> machineConfigDao = null;
		try{
			machineConfigDao = DaoManager.createDao(DBConnectionManager.getInstance().fetchDBConnection(), MachineConfigurationBean.class);
		}catch(SQLException e){
			logger.error("Error initializing DAO access object for MachineConfigurationBean.class : "+e.getMessage());
		}
		return machineConfigDao;
	} 
	public Dao<MachineProjectMapping,Integer> fetchMachineMappingDao(){
		Dao<MachineProjectMapping, Integer> machineMappingDao = null;
		try{
			machineMappingDao = DaoManager.createDao(DBConnectionManager.getInstance().fetchDBConnection(), MachineProjectMapping.class);
		}catch(SQLException e){
			logger.error("Error initializing DAO access object for MachineMapping.class : "+e.getMessage());
		}
		return machineMappingDao;
	} 
	
	public Dao<ShellScriptBean,Integer> fetchShellScriptDao(){
		Dao<ShellScriptBean, Integer> shellScriptDao = null;
		try{
			
			shellScriptDao = DaoManager.createDao(DBConnectionManager.getInstance().fetchDBConnection(), ShellScriptBean.class);
		}catch(SQLException e){
			logger.error("Error initializing DAO access object for ShellScriptBean.class : "+e.getMessage());
		}
		return shellScriptDao;
	}
	public Dao<ShellScriptMachineConfigMapping,Integer> fetchShellScriptMappingDao(){
		Dao<ShellScriptMachineConfigMapping, Integer> shellScriptMappingDao = null;
		try{
			
			shellScriptMappingDao = DaoManager.createDao(DBConnectionManager.getInstance().fetchDBConnection(), ShellScriptMachineConfigMapping.class);
		}catch(SQLException e){
			logger.error("Error initializing DAO access object for ProviderBean.class : "+e.getMessage());
		}
		return shellScriptMappingDao;
	}
	public static void main(String args[]) throws SQLException{
		
		//DELETE TABLES FROM DATABASE //
//		TableUtils.dropTable(DBManager.getInstance().fetchDBConnection(), ProjectBean.class, false);
//		TableUtils.dropTable(DBManager.getInstance().fetchDBConnection(), ProviderBean.class, false);
		
		//CREATE TABLES FROM DATABASE //
//		TableUtils.createTable(DBManager.getInstance().fetchDBConnection(), ProjectBean.class);
//		TableUtils.createTable(DBManager.getInstance().fetchDBConnection(), ProviderBean.class);
		
		//TableUtils.dropTable(DBManager.getInstance().fetchDBConnection(), ProviderBean.class, false);
		// Create Provider 1 //
		TableUtils.dropTable(DBConnectionManager.getInstance().fetchDBConnection(), ProjectBean.class, false);
		//TableUtils.createTable(DBManager.getInstance().fetchDBConnection(), ProjectBean.class);
		//TableUtils.dropTable(DBManager.getInstance().fetchDBConnection(), UserDetailBean.class, false);
		//TableUtils.dropTable(DBManager.getInstance().fetchDBConnection(), UserProjectMapping.class, false);
		//TableUtils.createTable(DBManager.getInstance().fetchDBConnection(), ProjectBean.class);
		//TableUtils.createTable(DBManager.getInstance().fetchDBConnection(), ProviderBean.class);
		//TableUtils.createTable(DBManager.getInstance().fetchDBConnection(), UserDetailBean.class);
		//TableUtils.createTable(DBManager.getInstance().fetchDBConnection(), UserProjectMapping.class);
		//TableUtils.createTableIfNotExists(DBManager.getInstance().fetchDBConnection(), UserProjectMapping.class);
		//TableUtils.createTableIfNotExists(DBManager.getInstance().fetchDBConnection(), UserDetailBean.class);
		TableUtils.createTable(DBConnectionManager.getInstance().fetchDBConnection(), ProviderBean.class);
		//TableUtils.dropTable(DBManager.getInstance().fetchDBConnection(), ProviderBean.class, false);

		ProviderBean providerBean = new ProviderBean();
		providerBean.setDisabled(false);
		providerBean.setName("Docker");
		DAOProvider.getInstance().fetchDao(ProviderBean.class).create(providerBean);
		
		providerBean = new ProviderBean();
		providerBean.setDisabled(false);
		providerBean.setName("VirtualBox");
		DAOProvider.getInstance().fetchProviderDao().create(providerBean);
		
	/*	UserDetailBean userDetailBean  = new UserDetailBean();
		userDetailBean.setFirstName("boxupp");
		userDetailBean.setLastName("tool");
		userDetailBean.setIsDisabled(false);
		userDetailBean.setMailId("boxupp@gmail.com");
		userDetailBean.setPassword("boxupp");
		userDetailBean.setUserType("admin");
		DaoController.getInstance().fetchDao(UserDetailBean.class).create(userDetailBean);
		List<UserDetailBean> userdata = DaoController.getInstance().fetchDao(UserDetailBean.class).queryBuilder().where().eq("mailId", "boxupp@gmail.com").and().eq("password", "boxupp").query();
		System.out.println(userdata.get(0).getId());*/
		
		//Create Provider 2 //
		
		
	/*	List<ProjectBean> projects = ProjectDBManager.getInstance().readAllDB();
		for(ProjectBean project : projects){
			System.out.println(project.getName());
		}
		Collection<ProjectBean> projectsCollection = projects;
		UserProjectMapping userProjects = new UserProjectMapping();
		userProjects.setUserId(1);
		userProjects.setProjects(projectsCollection);
		DaoController.getInstance().fetchDao(UserProjectMapping.class).create(userProjects);*/
		/*List<UserProjectMapping> userProjects = DaoController.getInstance().fetchDao(UserProjectMapping.class).queryForAll();
		for(UserProjectMapping user : userProjects){
			System.out.println(user.getId());
		}
		
		UserProjectMapping userpro = DaoController.getInstance().fetchDao(UserProjectMapping.class).queryForId(1);
		for (ProjectBean project : userpro.getProjects()){
			System.out.println(project.getName());
		}*/
		
	}
}
