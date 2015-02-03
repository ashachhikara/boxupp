package com.boxupp.resources;

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

import org.codehaus.jackson.JsonNode;

import com.boxupp.dao.LocalGitRepoDAOManager;
import com.boxupp.responseBeans.StatusBean;
import com.boxupp.utilities.LocalRepoUtilities;

@Path("/localGitRepo/")
public class LocalGitRepo {

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes("application/json")
	public StatusBean saveLocalGitRepoData(JsonNode newData) {
		return LocalGitRepoDAOManager.getInstance().create(newData);
	}

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes("application/json")
	public StatusBean getLocalGitRepoData(@PathParam("id") String gitRepoID) {
		return LocalGitRepoDAOManager.getInstance().read(gitRepoID);
	}

	@DELETE
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public StatusBean deleteLocalGitRepoData(@PathParam("id") String localGitRepoID) {
		return LocalGitRepoDAOManager.getInstance().delete(localGitRepoID);
	}

	@GET
	@Path("/getBranches")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes("application/json")
	public List<String> getLocalGitRepos(@Context HttpServletRequest request) {
		return LocalRepoUtilities.getInstance().getLocalRepos(request);
	}
	
	@POST
	@Path("/commmit")
	@Produces(MediaType.APPLICATION_JSON)
	public StatusBean commitOnRemoteRepo(JsonNode param) {
		return LocalRepoUtilities.getInstance().commitOnRemoteRepo(param);
	}
}
