package com.boxupp.db.beans;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "gitRepoMapping")
public class GitRepoMapping {
	public final static String PROJECT_ID_FIELD_NAME = "projectID";

	@DatabaseField(canBeNull = false, generatedId = true, useGetSet = true)
	private Integer ID;
	
	@DatabaseField(foreign = true, useGetSet = true, columnName =PROJECT_ID_FIELD_NAME, foreignAutoRefresh=true, foreignAutoCreate=true)
	private ProjectBean project;
	
	public Integer getID() {
		return ID;
	}

	public void setID(Integer ID) {
		this.ID = ID;
	}

	public ProjectBean getProject() {
		return project;
	}

	public void setProject(ProjectBean project) {
		this.project = project;
	}
}
