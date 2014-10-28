package com.boxupp.resources;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.codehaus.jackson.JsonNode;

import com.boxupp.dao.MachineConfigDAOManager;
import com.boxupp.dao.ProjectDAOManager;
import com.boxupp.dao.PuppetModuleDAOManager;
import com.boxupp.dao.ShellScriptDAOManager;
import com.boxupp.db.beans.MachineConfigurationBean;
import com.boxupp.db.beans.ProjectBean;
import com.boxupp.db.beans.PuppetModuleBean;
import com.boxupp.db.beans.ShellScriptBean;
import com.boxupp.responseBeans.StatusBean;

@Path("/project/")
public class Project {
	
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
		return ProjectDAOManager.getInstance().createMappedDB(newProjectData);
	}
	
	
	@DELETE
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public StatusBean deleteProject(@PathParam("id") String projectId){
		return ProjectDAOManager.getInstance().delete(projectId);
	}
	
	@GET
	@Path("/getScripts/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<ShellScriptBean> getAllShellScriptsList(@PathParam("id") String projectId){
		return ShellScriptDAOManager.getInstance().retireveScriptsForProject(projectId);
	}
	@GET
	@Path("/getBoxes/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<MachineConfigurationBean> getAllMachinConfigsList(@PathParam("id") String projectId){
		return  MachineConfigDAOManager.getInstance().retireveBoxesForProject(projectId);
	}
	@GET
	@Path("/getModules/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<PuppetModuleBean> getAllPuppetModulesList(@PathParam("id") String projectId){
		return  PuppetModuleDAOManager.getInstance().retireveModulesForProject(projectId);
	}
	
	
}
