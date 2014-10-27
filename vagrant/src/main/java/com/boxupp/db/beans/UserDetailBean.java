package com.boxupp.db.beans;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "user_detail")
public class UserDetailBean {

	@DatabaseField(canBeNull = false, generatedId=true, useGetSet = true)
	private Integer userId;
	
	@DatabaseField( useGetSet = true)
	private String firstName;
	
	@DatabaseField( useGetSet = true)
	private String lastName;
	
	@DatabaseField(useGetSet = true)
	private String mailId;
	
	@DatabaseField(useGetSet = true)
	private String password;
	
	@DatabaseField(useGetSet = true)
	private String userType;
	
	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public Boolean getIsDisabled() {
		return isDisabled;
	}

	public void setIsDisabled(Boolean isDisabled) {
		this.isDisabled = isDisabled;
	}
	public String getMailId() {
		return mailId;
	}

	public void setMailId(String mailId) {
		this.mailId = mailId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	@DatabaseField(useGetSet = true)
	private Boolean isDisabled;

}
