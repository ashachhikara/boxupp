package com.boxupp.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.JsonNode;

import com.boxupp.db.DAOProvider;
import com.boxupp.db.beans.MachineConfigurationBean;
import com.boxupp.db.beans.MachineProjectMapping;
import com.boxupp.db.beans.ProjectBean;
import com.boxupp.responseBeans.StatusBean;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.SelectArg;

public class MachineConfigDAOManager implements DAOImplInterface {
	private static Logger logger = LogManager.getLogger(MachineConfigDAOManager.class.getName());
	protected static Dao<MachineConfigurationBean, Integer>machineConfigDao = null;
	protected static Dao<MachineProjectMapping, Integer> machineMappingDao = null;
	private static MachineConfigDAOManager machineConfigDBManager = null;
	private PreparedQuery<MachineConfigurationBean> machineForProjectQuery = null;
	
	public static MachineConfigDAOManager getInstance(){
		if(machineConfigDao == null){
			machineConfigDBManager = new MachineConfigDAOManager();
		}
		return machineConfigDBManager;
		
	}
	
	private  MachineConfigDAOManager() {
		machineConfigDao = (Dao<MachineConfigurationBean, Integer>) DAOProvider.getInstance().fetchDao(MachineConfigurationBean.class);
		machineMappingDao = (Dao<MachineProjectMapping, Integer>) DAOProvider.getInstance().fetchDao(MachineProjectMapping.class);
		
	}

	@Override
	public StatusBean create(JsonNode newData) {
		MachineConfigurationBean machineConfigBean  = null;
		Gson machineConfigData = new GsonBuilder().setDateFormat("yyyy'-'MM'-'dd HH':'mm':'ss").create();
		machineConfigBean = machineConfigData.fromJson(newData.toString(),MachineConfigurationBean.class);
		StatusBean statusBean = new StatusBean();
		try {
			machineConfigDao.create(machineConfigBean);
			PortMappingDAOManager.getInstance().save(machineConfigBean, newData.get("portMappings"));
			SyncFolderDAOManager.getInstance().save(machineConfigBean, newData.get("syncFolders"));
			if(newData.get("dockerLinkContainers") != null){
				DockerLinkDAOManager.getInstance().save(machineConfigBean, newData.get("dockerLinkContainers"));
			}
			ProjectBean project = ProjectDAOManager.projectDao.queryForId(Integer.parseInt(newData.get("ProjectId").getTextValue()));
			MachineProjectMapping machineProjectMApping = new MachineProjectMapping(project, machineConfigBean);
			machineMappingDao.create(machineProjectMApping);
			
		} catch (SQLException e) {
			logger.error("Error creating a new project : " + e.getMessage());
			statusBean.setStatusCode(1);
			statusBean.setStatusMessage("Error saving machine configuration : "+e.getMessage());
			e.printStackTrace();
		}
		
		statusBean.setStatusCode(0);
		statusBean.setStatusMessage("Machine Congifuration saved successfully");
		return statusBean;
	}

	/*@Override
	public <E> List<E> readAllDB() {
		List<MachineConfigurationBean> machineconfigList = null;
		try{
			machineconfigList = machineConfigDao.queryForAll();
		}catch(SQLException e){
			logger.error("Error querying the machine configuration list  from DB : " + e.getMessage());
		}
		return (List<E>) machineconfigList;
		
	}*/

	@Override
	public StatusBean update(JsonNode updatedData){
		MachineConfigurationBean machineConfigBean  = null;
		Gson projectData = new GsonBuilder().setDateFormat("yyyy'-'MM'-'dd HH':'mm':'ss").create();
		machineConfigBean = projectData.fromJson(updatedData.toString(),MachineConfigurationBean.class);
		StatusBean statusBean = new StatusBean();
		try {
			DockerLinkDAOManager.getInstance().update(machineConfigBean.getDockerLinks());
			PortMappingDAOManager.getInstance().update(machineConfigBean.getPortMappings());
			SyncFolderDAOManager.getInstance().update(machineConfigBean.getSyncFolders());
			machineConfigDao.update(machineConfigBean);
		} catch (SQLException e) {
			logger.error("Error creating a new project : " + e.getMessage());
			statusBean.setStatusCode(1);
			statusBean.setStatusMessage("Error updating machine configuration : "+e.getMessage());
			e.printStackTrace();
		}
		statusBean.setStatusCode(0);
		statusBean.setStatusMessage("Machine Congifuration updated successfully");
		return statusBean;
	}

	@Override
	public StatusBean delete(String id) {
		StatusBean statusBean = new StatusBean();
		try {
			machineConfigDao.deleteById(Integer.parseInt(id));
			List<MachineProjectMapping> machineMapping = DAOProvider.getInstance().fetchMachineMappingDao().queryForEq("machineId", Integer.parseInt(id));
			for(MachineProjectMapping mapping : machineMapping){
				machineMappingDao.delete(mapping);
			}
		} catch (SQLException e) {
			logger.error("Error creating a new project : " + e.getMessage());
			statusBean.setStatusCode(1);
			statusBean.setStatusMessage("Error deleting machine configuration : "+e.getMessage());
			e.printStackTrace();
		}
		statusBean.setStatusCode(0);
		statusBean.setStatusMessage("Machine Congifuration delete successfully");
		return statusBean;
	}

	
	@Override
	public <E> List<E> read(String id) {
		List<MachineConfigurationBean> machineList = new ArrayList<MachineConfigurationBean>();
		try {
		if (machineForProjectQuery == null) {
			machineForProjectQuery =  makeMachineForProjectQuery();
		}
		 machineForProjectQuery.setArgumentHolderValue(0, ProjectDAOManager.projectDao.queryForId(Integer.parseInt(id)));
		 machineList = machineConfigDao.query(machineForProjectQuery);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return (List<E>)machineList;
	}

	
	public  List<MachineConfigurationBean> fetchProjectMachines(String mappedId) {
		List<MachineConfigurationBean> machineList = new ArrayList<MachineConfigurationBean>();
		try {
		if (machineForProjectQuery == null) {
			machineForProjectQuery =  makeMachineForProjectQuery();
		}
		 machineForProjectQuery.setArgumentHolderValue(0, ProjectDAOManager.getInstance().projectDao.queryForId(Integer.parseInt(mappedId)));
		 machineList = machineConfigDao.query(machineForProjectQuery);
		} catch (NumberFormatException e) {
			logger.error("Error in fetching Machine configuration is :"+e.getMessage());
		} catch (SQLException e) {
			logger.error("Error in fetching Machine configuration is :"+e.getMessage());
		}
		return machineList;
	}
	
	private PreparedQuery<MachineConfigurationBean> makeMachineForProjectQuery() throws SQLException {
		
		QueryBuilder<MachineProjectMapping, Integer> machineProjectQb = machineMappingDao.queryBuilder();
		machineProjectQb.selectColumns(MachineProjectMapping.MACHINE_ID_FIELD_NAME);
		SelectArg userSelectArg = new SelectArg();
		machineProjectQb.where().eq(MachineProjectMapping.PROJECT_ID_FIELD_NAME, userSelectArg);
		QueryBuilder<MachineConfigurationBean, Integer> machineConfigQb = machineConfigDao.queryBuilder();
		machineConfigQb.where().in(MachineConfigurationBean.ID_FIELD_NAME, machineProjectQb);
		return machineConfigQb.prepare();
	}
}
