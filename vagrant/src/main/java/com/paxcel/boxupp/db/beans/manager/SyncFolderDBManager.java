package com.paxcel.boxupp.db.beans.manager;

import java.sql.SQLException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.JsonNode;

import com.boxupp.db.beans.MachineConfigurationBean;
import com.boxupp.db.beans.SyncFoldersBean;
import com.google.gson.Gson;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.ForeignCollection;
import com.paxcel.boxupp.db.DAOProvider;
import com.paxcel.responseBeans.StatusBean;

public class SyncFolderDBManager {
	private static Logger logger = LogManager.getLogger(SyncFolderDBManager.class.getName());
	protected static Dao<SyncFoldersBean, Integer> syncFolderDao = null;
	private static SyncFolderDBManager syncFolderDBManager = null;

	public static SyncFolderDBManager getInstance(){
		if(syncFolderDBManager == null){
			syncFolderDBManager = new SyncFolderDBManager();
		}
		return syncFolderDBManager;
		
	}
	
	private  SyncFolderDBManager() {
		syncFolderDao = DAOProvider.getInstance().fetchDao(SyncFoldersBean.class);
	}
	public StatusBean save(MachineConfigurationBean machineConfig,
			JsonNode syncFolderData) {
		StatusBean statusBean = new StatusBean();
		
		Gson portforwarded = new Gson();
		List<SyncFoldersBean> syncFolders  = (List<SyncFoldersBean>) portforwarded.fromJson(syncFolderData.toString(), SyncFoldersBean.class);
		
		try {
			for (SyncFoldersBean syncFolder: syncFolders){
				syncFolder.setMachineConfig(machineConfig);
				syncFolderDao.create(syncFolder);
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


	public StatusBean update(ForeignCollection<SyncFoldersBean> syncFolders) {
		StatusBean statusBean = new StatusBean();
		for(SyncFoldersBean syncFolder : syncFolders){
			try {
				syncFolderDao.update(syncFolder);
			} catch (SQLException e) {
				statusBean.setStatusCode(1);
				statusBean.setStatusMessage("Error in updating sync Folder  : "+e.getMessage());
			}
		}
		statusBean.setStatusCode(0);
		statusBean.setStatusMessage("Sync Folder upadte successfully");
		return statusBean;
	}
}
