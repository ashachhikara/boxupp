/*******************************************************************************
 *  Copyright 2014 Paxcel Technologies
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *******************************************************************************/
package com.boxupp.resources;

import java.util.List;

<<<<<<< HEAD
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

=======
>>>>>>> 6f531bc934e3b2d6681b64c4509d519436275e91
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
<<<<<<< HEAD
import javax.ws.rs.core.Context;

=======
>>>>>>> 6f531bc934e3b2d6681b64c4509d519436275e91
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
<<<<<<< HEAD

	public UserAuthenticationResponse getLoginUserId(JsonNode loginCredentials,@Context HttpServletRequest request){
		return LoginDAOManager.getInstance().loginAuthorization(loginCredentials,request);
=======
	public UserAuthenticationResponse getLoginUserId(JsonNode loginCredentials){
		return LoginDAOManager.getInstance().loginAuthorization(loginCredentials);
>>>>>>> 6f531bc934e3b2d6681b64c4509d519436275e91
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
	
	@GET
	@Path("/idCheck/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public StatusBean checkExistUser(@PathParam("id") String userId){
		return UserDAOManager.getInstance().checkForExistingUser(userId);
	}
	
<<<<<<< HEAD
	@GET
	@Path("/signout")
	public void deleteSession(@Context HttpServletRequest request){
		SessionTracker.getInstance().destroySession(request);
	}

	@GET
	@Path("/checkSession")
	public Boolean checkSession(@Context HttpServletRequest request){
		return SessionTracker.getInstance().isSessionActive(request);
	}
	


=======
>>>>>>> 6f531bc934e3b2d6681b64c4509d519436275e91
}
