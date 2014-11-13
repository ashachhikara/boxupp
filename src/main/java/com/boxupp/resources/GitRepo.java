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

import com.boxupp.dao.GitRepoDAOManager;
import com.boxupp.responseBeans.StatusBean;

@Path("/gitRepo/")
public class GitRepo {
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes("application/json")
	public StatusBean saveGitRepo(JsonNode newData) {
		return GitRepoDAOManager.getInstance().create(newData);
	}
	
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes("application/json")
	public StatusBean getGitRepo(@PathParam("id") String gitRepoID) {
		return GitRepoDAOManager.getInstance().read(gitRepoID);
	}
	
	@DELETE
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public StatusBean deleteGitRepo(@PathParam("id") String gitRepoID){
		return GitRepoDAOManager.getInstance().delete(gitRepoID);
	}
	
}
