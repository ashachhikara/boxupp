package com.paxcel.boxupp.db.beans.manager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.JsonNode;

import com.boxupp.db.beans.DockerLinkBean;
import com.boxupp.db.beans.ForwardedPortsBean;
import com.boxupp.db.beans.MachineConfigurationBean;
import com.boxupp.db.beans.MachineProjectMapping;
import com.boxupp.db.beans.ProjectBean;
import com.boxupp.db.beans.SyncFoldersBean;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.SelectArg;
import com.paxcel.boxupp.db.DAOProvider;
import com.paxcel.responseBeans.StatusBean;

public class MachineConfigDBManager implements DBBeansManager {
	private static Logger logger = LogManager.getLogger(MachineConfigDBManager.class.getName());
	protected static Dao<MachineConfigurationBean, Integer>machineConfigDao = null;
	protected static Dao<MachineProjectMapping, Integer> machineMappingDao = null;
	private static MachineConfigDBManager machineConfigDBManager = null;
	private PreparedQuery<MachineConfigurationBean> machineForProjectQuery = null;

	public static MachineConfigDBManager getInstance(){
		if(machineConfigDBManager == null){
			machineConfigDBManager = new MachineConfigDBManager();
		}
		return machineConfigDBManager;
		
	}
	
	private  MachineConfigDBManager() {
		machineConfigDao = DAOProvider.getInstance().fetchDao(MachineConfigurationBean.class);
		machineMappingDao = DAOProvider.getInstance().fetchDao(MachineProjectMapping.class);
	}

	
	public StatusBean createDB(JsonNode newData) {
		MachineConfigurationBean machineConfigBean  = null;
		Gson machineConfigData = new GsonBuilder().setDateFormat("yyyy'-'MM'-'dd HH':'mm':'ss").create();
		machineConfigBean = machineConfigData.fromJson(newData.toString(),MachineConfigurationBean.class);
		StatusBean statusBean = new StatusBean();
		try {
			machineConfigDao.create(machineConfigBean);
			/*MachineMapping machineMapping = new MachineMapping(Integer.parseInt(projectID), machineConfigBean.getId());
			machineMappingDao.create(machineMapping);*/

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

	@Override
	public StatusBean create(String mappedId, JsonNode newData) {
		MachineConfigurationBean machineConfigBean  = null;
		Gson machineConfigData = new GsonBuilder().setDateFormat("yyyy'-'MM'-'dd HH':'mm':'ss").create();
		machineConfigBean = machineConfigData.fromJson(newData.toString(),MachineConfigurationBean.class);
		StatusBean statusBean = new StatusBean();
		try {
			machineConfigDao.create(machineConfigBean);
			PortMappingDBMAnager.getInstance().save(machineConfigBean, newData.get("portMappings"));
			SyncFolderDBManager.getInstance().save(machineConfigBean, newData.get("syncFolders"));
			if(newData.get("dockerLinkContainers") != null){
				DockerLinkDBManager.getInstance().save(machineConfigBean, newData.get("dockerLinkContainers"));
			}
			ProjectBean project = ProjectDBManager.projectDao.queryForId(Integer.parseInt(mappedId));
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

	@Override
	public <E> List<E> read(String mappedId) {
		List<MachineConfigurationBean> machineList = new ArrayList<MachineConfigurationBean>();
		try {
		if (machineForProjectQuery == null) {
			machineForProjectQuery =  makeMachineForProjectQuery();
		}
		 machineForProjectQuery.setArgumentHolderValue(0, ProjectDBManager.projectDao.queryForId(Integer.parseInt(mappedId)));
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


	@Override
	public StatusBean update(JsonNode updatedData){
		
		MachineConfigurationBean machineConfigBean  = null;
		Gson projectData = new GsonBuilder().setDateFormat("yyyy'-'MM'-'dd HH':'mm':'ss").create();
		machineConfigBean = projectData.fromJson(updatedData.toString(),MachineConfigurationBean.class);
		StatusBean statusBean = new StatusBean();
		try {
			DockerLinkDBManager.getInstance().update(machineConfigBean.getDockerLinks());
			PortMappingDBMAnager.getInstance().update(machineConfigBean.getPortMappings());
			SyncFolderDBManager.getInstance().update(machineConfigBean.getSyncFolders());
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
