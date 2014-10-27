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
import com.boxupp.db.beans.ShellScriptBean;
import com.boxupp.db.beans.ShellScriptMapping;
import com.boxupp.responseBeans.StatusBean;
import com.boxupp.utilities.Utilities;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.SelectArg;

public class ShellScriptDAOManager implements DAOImplInterface {
	private static Logger logger = LogManager.getLogger(ShellScriptDAOManager.class.getName());
	protected static Dao<ShellScriptBean, Integer> shellScriptDao = null;
	public static Dao<ShellScriptMapping, Integer> shellScriptMappingDao = null;
	private static ShellScriptDAOManager shellScriptDAOManager = null;
	private PreparedQuery<ShellScriptBean> scriptForProjectQuery = null;
	
	public static ShellScriptDAOManager getInstance(){
		if(shellScriptDAOManager == null){
			shellScriptDAOManager = new ShellScriptDAOManager();
		}
		return shellScriptDAOManager;
		
	}
	
	private ShellScriptDAOManager(){
		shellScriptDao = (Dao<ShellScriptBean, Integer>) DAOProvider.getInstance().fetchDao(ShellScriptBean.class);
		shellScriptMappingDao = (Dao<ShellScriptMapping, Integer>) DAOProvider.getInstance().fetchDao(ShellScriptMapping.class);
		
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
			ProjectBean project = ProjectDAOManager.getInstance().projectDao.queryForId(Integer.parseInt(newData.get("ProjectId").getTextValue()));
			ShellScriptMapping shellscriptMapping = new ShellScriptMapping(null, shellScriptBean, project);
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
	
	public StatusBean delete(String id) {
		StatusBean statusBean = new StatusBean();
		try {
			Utilities.getInstance().deleteScriptfileOnDisk(shellScriptDao.queryForId(Integer.parseInt(id)).getScriptName());
			shellScriptDao.deleteById(Integer.parseInt(id));
			List<ShellScriptMapping> shellscriptMappping = shellScriptMappingDao.queryForEq("script_id", Integer.parseInt(id));
				for(ShellScriptMapping shellScript : shellscriptMappping){
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
	public <E> List<E> read(String projectId) {
		List<ShellScriptBean> shellScriptList = new ArrayList<ShellScriptBean>();
		try {
		if (scriptForProjectQuery == null) {
			scriptForProjectQuery = makeShellScriptForProjectQuery();
		}
		
		scriptForProjectQuery.setArgumentHolderValue(0, ProjectDAOManager.getInstance().projectDao.queryForId(Integer.parseInt(projectId)));
		shellScriptList = shellScriptDao.query(scriptForProjectQuery);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return (List<E>)shellScriptList;
	}

	public StatusBean saveMachineMappingWithScript(String machineId, String scriptId, String projectId) {
		StatusBean statusBean =   new StatusBean();
		try {
		ProjectBean project = ProjectDAOManager.getInstance().projectDao.queryForId(Integer.parseInt(projectId));
		ShellScriptBean shellScript =  shellScriptDao.queryForId(Integer.parseInt(scriptId));
		
		MachineConfigurationBean machineConfig = MachineConfigDAOManager.getInstance().machineConfigDao.queryForId(Integer.parseInt(machineId));
		shellScriptMappingDao.updateBuilder().updateColumnValue(ShellScriptMapping.MACHINE_ID_FIELD_NAME, machineConfig).where().eq(ShellScriptMapping.PROJECT_ID_FIELD_NAME, project).and().eq(ShellScriptMapping.SCRIPT_ID_FIELD_NAME, shellScript);

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
		
		QueryBuilder<ShellScriptMapping, Integer> scriptProjectQb = shellScriptMappingDao.queryBuilder();
		// just select the post-id field
		scriptProjectQb.selectColumns(ShellScriptMapping.SCRIPT_ID_FIELD_NAME);
		SelectArg projectSelectArg = new SelectArg();
		// you could also just pass in user1 here
		scriptProjectQb.where().eq(ShellScriptMapping.PROJECT_ID_FIELD_NAME, projectSelectArg);
	
		// build our outer query for Post objects
		QueryBuilder<ShellScriptBean, Integer> shellScriptQb = shellScriptDao.queryBuilder();
		// where the id matches in the post-id from the inner query
		shellScriptQb.where().in(ShellScriptBean.ID_FIELD_NAME, scriptProjectQb);
		return shellScriptQb.prepare();
	}
}




