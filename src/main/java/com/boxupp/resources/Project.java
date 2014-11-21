package com.boxupp.resources;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.annotate.JsonIgnore;

import com.boxupp.ConfigurationGenerator;
import com.boxupp.FileManager;
import com.boxupp.dao.MachineConfigDAOManager;
import com.boxupp.dao.ProjectDAOManager;
import com.boxupp.dao.PuppetModuleDAOManager;
import com.boxupp.dao.ShellScriptDAOManager;
import com.boxupp.db.beans.MachineConfigurationBean;
import com.boxupp.db.beans.ProjectBean;
import com.boxupp.db.beans.PuppetModuleBean;
import com.boxupp.db.beans.PuppetModuleMapping;
import com.boxupp.db.beans.ShellScriptBean;
import com.boxupp.db.beans.ShellScriptMapping;
import com.boxupp.responseBeans.StatusBean;
import com.boxupp.responseBeans.VagrantFileStatus;
import com.boxupp.utilities.Utilities;

@Path("/project/")
public class Project {
	private static Logger logger = LogManager.getLogger(Project.class.getName());

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public ProjectBean retrieveProject(@PathParam("id") String projectId){
		return (ProjectBean) ProjectDAOManager.getInstance().read(projectId);
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes("application/json")
	public StatusBean createNewProject(JsonNode newProjectData){
		return ProjectDAOManager.getInstance().create(newProjectData);
	}
	
	
	@DELETE
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public StatusBean deleteProject(@PathParam("id") String projectID){
		return ProjectDAOManager.getInstance().delete(projectID);
	}
	
	@GET
	@Path("/getScripts/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<ShellScriptBean> getAllShellScriptsList(@PathParam("id") String projectID){
		return ShellScriptDAOManager.getInstance().retireveScriptsForProject(projectID);
	}
	@GET
	@Path("/getBoxes/{id}")
	@JsonIgnore
	@Produces(MediaType.APPLICATION_JSON)
	public List<MachineConfigurationBean> getAllMachinConfigsList(@PathParam("id") String projectID){
		return  MachineConfigDAOManager.getInstance().retireveBoxesForProject(projectID);
	}
	@GET
	@Path("/getAllModules/")
	@Produces(MediaType.APPLICATION_JSON)
	public List<PuppetModuleBean> getAllPuppetModulesList(){
		return  ProjectDAOManager.getInstance().retireveAllModules();
	}
	
	@GET
	@Path("/getModuleMapping/{id}")
	@Produces(MediaType.APPLICATION_JSON)

	public List<PuppetModuleMapping> getAllModuleMapping(@PathParam("id") String projectID) {
		return ProjectDAOManager.getInstance().retireveModulesMapping(projectID);
	}
	@GET
	@Path("/getScriptMappping/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<ShellScriptMapping> getAllScriptMapping(@PathParam("id") String projectID) {
		return ProjectDAOManager.getInstance().retireveScriptsMapping(projectID);
	}
	@POST
	@Path("/createVagrantFile")
	@Consumes(MediaType.APPLICATION_JSON)
	public VagrantFileStatus saveAsFile(JsonNode VagrantFileData) throws IOException
	{
		return Utilities.getInstance().saveVagrantFile(VagrantFileData);
	}
	
}
