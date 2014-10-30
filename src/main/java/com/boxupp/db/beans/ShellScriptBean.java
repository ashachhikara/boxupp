package com.boxupp.db.beans;

import java.sql.Date;

import com.j256.ormlite.field.DatabaseField;

public class ShellScriptBean {
	public static final String ID_FIELD_NAME = "scriptID";

	@DatabaseField(canBeNull = false, generatedId=true, useGetSet = true, columnName=ID_FIELD_NAME)
	private Integer scriptID;
	
	@DatabaseField(useGetSet = true)
	private String scriptName;
	
	@DatabaseField(useGetSet = true)
	private Boolean isDisabled;
	
	@DatabaseField(useGetSet = true)
	private String scriptContent;
	
	@DatabaseField(useGetSet = true)
	private Integer creatorUserID;
	
	@DatabaseField(useGetSet = true, format = "yyyy-MM-dd HH:mm:ss")
	private Date creationTime;
	
	@DatabaseField(useGetSet = true)
	private String description;

	public Integer getScriptID() {
		return scriptID;
	}

	public void setScriptID(Integer scriptID) {
		this.scriptID = scriptID;
	}

	public String getScriptName() {
		return scriptName;
	}

	public void setScriptName(String scriptName) {
		this.scriptName = scriptName;
	}

	public Boolean getIsDisabled() {
		return isDisabled;
	}

	public void setIsDisabled(Boolean isDisabled) {
		this.isDisabled = isDisabled;
	}

	public String getScriptContent() {
		return scriptContent;
	}

	public void setScriptContent(String scriptContent) {
		this.scriptContent = scriptContent;
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
