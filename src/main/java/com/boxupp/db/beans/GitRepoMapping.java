package com.boxupp.db.beans;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "gitRepoMapping")
public class GitRepoMapping {
	@DatabaseField(canBeNull = false, generatedId = true, useGetSet = true)
	private Integer ID;
	
	@DatabaseField(canBeNull = false, useGetSet = true)
	private Integer userID;
	
	@DatabaseField(canBeNull = false, useGetSet = true)
	private Integer projectID;
	public Integer getID() {
		return ID;
	}

	public void setID(Integer ID) {
		this.ID = ID;
	}

	public Integer getUserID() {
		return userID;
	}

	public void setUserID(Integer userID) {
		this.userID = userID;
	}

	public Integer getProjectID() {
		return projectID;
	}

	public void setProjectID(Integer projectID) {
		this.projectID = projectID;
	}

	
	

}
