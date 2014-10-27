package com.boxupp.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.JsonNode;

import com.boxupp.db.DAOProvider;
import com.boxupp.db.beans.MachineConfigurationBean;
import com.boxupp.db.beans.ProjectBean;
import com.boxupp.db.beans.PuppetModuleBean;
import com.boxupp.db.beans.PuppetModuleMapping;
import com.boxupp.db.beans.UserDetailBean;
import com.boxupp.responseBeans.StatusBean;
import com.boxupp.utilities.PuppetUtilities;
import com.boxupp.utilities.Utilities;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.SelectArg;

public class PuppetModuleDAOManager  implements DAOImplInterface{
	private static Logger logger = LogManager.getLogger(PuppetModuleDAOManager.class.getName());

	protected static Dao<PuppetModuleBean, Integer> puppetModuleDao = null;
	public static Dao<PuppetModuleMapping, Integer> puppetModuleMappingDao = null;
	private static PuppetModuleDAOManager puppetModuleDBManager;
	private PreparedQuery<PuppetModuleBean> moduleForProjectQuery = null;

	public static PuppetModuleDAOManager getInstance(){
		if(puppetModuleDBManager == null){
			puppetModuleDBManager = new PuppetModuleDAOManager();
		}
		return puppetModuleDBManager;
	}
	
	private PuppetModuleDAOManager() {
		puppetModuleDao = DAOProvider.getInstance().fetchDao(PuppetModuleBean.class);
		puppetModuleMappingDao = DAOProvider.getInstance().fetchDao(PuppetModuleMapping.class);
	}
	
	@Override
	public StatusBean create(JsonNode newData) {
		PuppetModuleBean puppetModuleBean = null;
		Gson puppetModuleData = new GsonBuilder().setDateFormat("yyyy'-'MM'-'dd HH':'mm':'ss").create();
		puppetModuleBean = puppetModuleData.fromJson(newData.toString(),PuppetModuleBean.class);
		StatusBean statusBean = new StatusBean();
		try {
			puppetModuleDao.create(puppetModuleBean);
			ProjectBean project = ProjectDAOManager.getInstance().projectDao
					.queryForId(Integer.parseInt(newData.get("ProjectId").getTextValue()));
			PuppetModuleMapping moduleMapping = new PuppetModuleMapping(
					null, project, puppetModuleBean);
			puppetModuleMappingDao.create(moduleMapping);

		} catch (SQLException e) {
			logger.error("Error saving a new puppet module : " + e.getMessage());
			statusBean.setStatusCode(1);
			statusBean.setStatusMessage("Error saving a new puppet module : "+ e.getMessage());
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
		PuppetModuleBean puppetModuleBean  = null;
		Gson puppetModuleData = new GsonBuilder().setDateFormat("yyyy'-'MM'-'dd HH':'mm':'ss").create();
		puppetModuleBean = puppetModuleData.fromJson(updatedData.toString(), PuppetModuleBean.class);
		StatusBean statusBean = new StatusBean();
		try {
			puppetModuleDao.update(puppetModuleBean);
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
	public StatusBean delete(String puppetModuleId) {
		StatusBean statusBean = new StatusBean();
		try {
			PuppetUtilities.getInstance().deletePuppetModule(puppetModuleDao.queryForId(Integer.parseInt(puppetModuleId)).getModuleName());
			puppetModuleDao.deleteById(Integer.parseInt(puppetModuleId));
			List<PuppetModuleMapping> puppetModuleMappping = puppetModuleMappingDao.queryForEq("puppetId", Integer.parseInt(puppetModuleId));
				for(PuppetModuleMapping puppetModule : puppetModuleMappping){
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
	public <T>T read(String puppetModuleId) {
		PuppetModuleBean puppetModule = null;
		try{
			puppetModule = puppetModuleDao.queryForId(Integer.parseInt(puppetModuleId));
		}catch(SQLException e){
			logger.error("Error querying the module from DB : " + e.getMessage());
		}
		return  (T)puppetModule;
	}
	
	
	public <E>List <E> retriveModulesForProject(String projectId) {
		List<PuppetModuleBean> puppetModuleList = new ArrayList<PuppetModuleBean>();
		try {
			if (moduleForProjectQuery == null) {
				moduleForProjectQuery = prepareModuleForProjectQuery();
			}

			moduleForProjectQuery.setArgumentHolderValue(0, ProjectDAOManager
					.getInstance().projectDao.queryForId(Integer.parseInt(projectId)));
			puppetModuleList = puppetModuleDao.query(moduleForProjectQuery);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return (List<E>) puppetModuleList;
	}
	public StatusBean saveMachineMappingWithModule(String machineId,
			String moduleId, String projectId) {
		StatusBean statusBean = new StatusBean();
		try {
			ProjectBean project = ProjectDAOManager.getInstance().projectDao
					.queryForId(Integer.parseInt(projectId));
			PuppetModuleBean puppetModule = puppetModuleDao.queryForId(Integer.parseInt(moduleId));
			MachineConfigurationBean machineConfig = MachineConfigDAOManager.getInstance().
					machineConfigDao.queryForId(Integer.parseInt(machineId));
			puppetModuleMappingDao.updateBuilder()
			.updateColumnValue(PuppetModuleMapping.MACHINE_ID_FIELD_NAME,machineConfig)
					.where().eq(PuppetModuleMapping.PROJECT_ID_FIELD_NAME, project)
					.and().eq(PuppetModuleMapping.MODULE_ID_FIELD_NAME,	puppetModule);

		} catch (NumberFormatException e) {
			statusBean.setStatusCode(1);
			statusBean.setStatusMessage("Error saving machine Mapping with module : "+ e.getMessage());
			e.printStackTrace();
		} catch (SQLException e) {
			statusBean.setStatusCode(1);
			statusBean.setStatusMessage("Error saving machine Mapping with module : "+ e.getMessage());
			e.printStackTrace();
		}
		statusBean.setStatusCode(0);
		statusBean.setStatusMessage("Machine Mapping with  Spuppet Module saved successfully");

		return statusBean;

	}

	private PreparedQuery<PuppetModuleBean> prepareModuleForProjectQuery()	throws SQLException {

		QueryBuilder<PuppetModuleMapping, Integer> moduleProjectQb = puppetModuleMappingDao.queryBuilder();
		moduleProjectQb.selectColumns(PuppetModuleMapping.MODULE_ID_FIELD_NAME);
		SelectArg projectSelectArg = new SelectArg();
		moduleProjectQb.where().eq(	PuppetModuleMapping.PROJECT_ID_FIELD_NAME, projectSelectArg);
		QueryBuilder<PuppetModuleBean, Integer> moduleQb = puppetModuleDao.queryBuilder();
		moduleQb.where().in(PuppetModuleBean.ID_FIELD_NAME,moduleProjectQb);
		return moduleQb.prepare();
	}	

}
