package com.boxupp.resources;

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
import com.boxupp.db.beans.ProjectBean;
import com.boxupp.responseBeans.StatusBean;

@Path("/project/")
public class Project {
	
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public ProjectBean retrieveProject(@PathParam("id") String projectID){
		return (ProjectBean) ProjectDAOManager.getInstance().read(projectID);
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
	
	
}
