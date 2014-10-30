package com.boxupp.resources;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.codehaus.jackson.JsonNode;

import com.boxupp.dao.LoginDAOManager;
import com.boxupp.dao.ProjectDAOManager;
import com.boxupp.dao.UserDAOManager;
import com.boxupp.db.beans.ProjectBean;
import com.boxupp.responseBeans.StatusBean;
import com.boxupp.responseBeans.UserAuthenticationResponse;

@Path("/user/")
public class User {
	
	@POST
	@Path("/login")
	@Produces(MediaType.APPLICATION_JSON)
	public UserAuthenticationResponse getLoginUserId(JsonNode loginCredentials){
		return LoginDAOManager.getInstance().loginAuthorization(loginCredentials);
	}
	
	@POST
	@Path("/signup")
	@Produces(MediaType.APPLICATION_JSON)
	public StatusBean creteNewUser(JsonNode newUserDetail){
		return UserDAOManager.getInstance().create(newUserDetail);
	}
	
	@GET
	@Path("/getProjects/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<ProjectBean> getAllProjectsList(@PathParam("id") String userId){
		return ProjectDAOManager.getInstance().retireveProjectsForUser(userId);
	}
}
