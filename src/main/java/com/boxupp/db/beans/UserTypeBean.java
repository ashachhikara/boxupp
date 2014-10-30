package com.boxupp.db.beans;

import java.sql.Date;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
@DatabaseTable(tableName = "userType")
public class UserTypeBean {
	
	@DatabaseField(canBeNull = false, generatedId=true, useGetSet = true)
	private Integer ID;
	
	@DatabaseField( useGetSet = true)
	private Integer userTypeCode;
	
	@DatabaseField(useGetSet = true)
	private String userTypeDesc;
	
	@DatabaseField(useGetSet = true)
	private Boolean isDisabled;
	
	@DatabaseField(useGetSet = true, format = "yyyy-MM-dd HH:mm:ss")
	private Date creationTime;
	
	@DatabaseField(useGetSet = true)
	private Integer adminID;

	public Integer getID() {
		return ID;
	}

	public void setID(Integer ID) {
		this.ID = ID;
	}

	public Integer getUserTypeCode() {
		return userTypeCode;
	}

	public void setUserTypeCode(Integer userTypeCode) {
		this.userTypeCode = userTypeCode;
	}

	public String getUserTypeDesc() {
		return userTypeDesc;
	}

	public void setUserTypeDesc(String userTypeDesc) {
		this.userTypeDesc = userTypeDesc;
	}

	public Boolean getIsDisabled() {
		return isDisabled;
	}

	public void setIsDisabled(Boolean isDisabled) {
		this.isDisabled = isDisabled;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public Integer getAdminID() {
		return adminID;
	}

	public void setAdminID(Integer adminID) {
		this.adminID = adminID;
	}
	
}
