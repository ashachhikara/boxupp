package com.boxupp.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.JsonNode;

import com.boxupp.db.DAOProvider;
import com.boxupp.db.beans.ForwardedPortsBean;
import com.boxupp.db.beans.MachineConfigurationBean;
import com.boxupp.db.beans.ShellScriptMapping;
import com.boxupp.db.beans.SyncFoldersBean;
import com.boxupp.responseBeans.StatusBean;
import com.google.gson.Gson;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.stmt.DeleteBuilder;

public class SyncFolderDAOManager {
	private static Logger logger = LogManager.getLogger(SyncFolderDAOManager.class.getName());
	protected static Dao<SyncFoldersBean, Integer> syncFolderDao = null;
	private static SyncFolderDAOManager syncFolderDBManager = null;

	public static SyncFolderDAOManager getInstance(){
		if(syncFolderDBManager == null){
			syncFolderDBManager = new SyncFolderDAOManager();
		}
		return syncFolderDBManager;
		
	}
	
	private  SyncFolderDAOManager() {
		syncFolderDao = DAOProvider.getInstance().fetchDao(SyncFoldersBean.class);
	}
	public StatusBean save(MachineConfigurationBean machineConfig, JsonNode syncFolderData) {
		StatusBean statusBean = new StatusBean();
		
		Gson syncFolder = new Gson();
		try {
			for(JsonNode mapping : syncFolderData){
				SyncFoldersBean syncFolderMapping = syncFolder.fromJson(mapping.toString(), SyncFoldersBean.class);
				syncFolderMapping.setMachineConfig(machineConfig);
				syncFolderDao.create(syncFolderMapping);
			}
		} catch (SQLException e) {
			logger.error("Error creating a new sync folder mapping : " + e.getMessage());
			statusBean.setStatusCode(1);
			statusBean.setStatusMessage("Error in creating sync folder mapping :"+ e.getMessage());

		}
		statusBean.setStatusCode(0);
		statusBean.setStatusMessage("sync folder mapping create successfully");
		return statusBean;
	}


	public StatusBean update(MachineConfigurationBean machineConfig, JsonNode syncFolderData) {
		StatusBean statusBean = new StatusBean();
		Gson syncFolder = new Gson();
		try {
				for(JsonNode mapping : syncFolderData){
					SyncFoldersBean syncFolderMapping = syncFolder.fromJson(mapping.toString(), SyncFoldersBean.class);
					syncFolderMapping.setMachineConfig(machineConfig);
					syncFolderDao.update(syncFolderMapping);
				}
			
		} catch (SQLException e) {
			statusBean.setStatusCode(1);
			statusBean.setStatusMessage("Error in updating sync Folder  : "+e.getMessage());
		}
		
		statusBean.setStatusCode(0);
		statusBean.setStatusMessage("Sync Folder upadte successfully");
		return statusBean;
	}
	public  StatusBean delete(MachineConfigurationBean machineConfig) {
		StatusBean statusBean = new StatusBean();
			try { 
				DeleteBuilder<SyncFoldersBean, Integer> deleteBuilder = syncFolderDao.deleteBuilder();
				deleteBuilder.where().eq(SyncFoldersBean.MACHINE_ID_FIELD_NAME, machineConfig);
				deleteBuilder.delete();
			} catch (SQLException e) {
				statusBean.setStatusCode(1);
				statusBean.setStatusMessage("Error in deleting  sync Folder  : "+e.getMessage());
			}
		
		statusBean.setStatusCode(0);
		statusBean.setStatusMessage(" sync Folder  deleting successfully");
		return statusBean;
	}
}
