package com.boxupp.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.JsonNode;

import com.boxupp.db.DAOProvider;
import com.boxupp.db.beans.ForwardedPortsBean;
import com.boxupp.db.beans.MachineConfigurationBean;
import com.boxupp.responseBeans.StatusBean;
import com.google.gson.Gson;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.ForeignCollection;

public class PortMappingDAOManager {
	private static Logger logger = LogManager.getLogger(PortMappingDAOManager.class.getName());
	
	protected static Dao<ForwardedPortsBean, Integer> forwardedPortDao = null;
	
	private static PortMappingDAOManager portMappingDBManager = null;

	public static PortMappingDAOManager getInstance(){
		if(portMappingDBManager == null){
			portMappingDBManager = new PortMappingDAOManager();
		}
		return portMappingDBManager;
		
	}
	
	private  PortMappingDAOManager() {
		forwardedPortDao =  DAOProvider.getInstance().fetchDao(ForwardedPortsBean.class);
		
	}
	public StatusBean save(MachineConfigurationBean machineConfig, JsonNode portForwardData){
		StatusBean statusBean = new StatusBean();
		
		Gson portForwarded = new Gson();
		try{
			for(JsonNode mapping : portForwardData){
				ForwardedPortsBean forwardedPort = portForwarded.fromJson(mapping.toString(), ForwardedPortsBean.class);
				forwardedPort.setMachineConfig(machineConfig);
				forwardedPortDao.create(forwardedPort);
			}
		}
		catch (SQLException e) {
			logger.error("Error creating a new forwarded port mapping : " + e.getMessage());
			statusBean.setStatusCode(1);
			statusBean.setStatusMessage("Error in creating forwarded port :"+ e.getMessage());
		}
	
//		List<ForwardedPortsBean>forwordedPorts  = (List<ForwardedPortsBean>) portforwarded.fromJson(portForwardData.toString(), ForwardedPortsBean.class);
//		try {
//			for(ForwardedPortsBean forwardedPort :forwordedPorts){
//				forwardedPort.setMachineConfig(machineConfig);
//				forwardedPortDao.create(forwardedPort);
//			}
//		} catch (SQLException e) {
//			logger.error("Error creating a new forwarded port mapping : " + e.getMessage());
//			statusBean.setStatusCode(1);
//			statusBean.setStatusMessage("Error in creating forwarded port :"+ e.getMessage());
//
//		}
		statusBean.setStatusCode(0);
		statusBean.setStatusMessage("Forwarded port create successfully");
		return statusBean;
		
	}
	public  StatusBean update(ForeignCollection<ForwardedPortsBean> forwordedPorts) {
		StatusBean statusBean = new StatusBean();
		for(ForwardedPortsBean forwordedPort : forwordedPorts){
			try {
				forwardedPortDao.update((ForwardedPortsBean) forwordedPort);
			} catch (SQLException e) {
				statusBean.setStatusCode(1);
				statusBean.setStatusMessage("Error in updating forwarded Port  : "+e.getMessage());
			}
		}
		statusBean.setStatusCode(0);
		statusBean.setStatusMessage("forwarded port  update successfully");
		return statusBean;
	}
}
