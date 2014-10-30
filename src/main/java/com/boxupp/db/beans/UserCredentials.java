package com.boxupp.db.beans;

import com.j256.ormlite.field.DatabaseField;

public class UserCredentials {

	@DatabaseField( useGetSet = true, canBeNull = true)
	private String loginID;
	
	@DatabaseField(useGetSet = true, canBeNull = true)
	private String password;

	public String getLoginID() {
		return loginID;
	}

	public void setLoginID(String loginID) {
		this.loginID = loginID;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
