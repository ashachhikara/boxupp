package com.boxupp.db.beans;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "user_detail")
public class UserDetailBean {

	@DatabaseField(canBeNull = false, generatedId=true, useGetSet = true)
	private Integer userID;
	
	@DatabaseField( useGetSet = true)
	private String firstName;
	
	@DatabaseField( useGetSet = true)
	private String lastName;
	
	@DatabaseField(useGetSet = true, unique = true )
	private String mailID;
	
	@DatabaseField(useGetSet = true)
	private String password;
	
	@DatabaseField(useGetSet = true)
	private Integer userType;
	
	public Integer getUserID() {
		return userID;
	}

	public void setUserID(Integer userID) {
		this.userID = userID;
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

	public Integer getUserType() {
		return userType;
	}

	public void setUserType(Integer userType) {
		this.userType = userType;
	}

	public Boolean getIsDisabled() {
		return isDisabled;
	}

	public void setIsDisabled(Boolean isDisabled) {
		this.isDisabled = isDisabled;
	}
	public String getMailID() {
		return mailID;
	}

	public void setMailID(String mailID) {
		this.mailID = mailID;
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
