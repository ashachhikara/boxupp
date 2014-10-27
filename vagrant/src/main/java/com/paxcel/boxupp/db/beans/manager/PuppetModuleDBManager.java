package com.paxcel.boxupp.db.beans.manager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.JsonNode;

import com.boxupp.db.beans.MachineConfigurationBean;
import com.boxupp.db.beans.ProjectBean;
import com.boxupp.db.beans.PuppetModuleBean;
import com.boxupp.db.beans.PuppetModuleMachineConfigMapping;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.SelectArg;
import com.paxcel.boxupp.db.DAOProvider;
import com.paxcel.responseBeans.StatusBean;

public class PuppetModuleDBManager implements DBBeansManager {
	private static Logger logger = LogManager.getLogger(PuppetModuleDBManager.class.getName());

	protected static Dao<PuppetModuleBean, Integer> puppetModuleDao = null;
	protected static Dao<PuppetModuleMachineConfigMapping, Integer> puppetModuleMappingDao = null;
	private static PuppetModuleDBManager puppetModuleDBManager;
	private PreparedQuery<PuppetModuleBean> moduleForProjectQuery = null;

	public static PuppetModuleDBManager getInstance() {
		if (puppetModuleDBManager == null) {
			puppetModuleDBManager = new PuppetModuleDBManager();
		}
		return puppetModuleDBManager;
	}

	private PuppetModuleDBManager() {
		puppetModuleDao = DAOProvider.getInstance().fetchDao(PuppetModuleBean.class);
		puppetModuleMappingDao = DAOProvider.getInstance().fetchDao(PuppetModuleMachineConfigMapping.class);
	}

	/*@Override
	public StatusBean createDB(JsonNode newData) {
		PuppetModuleBean puppetModuleBean = null;
		Gson puppetModuleData = new GsonBuilder().setDateFormat(
				"yyyy'-'MM'-'dd HH':'mm':'ss").create();
		puppetModuleBean = puppetModuleData.fromJson(newData.toString(),
				PuppetModuleBean.class);
		StatusBean statusBean = new StatusBean();
		try {
			puppetModuleDao.create(puppetModuleBean);
		} catch (SQLException e) {
			logger.error("Error saving a new puppet module : " + e.getMessage());
			statusBean.setStatusCode(1);
			statusBean.setStatusMessage("Error saving a new puppet module : "
					+ e.getMessage());
			e.printStackTrace();
		}
		statusBean.setStatusCode(0);
		statusBean.setStatusMessage("Puppet module saved successfully");
		return statusBean;
	}*/

/*	@Override
	public <E> List<E> readAllDB() {
		List<PuppetModuleBean> puppetModulesList = null;
		try {
			puppetModulesList = puppetModuleDao.queryForAll();
		} catch (SQLException e) {
			logger.error("Error querying the projects list from DB : "
					+ e.getMessage());
		}
		return (List<E>) puppetModulesList;

	}*/

	@Override
	public StatusBean update(JsonNode updatedData) {
		PuppetModuleBean shellScriptBean = null;
		Gson shellScriptData = new GsonBuilder().setDateFormat("yyyy'-'MM'-'dd HH':'mm':'ss").create();
		shellScriptBean = shellScriptData.fromJson(updatedData.toString(), PuppetModuleBean.class);
		StatusBean statusBean = new StatusBean();
		try {
			puppetModuleDao.update(shellScriptBean);
		} catch (SQLException e) {
			logger.error("Error updating a puppet module : " + e.getMessage());
			statusBean.setStatusCode(1);
			statusBean.setStatusMessage("Error updating a puppet module : "+ e.getMessage());
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
			List<PuppetModuleMachineConfigMapping> puppetModuleMappping = puppetModuleMappingDao
					.queryForEq("puppet_id", Integer.parseInt(id));
			for (PuppetModuleMachineConfigMapping puppetModule : puppetModuleMappping) {
				puppetModuleMappingDao.delete(puppetModule);
			}
		} catch (SQLException e) {
			logger.error("Error deleting a puppet Module : " + e.getMessage());
			statusBean.setStatusCode(1);
			statusBean.setStatusMessage("Error deleting  puppet Module : "+ e.getMessage());
			e.printStackTrace();
		}
		statusBean.setStatusCode(0);
		statusBean.setStatusMessage("Puppet module deleted successfully");
		return statusBean;
	}

