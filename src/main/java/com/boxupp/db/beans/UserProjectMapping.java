package com.boxupp.db.beans;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName= "UserProjectMapping")
public class UserProjectMapping {
	public UserProjectMapping() {
		
	}

	public final static String USER_ID_FIELD_NAME = "user_id";
	public final static String PROJECT_ID_FIELD_NAME = "project_id";

	@DatabaseField(canBeNull = false, generatedId = true, useGetSet = true)
	private Integer id;

	@DatabaseField(foreign = true, columnName = USER_ID_FIELD_NAME)
	UserDetailBean user;

	@DatabaseField(foreign = true, columnName = PROJECT_ID_FIELD_NAME)
	ProjectBean project;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public ProjectBean getProject() {
		return project;
	}

	public void setProject(ProjectBean project) {
		this.project = project;
	}

	public UserDetailBean getUser() {
		return user;
	}

	public void setUser(UserDetailBean user) {
		this.user = user;
	}

	public UserProjectMapping(UserDetailBean user, ProjectBean project) {
		super();
		this.user = user;
		this.project = project;
	}
	
}
