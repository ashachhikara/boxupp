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

import com.boxupp.dao.ProjectDAOManager;
import com.boxupp.db.beans.PuppetModuleBean;
import com.boxupp.responseBeans.StatusBean;

public class PuppetModule {
	@POST 
	@Path("/puppetModule")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes("application/json")
	public StatusBean savePuppetModule(JsonNode newPuppetModuleData) {
		return ProjectDAOManager.getInstance().create(newPuppetModuleData);
	}
	
	@GET
	@Path("/puppetModule/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<PuppetModuleBean> getPuppetModules(@PathParam("id") String projectId) {
		return ProjectDAOManager.getInstance().read(projectId);
	}
	
	@POST 
	@Path("/updatePuppetModule")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes("application/json")
	public StatusBean updatePuppetModule(JsonNode updatedPuppetModuleData) {
		return ProjectDAOManager.getInstance().update(updatedPuppetModuleData);
	}
	
	@DELETE 
	@Path("/puppetModule/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public StatusBean deletePuppetModule(@PathParam("id") String puppetModuleId) {
		return ProjectDAOManager.getInstance().delete(puppetModuleId);
	}
	
}