	/*@Override
	public <T> T readDB(String id) {
		PuppetModuleBean puppetModules = null;
		try {
			puppetModules = puppetModuleDao.queryForId(Integer.parseInt(id));
		} catch (SQLException e) {
			logger.error("Error querying the puppet Module from DB : "
					+ e.getMessage());
		}
		return (T) puppetModules;
	}*/

	@Override
	public StatusBean create(String mappedId, JsonNode newData) {
		PuppetModuleBean puppetModuleBean = null;
		Gson puppetModuleData = new GsonBuilder().setDateFormat("yyyy'-'MM'-'dd HH':'mm':'ss").create();
		puppetModuleBean = puppetModuleData.fromJson(newData.toString(),PuppetModuleBean.class);
		StatusBean statusBean = new StatusBean();
		try {
			puppetModuleDao.create(puppetModuleBean);
			ProjectBean project = ProjectDBManager.getInstance().projectDao
					.queryForId(Integer.parseInt(mappedId));
			PuppetModuleMachineConfigMapping moduleMapping = new PuppetModuleMachineConfigMapping(
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

	@Override
	public <E> List<E> read(String mappedId) {
		List<PuppetModuleBean> projectList = new ArrayList<PuppetModuleBean>();
		try {
			if (moduleForProjectQuery == null) {
				moduleForProjectQuery = prepareModuleForProjectQuery();
			}

			moduleForProjectQuery.setArgumentHolderValue(0, ProjectDBManager
					.getInstance().projectDao.queryForId(Integer.parseInt(mappedId)));
			projectList = puppetModuleDao.query(moduleForProjectQuery);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return (List<E>) projectList;
	}

	/*@Override
	public <T> T populateMappingBean(T mappingBean, String... ids) {
		return mappingBean;
		
		 * PuppetModuleMachineConfigMapping userProjectMappingBean = null; try {
		 * Pupp = new
		 * UserProjectMapping(userDetailDao.queryForId(Integer.parseInt
		 * (ids[0])), (ProjectBean) mappingBean);
		 * userProjectMappingDao.create(userProjectMappingBean); } catch
		 * (SQLException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); } catch (NumberFormatException e1) { // TODO
		 * Auto-generated catch block e1.printStackTrace(); } return (T)
		 * userProjectMappingBean;
		 
	}*/

	public StatusBean saveMachineMappingWithModule(String machineId,
			String moduleId, String projectId) {
		StatusBean statusBean = new StatusBean();
		try {
			ProjectBean project = ProjectDBManager.getInstance().projectDao
					.queryForId(Integer.parseInt(projectId));
			PuppetModuleBean puppetModule = puppetModuleDao.queryForId(Integer.parseInt(moduleId));
			MachineConfigurationBean machineConfig = MachineConfigDBManager.getInstance().
					machineConfigDao.queryForId(Integer.parseInt(machineId));
			puppetModuleMappingDao.updateBuilder()
			.updateColumnValue(PuppetModuleMachineConfigMapping.MACHINE_ID_FIELD_NAME,machineConfig)
					.where().eq(PuppetModuleMachineConfigMapping.PROJECT_ID_FIELD_NAME, project)
					.and().eq(PuppetModuleMachineConfigMapping.MODULE_ID_FIELD_NAME,	puppetModule);

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
		statusBean.setStatusMessage("Machine MApping with  Spuppet Module saved successfully");

		return statusBean;

	}

	private PreparedQuery<PuppetModuleBean> prepareModuleForProjectQuery()	throws SQLException {

		QueryBuilder<PuppetModuleMachineConfigMapping, Integer> moduleProjectQb = puppetModuleMappingDao.queryBuilder();
		moduleProjectQb.selectColumns(PuppetModuleMachineConfigMapping.MODULE_ID_FIELD_NAME);
		SelectArg projectSelectArg = new SelectArg();
		moduleProjectQb.where().eq(	PuppetModuleMachineConfigMapping.PROJECT_ID_FIELD_NAME, projectSelectArg);
		QueryBuilder<PuppetModuleBean, Integer> moduleQb = puppetModuleDao.queryBuilder();
		moduleQb.where().in(PuppetModuleBean.ID_FIELD_NAME,moduleProjectQb);
		return moduleQb.prepare();
	}
}
