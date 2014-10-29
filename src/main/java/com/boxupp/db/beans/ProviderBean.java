package com.boxupp.db.beans;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "provider")
public class ProviderBean {
	
	@DatabaseField(canBeNull = false, generatedId=true, useGetSet = true)
	private Integer providerId;
	

	@DatabaseField(useGetSet = true, unique = true)
	private String name;
	
	@DatabaseField(useGetSet = true)
	private boolean disabled;
	
	public Integer getProviderId() {
		return providerId;
	}

	public void setProviderId(Integer providerId) {
		this.providerId = providerId;
	}

	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean getDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}
	
	
}
