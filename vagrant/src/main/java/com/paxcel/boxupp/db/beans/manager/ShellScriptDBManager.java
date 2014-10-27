package com.paxcel.boxupp.db.beans.manager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.JsonNode;

import com.boxupp.db.beans.MachineConfigurationBean;
import com.boxupp.db.beans.ProjectBean;
import com.boxupp.db.beans.ShellScriptBean;
import com.boxupp.db.beans.ShellScriptMachineConfigMapping;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.SelectArg;
import com.paxcel.boxupp.db.DAOProvider;
import com.paxcel.boxupp.utilities.Utilities;
import com.paxcel.responseBeans.StatusBean;

public class ShellScriptDBManager implements DBBeansManager {
	private static Logger logger = LogManager.getLogger(ShellScriptDBManager.class.getName());
	protected static Dao<ShellScriptBean, Integer> shellScriptDao = null;
	protected static Dao<ShellScriptMachineConfigMapping, Integer> shellScriptMappingDao = null;
	private static ShellScriptDBManager shellScriptDBManager = null;
	private PreparedQuery<ShellScriptBean> scriptForProjectQuery = null;

	public static ShellScriptDBManager getInstance(){
		if(shellScriptDBManager == null){
			shellScriptDBManager = new ShellScriptDBManager();
		}
		return shellScriptDBManager;
		
	}
	
	private ShellScriptDBManager(){
		shellScriptDao = (Dao<ShellScriptBean, Integer>) DAOProvider.getInstance().fetchDao(ShellScriptBean.class);
		shellScriptMappingDao = (Dao<ShellScriptMachineConfigMapping, Integer>) DAOProvider.getInstance().fetchDao(ShellScriptMachineConfigMapping.class);
		
	}
	
	/*@Override
	public StatusBean createDB(JsonNode newData) {
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
	}*/

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
		
	}*/

	@Override
	public StatusBean delete(String id) {
		StatusBean statusBean = new StatusBean();
		try {
			Utilities.getInstance().deleteScriptfileOnDisk(shellScriptDao.queryForId(Integer.parseInt(id)).getScriptName());
			shellScriptDao.deleteById(Integer.parseInt(id));
			List<ShellScriptMachineConfigMapping> shellscriptMappping = shellScriptMappingDao.queryForEq("script_id", Integer.parseInt(id));
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

	/*@Override
	public <T>T readDB(String id) {
		ShellScriptBean shellScript = null;
		try{
			shellScript = shellScriptDao.queryForId(Integer.parseInt(id));
		}catch(SQLException e){
			logger.error("Error querying the shell Script  from DB : " + e.getMessage());
		}
		return (T) shellScript;
	}*/

	@Override
	public StatusBean create(String mappedId, JsonNode newData) {
		ShellScriptBean shellScriptBean  = null;
		Gson shellScriptData = new GsonBuilder().setDateFormat("yyyy'-'MM'-'dd HH':'mm':'ss").create();
		shellScriptBean = shellScriptData.fromJson(newData.toString(), ShellScriptBean.class);
		StatusBean statusBean = new StatusBean();
		try {
			Utilities.getInstance().writeScriptToDisk(shellScriptBean);
			shellScriptDao.create(shellScriptBean);
			ProjectBean project = ProjectDBManager.getInstance().projectDao.queryForId(Integer.parseInt(mappedId));
			ShellScriptMachineConfigMapping shellscriptMapping = new ShellScriptMachineConfigMapping(null, shellScriptBean, project);
			shellScriptMappingDao.create(shellscriptMapping);
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
	public <E> List<E> read(String mappedId) {
		List<ShellScriptBean> projectList = new ArrayList<ShellScriptBean>();
		try {
		if (scriptForProjectQuery == null) {
			scriptForProjectQuery = makeShellScriptForProjectQuery();
		}
		
		scriptForProjectQuery.setArgumentHolderValue(0, ProjectDBManager.getInstance().projectDao.queryForId(Integer.parseInt(mappedId)));
		projectList = shellScriptDao.query(scriptForProjectQuery);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return (List<E>)projectList;
	}

	/*@Override
	public <T> T populateMappingBean(T mappingBean, String...ids) {
		// TODO Auto-generated method stub
		return null;
	}*/
	
	public StatusBean saveMachineMappingWithScript(String machineId, String scriptId, String projectId) {
		StatusBean statusBean =   new StatusBean();
		try {
		ProjectBean project = ProjectDBManager.getInstance().projectDao.queryForId(Integer.parseInt(projectId));
		ShellScriptBean shellScript =  shellScriptDao.queryForId(Integer.parseInt(scriptId));
		
		MachineConfigurationBean machineConfig = MachineConfigDBManager.getInstance().machineConfigDao.queryForId(Integer.parseInt(machineId));
		shellScriptMappingDao.updateBuilder().updateColumnValue(ShellScriptMachineConfigMapping.MACHINE_ID_FIELD_NAME, machineConfig).where().eq(ShellScriptMachineConfigMapping.PROJECT_ID_FIELD_NAME, project).and().eq(ShellScriptMachineConfigMapping.SCRIPT_ID_FIELD_NAME, shellScript);

		} catch (NumberFormatException e) {
			statusBean.setStatusCode(1);
			statusBean.setStatusMessage("Error saving machine MApping with script : "+e.getMessage());
			e.printStackTrace();
		} catch (SQLException e) {
			statusBean.setStatusCode(1);
			statusBean.setStatusMessage("Error saving machine MApping with script : "+e.getMessage());
			e.printStackTrace();
		}
		statusBean.setStatusCode(0);
		statusBean.setStatusMessage("Machine MApping with  Shell script saved successfully");
		
		return statusBean;
		
	}

private PreparedQuery<ShellScriptBean> makeShellScriptForProjectQuery() throws SQLException {
		
		QueryBuilder<ShellScriptMachineConfigMapping, Integer> scriptProjectQb = shellScriptMappingDao.queryBuilder();
		// just select the post-id field
		scriptProjectQb.selectColumns(ShellScriptMachineConfigMapping.SCRIPT_ID_FIELD_NAME);
		SelectArg projectSelectArg = new SelectArg();
		// you could also just pass in user1 here
		scriptProjectQb.where().eq(ShellScriptMachineConfigMapping.PROJECT_ID_FIELD_NAME, projectSelectArg);
	
		// build our outer query for Post objects
		QueryBuilder<ShellScriptBean, Integer> shellScriptQb = shellScriptDao.queryBuilder();
		// where the id matches in the post-id from the inner query
		shellScriptQb.where().in(ShellScriptBean.ID_FIELD_NAME, scriptProjectQb);
		return shellScriptQb.prepare();
	}
}
