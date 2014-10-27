package com.boxupp.db.beans;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "projectProviderMapping")
public class ProjectProviderMappingBean {
	
	@DatabaseField(canBeNull = false, generatedId = true, useGetSet = true)
	private Integer id;
	
	@DatabaseField(canBeNull = false, useGetSet = true)
	private Integer ProjectId;
	
	@DatabaseField(canBeNull = false, useGetSet = true)
	private Integer providerId;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getProjectId() {
		return ProjectId;
	}

	public void setProjectId(Integer projectId) {
		ProjectId = projectId;
	}

	public Integer getProviderId() {
		return providerId;
	}

	public void setProviderId(Integer providerId) {
		this.providerId = providerId;
	}

}
