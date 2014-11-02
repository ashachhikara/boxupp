package com.boxupp.db.beans;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "provider")
public class ProviderBean {
	
	@DatabaseField(canBeNull = false, generatedId=true, useGetSet = true)
	private Integer providerID;

	@DatabaseField(useGetSet = true, unique = true)
	private String name;
	
	@DatabaseField(useGetSet = true)
	private boolean disabled;
	
	public Integer getProviderID() {
		return providerID;
	}

	public void setProviderID(Integer providerID) {
		this.providerID = providerID;
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
