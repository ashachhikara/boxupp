package com.boxupp.ws.resources;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.JsonNode;

import com.boxupp.db.beans.ProjectBean;
import com.boxupp.db.beans.ProviderBean;
import com.boxupp.db.beans.PuppetModuleBean;
import com.paxcel.boxupp.db.beans.manager.LoginDBManager;
import com.paxcel.boxupp.db.beans.manager.MachineConfigDBManager;
import com.paxcel.boxupp.db.beans.manager.ProjectDBManager;
import com.paxcel.boxupp.db.beans.manager.ProviderDBManager;
import com.paxcel.boxupp.db.beans.manager.PuppetModuleDBManager;
import com.paxcel.boxupp.db.beans.manager.ShellScriptDBManager;
import com.paxcel.boxupp.db.beans.manager.UserDBManager;
import com.paxcel.responseBeans.StatusBean;

@Path("/resources/")
public class Resources {
	
	private static Logger logger = LogManager.getLogger(Resources.class.getName());
	
	/* USER  API's*/
	
	@POST
	@Path("/login")
	@Produces(MediaType.APPLICATION_JSON)
	public StatusBean getLoginUserId(JsonNode loginCredentials){
		return LoginDBManager.getInstance().loginAuthorization(loginCredentials);
	}
	
	@POST
	@Path("/signUp")
	@Produces(MediaType.APPLICATION_JSON)
	public StatusBean creteNewUser(JsonNode newUserDetail){
		
		return UserDBManager.getInstance().create(newUserDetail);
	}
	/* Project API's */
	
	@POST
	@Path("/projects")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes("application/json")
	public StatusBean createNewProject(String userId, JsonNode newProjectData){

		return ProjectDBManager.getInstance().create("1", newProjectData);
	}
	
	@GET
	@Path("/projects/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<ProjectBean> getAllProjectsList(@PathParam("id") String userId){
		
		return ProjectDBManager.getInstance().read(userId);
	}
	
	@DELETE
	@Path("/projects/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public StatusBean deleteProject(@PathParam("id") String projectId){
		
		return ProjectDBManager.getInstance().delete(projectId);
	}

	/* Provider API's */
	
	@GET
	@Path("/providers")
	@Produces(MediaType.APPLICATION_JSON)
	public List<ProviderBean> getAllProviders(){
		
		return ProviderDBManager.getInstance().read();
	}
	
	@GET
	@Path("/providers/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public ProviderBean getProviderData(@PathParam("id") String providerID){
		return ProviderDBManager.getInstance().readProvider(providerID);
	}
	
	/* Machine Configuration API's */
	
	@POST 
	@Path("/machineConfig/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes("application/json")
	public StatusBean saveMachineConfiguration(@PathParam("id") String id,JsonNode mappings) {
		return MachineConfigDBManager.getInstance().create(id , mappings);
	}
	
	@GET
	@Path("/machineConfig/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<PuppetModuleBean> getMachineConfig(@PathParam("id") String projectId) {
		return MachineConfigDBManager.getInstance().read(projectId);
	}
	@POST 
	@Path("/updateMachineConfig")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes("application/json")
	public StatusBean updateMachineConfiguration( JsonNode updatedmachineConfigData) {
		return MachineConfigDBManager.getInstance().update(updatedmachineConfigData);
	}
	
	@DELETE
	@Path("/machineConfig/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public StatusBean deleteMachineConfiguration(@PathParam("id") String machineID) {
		return MachineConfigDBManager.getInstance().delete(machineID);
	}
	
	/* Shell Script API's */
	
	@POST 
	@Path("/shellScript/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes("application/json")
	public StatusBean saveShellScript(@PathParam("id") String id, JsonNode newShellScriptData) {
		return ShellScriptDBManager.getInstance().create(id, newShellScriptData);
	}
	@GET
	@Path("/shellScript/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<PuppetModuleBean> getShellScripts(@PathParam("id") String projectId) {
		return ShellScriptDBManager.getInstance().read(projectId);
	}
	
	@GET 
	@Path("/updateShellScript")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes("application/json")
	public StatusBean updateShellScript(JsonNode updatedShellScriptData) {
		
		return ShellScriptDBManager.getInstance().update(updatedShellScriptData);
	}
	
	@DELETE
	@Path("/deleteShellScript/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public StatusBean deleteShellScript(@PathParam("id") String shellScriptId) {
		return ShellScriptDBManager.getInstance().delete(shellScriptId);
	}
	
	/* Puppet Module API's */
	
	@POST 
	@Path("/puppetModule/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes("application/json")
	public StatusBean savePuppetModule(@PathParam("id") String id, JsonNode newPuppetModuleData) {
		return PuppetModuleDBManager.getInstance().create(id, newPuppetModuleData);
	}
	
	@GET
	@Path("/puppetModule/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<PuppetModuleBean> getPuppetModules(@PathParam("id") String projectId) {
		return PuppetModuleDBManager.getInstance().read(projectId);
	}
	
	@POST 
	@Path("/updatePuppetModule")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes("application/json")
	public StatusBean updatePuppetModule(JsonNode updatedPuppetModuleData) {
		return PuppetModuleDBManager.getInstance().update(updatedPuppetModuleData);
	}
	
	@DELETE 
	@Path("/puppetModule/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public StatusBean deletePuppetModule(@PathParam("id") String puppetModuleId) {
		return PuppetModuleDBManager.getInstance().delete(puppetModuleId);
	}
	
}
