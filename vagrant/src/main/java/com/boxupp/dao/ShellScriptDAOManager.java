package com.boxupp.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.JsonNode;

import com.boxupp.db.beans.ShellScriptBean;
import com.boxupp.db.beans.ShellScriptMachineConfigMapping;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.j256.ormlite.dao.Dao;
import com.paxcel.boxupp.db.DAOProvider;
import com.paxcel.boxupp.utilities.Utilities;
import com.paxcel.responseBeans.StatusBean;

public class ShellScriptDAOManager implements DAOImplInterface {
	private static Logger logger = LogManager.getLogger(ShellScriptDAOManager.class.getName());
	protected static Dao<ShellScriptBean, Integer> shellScriptDao = null;
	protected static Dao<ShellScriptMachineConfigMapping, Integer> shellScriptMappingDao = null;
	private static ShellScriptDAOManager shellScriptDBManager = null;
	
	public static ShellScriptDAOManager getInstance(){
		if(shellScriptDBManager == null){
			shellScriptDBManager = new ShellScriptDAOManager();
		}
		return shellScriptDBManager;
		
	}
	
	private ShellScriptDAOManager(){
		shellScriptDao = (Dao<ShellScriptBean, Integer>) DAOProvider.getInstance().fetchDao(ShellScriptBean.class);
		shellScriptMappingDao = (Dao<ShellScriptMachineConfigMapping, Integer>) DAOProvider.getInstance().fetchDao(ShellScriptMachineConfigMapping.class);
		
	}
	
	@Override
	public StatusBean create(JsonNode newData) {
		ShellScriptBean shellScriptBean  = null;
		Gson shellScriptData = new GsonBuilder().setDateFormat("yyyy'-'MM'-'dd HH':'mm':'ss").create();
		shellScriptBean = shellScriptData.fromJson(newData.toString(), ShellScriptBean.class);
		StatusBean statusBean = new StatusBean();
		try {
			Utilities.getInstance().writeScriptToDisk(shellScriptBean);
			shellScriptDao.create(shellScriptBean);
		} catch (SQLException e) {
			logger.error("Error saving a new shell script : " + e.getMessage());
			statusBean.setStatusCode(1);
			statusBean.setStatusMessage("Error saving a new shell script : "+e.getMessage());
			e.printStackTrace();
		}
		statusBean.setStatusCode(0);
		statusBean.setStatusMessage("Shell script saved successfully");
		return statusBean;
	}

	@Override
	public StatusBean update(JsonNode updatedData) {
		ShellScriptBean shellScriptBean  = null;
		Gson shellScriptData = new GsonBuilder().setDateFormat("yyyy'-'MM'-'dd HH':'mm':'ss").create();
		shellScriptBean = shellScriptData.fromJson(updatedData.toString(), ShellScriptBean.class);
		StatusBean statusBean = new StatusBean();
		try {
			shellScriptDao.update(shellScriptBean);
		} catch (SQLException e) {
			logger.error("Error updating a shell script : " + e.getMessage());
			statusBean.setStatusCode(1);
			statusBean.setStatusMessage("Error updating a shell script : "+e.getMessage());
			e.printStackTrace();
		}
		statusBean.setStatusCode(0);
		statusBean.setStatusMessage("Shell script updated successfully");
		return statusBean;
	}

	
	/*@Override
	public <E> List<E> readAllDB() {
		List<ShellScriptBean> shellScriptsList = null;
		try{
			shellScriptsList = shellScriptDao.queryForAll();
		}catch(SQLException e){
			logger.error("Error querying the shell script list from DB : " + e.getMessage());
		}
		return (List<E>) shellScriptsList;
		
	}
*/
	@Override
	public StatusBean delete(String id) {
		StatusBean statusBean = new StatusBean();
		try {
			Utilities.getInstance().deleteScriptfileOnDisk(shellScriptDao.queryForId(Integer.parseInt(id)).getScriptName());
			shellScriptDao.deleteById(Integer.parseInt(id));
			List<ShellScriptMachineConfigMapping> shellscriptMappping = shellScriptMappingDao.queryForEq("scriptId", Integer.parseInt(id));
				for(ShellScriptMachineConfigMapping shellScript : shellscriptMappping){
					shellScriptMappingDao.delete(shellScript);
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
		ShellScriptBean shellScript = null;
		try{
			shellScript = shellScriptDao.queryForId(Integer.parseInt(id));
		}catch(SQLException e){
			logger.error("Error querying the shell Script  from DB : " + e.getMessage());
		}
		return (T) shellScript;
	}

	/*@Override
	public StatusBean createMappedDB(String MappedId, JsonNode newData) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <E> List<E> readMappedData(String MappedId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T populateMappingBean(T mappingBean, String...ids) {
		// TODO Auto-generated method stub
		return null;
	}*/
	


}
