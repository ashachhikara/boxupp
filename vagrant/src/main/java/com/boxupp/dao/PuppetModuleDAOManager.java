package com.boxupp.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.JsonNode;

import com.boxupp.db.beans.ProjectBean;
import com.boxupp.db.beans.PuppetModuleBean;
import com.boxupp.db.beans.PuppetModuleMachineConfigMapping;
import com.boxupp.db.beans.PuppetModuleMapping;
import com.boxupp.db.beans.ShellScriptBean;
import com.boxupp.db.beans.ShellScriptMapping;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.j256.ormlite.dao.Dao;
import com.paxcel.boxupp.db.DAOProvider;
import com.paxcel.responseBeans.StatusBean;

public class PuppetModuleDAOManager  implements DAOImplInterface{
	private static Logger logger = LogManager.getLogger(PuppetModuleDAOManager.class.getName());

	protected static Dao<PuppetModuleBean, Integer> puppetModuleDao = null;
	protected static Dao<PuppetModuleMachineConfigMapping, Integer> puppetModuleMappingDao = null;
	private static PuppetModuleDAOManager puppetModuleDBManager;
	
	public static PuppetModuleDAOManager getInstance(){
		if(puppetModuleDBManager == null){
			puppetModuleDBManager = new PuppetModuleDAOManager();
		}
		return puppetModuleDBManager;
	}
	
	private PuppetModuleDAOManager() {
		puppetModuleDao = DAOProvider.getInstance().fetchDao(PuppetModuleBean.class);
		puppetModuleMappingDao = DAOProvider.getInstance().fetchDao(PuppetModuleMachineConfigMapping.class);
	}
	
	@Override
	public StatusBean create(JsonNode newData) {
		PuppetModuleBean puppetModuleBean  = null;
		Gson puppetModuleData = new GsonBuilder().setDateFormat("yyyy'-'MM'-'dd HH':'mm':'ss").create();
		puppetModuleBean = puppetModuleData.fromJson(newData.toString(), PuppetModuleBean.class);
		StatusBean statusBean = new StatusBean();
		try {
			puppetModuleDao.create(puppetModuleBean);
		} catch (SQLException e) {
			logger.error("Error saving a new puppet module : " + e.getMessage());
			statusBean.setStatusCode(1);
			statusBean.setStatusMessage("Error saving a new puppet module : "+e.getMessage());
			e.printStackTrace();
		}
		statusBean.setStatusCode(0);
		statusBean.setStatusMessage("Puppet module saved successfully");
		return statusBean;
	}

/*	@Override
	public <E> List<E> readAllDB(){
		List<PuppetModuleBean> puppetModulesList = null;
		try{
			puppetModulesList = puppetModuleDao.queryForAll();
		}catch(SQLException e){
			logger.error("Error querying the projects list from DB : " + e.getMessage());
		}
		return (List<E>) puppetModulesList;
		
		
	}
*/
	@Override
	public StatusBean update(JsonNode updatedData) {
		PuppetModuleBean shellScriptBean  = null;
		Gson shellScriptData = new GsonBuilder().setDateFormat("yyyy'-'MM'-'dd HH':'mm':'ss").create();
		shellScriptBean = shellScriptData.fromJson(updatedData.toString(), PuppetModuleBean.class);
		StatusBean statusBean = new StatusBean();
		try {
			puppetModuleDao.update(shellScriptBean);
		} catch (SQLException e) {
			logger.error("Error updating a puppet module : " + e.getMessage());
			statusBean.setStatusCode(1);
			statusBean.setStatusMessage("Error updating a puppet module : "+e.getMessage());
			e.printStackTrace();
		}
		statusBean.setStatusCode(0);
		statusBean.setStatusMessage("Puppet module updated successfully");
		return statusBean;
	}

	@Override
	public StatusBean delete(String id) {
		StatusBean statusBean = new StatusBean();
		try {
			puppetModuleDao.deleteById(Integer.parseInt(id));
			List<PuppetModuleMachineConfigMapping> puppetModuleMappping = puppetModuleMappingDao.queryForEq("puppetId", Integer.parseInt(id));
				for(PuppetModuleMachineConfigMapping puppetModule : puppetModuleMappping){
					puppetModuleMappingDao.delete(puppetModule);
				}
		} catch (SQLException e) {
			logger.error("Error deleting a puppet Module : " + e.getMessage());
			statusBean.setStatusCode(1);
			statusBean.setStatusMessage("Error deleting  puppet Module : "+e.getMessage());
			e.printStackTrace();
		}
		statusBean.setStatusCode(0);
		statusBean.setStatusMessage("Puppet module deleted successfully");
		return statusBean;
	}

	
	@Override
	public <T>T read(String id) {
		PuppetModuleBean puppetModules = null;
		try{
			puppetModules = puppetModuleDao.queryForId(Integer.parseInt(id));
		}catch(SQLException e){
			logger.error("Error querying the puppet Module from DB : " + e.getMessage());
		}
		return (T) puppetModules;
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
	}
	*/

}
