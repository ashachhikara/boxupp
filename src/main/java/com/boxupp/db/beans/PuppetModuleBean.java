package com.boxupp.db.beans;

import java.sql.Date;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "puppetModule")
public class PuppetModuleBean {
	public static final String ID_FIELD_NAME = "puppetID";

	@DatabaseField(canBeNull = false, generatedId = true, useGetSet = true, columnName=ID_FIELD_NAME)
	private Integer puppetID;
	
	
	@DatabaseField(useGetSet = true)
	private String moduleName;
	
	@DatabaseField(useGetSet = true)
	private Boolean isDisabled;
	
	@DatabaseField(useGetSet = true)
	private Integer creatorUserID;
	
	@DatabaseField(useGetSet = true, format="yyyy-MM-dd HH:mm:ss")
	private Date creationTime;
	
	@DatabaseField(useGetSet = true)
	private String description;
	
	public Integer getPuppetID() {
		return puppetID;
	}

	public void setPuppetID(Integer puppetID) {
		this.puppetID = puppetID;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public Boolean getIsDisabled() {
		return isDisabled;
	}

	public void setIsDisabled(Boolean isDisabled) {
		this.isDisabled = isDisabled;
	}

	public Integer getCreatorUserID() {
		return creatorUserID;
	}

	public void setCreatorUserID(Integer creatorUserID) {
		this.creatorUserID = creatorUserID;
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


}
