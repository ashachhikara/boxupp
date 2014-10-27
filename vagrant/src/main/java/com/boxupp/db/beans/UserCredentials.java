package com.boxupp.db.beans;

import com.j256.ormlite.field.DatabaseField;

public class UserCredentials {

	@DatabaseField( useGetSet = true, canBeNull = true)
	private String loginId;
	
	@DatabaseField(useGetSet = true, canBeNull = true)
	private String password;

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
