package com.boxupp.db.beans;

import java.sql.Date;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "project")
public class ProjectBean  {
	
	public static final String ID_FIELD_NAME = "projectId";

	@DatabaseField(canBeNull = false, generatedId=true, useGetSet = true, columnName=ID_FIELD_NAME)
	private Integer projectId;
	
	@DatabaseField(useGetSet = true)
	private String name;
	
	@DatabaseField(useGetSet = true)
	private String owner;
	
	@DatabaseField(useGetSet = true, format="yyyy-MM-dd HH:mm:ss")
	private Date creationTime;
	
	@DatabaseField(useGetSet = true)
	private String description;
	
	@DatabaseField(useGetSet = true)
	private Integer providerType;
	
	@DatabaseField(useGetSet = true)
	private boolean disabled;

	
	public Integer getProjectId() {
		return projectId;
	}
	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public Date getCreationTime() {
		return creationTime;
	}
	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getProviderType() {
		return providerType;
	}
	public void setProviderType(Integer providerType) {
		this.providerType = providerType;
	}
	public boolean getDisabled() {
		return disabled;
	}
	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}
	
}
