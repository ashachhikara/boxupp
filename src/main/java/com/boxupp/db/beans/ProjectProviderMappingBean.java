package com.boxupp.db.beans;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "projectProviderMapping")
public class ProjectProviderMappingBean {
	

	@DatabaseField(canBeNull = false, generatedId = true, useGetSet = true)
	private Integer ID;
	
	@DatabaseField(canBeNull = false, useGetSet = true)
	private Integer projectID;
	
	@DatabaseField(canBeNull = false, useGetSet = true)
	private Integer providerID;

	public Integer getID() {
		return ID;
	}

	public void setID(Integer ID) {
		this.ID = ID;
	}
	public Integer getProjectID() {
		return projectID;
	}

	public void setProjectID(Integer projectID) {
		this.projectID = projectID;
	}
	public Integer getProviderID() {
		return providerID;
	}

	public void setProviderID(Integer providerID) {
		this.providerID = providerID;
	}

}
