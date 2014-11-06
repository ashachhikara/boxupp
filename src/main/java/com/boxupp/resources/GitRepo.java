package com.boxupp.resources;

import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.codehaus.jackson.JsonNode;

import com.boxupp.dao.GitRepoDAOManager;
import com.boxupp.responseBeans.StatusBean;

public class GitRepo {
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public StatusBean saveGitRepo(JsonNode newData) {
		return GitRepoDAOManager.getInstance().create(newData);
	}
	

}
