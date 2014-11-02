package com.boxupp.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.JsonNode;

import com.boxupp.db.DAOProvider;
import com.boxupp.db.beans.DockerLinkBean;
import com.boxupp.db.beans.MachineConfigurationBean;
import com.boxupp.db.beans.SyncFoldersBean;
import com.boxupp.responseBeans.StatusBean;
import com.google.gson.Gson;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.ForeignCollection;

public class DockerLinkDAOManager {
	private static Logger logger = LogManager.getLogger(DockerLinkDAOManager.class.getName());
	protected  static Dao<DockerLinkBean, Integer> dockerLinkDao = null;
	private static DockerLinkDAOManager dockerLinkDBManager = null;

	public static DockerLinkDAOManager getInstance(){
		if(dockerLinkDBManager == null){
			dockerLinkDBManager = new DockerLinkDAOManager();
		}
		return dockerLinkDBManager;
		
	}
	
	private  DockerLinkDAOManager() {
		dockerLinkDao = DAOProvider.getInstance().fetchDao(DockerLinkBean.class);
	}
	
	public StatusBean save(MachineConfigurationBean machineConfig, JsonNode dockerLinkMapping) {
		StatusBean statusBean = new StatusBean();
		//DockerLinkBean dockerLink = new DockerLinkBean();
		Gson dockerLink = new Gson();
		
		
		try {
			for(JsonNode mapping : dockerLinkMapping){
				DockerLinkBean dockerLinkBean = dockerLink.fromJson(mapping.toString(), DockerLinkBean.class);
				dockerLinkBean.setMachineConfig(machineConfig);
				dockerLinkDao.create(dockerLinkBean);
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
	public StatusBean update(ForeignCollection<DockerLinkBean> dockerLinks) {
		StatusBean statusBean = new StatusBean();
		
			try {
				for(DockerLinkBean dockerLink : dockerLinks){
					dockerLinkDao.update(dockerLink);
				}
			} catch (SQLException e) {
				statusBean.setStatusCode(1);
				statusBean.setStatusMessage("Error in dockerLink mapping update : "+e.getMessage());
			}
		
		statusBean.setStatusCode(0);
		statusBean.setStatusMessage("Docker link  update successfully");
		return statusBean;
	}
}
