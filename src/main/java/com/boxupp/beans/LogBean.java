package com.boxupp.beans;

import java.sql.Time;
import java.util.Date;

public class LogBean {
	
	private Integer userID;
	private Integer projectID;
	private String vagrantID;
	private String status;
	private Date time;
	private String filename;
	
	public Integer getUserID() {
		return userID;
	}
	public void setUserID(Integer userID) {
		this.userID = userID;
	}
	public Integer getProjectID() {
		return projectID;
	}
	public void setProjectID(Integer projectID) {
		this.projectID = projectID;
	}
	public String getVagrantID() {
		return vagrantID;
	}
	public void setVagrantID(String vagrantID) {
		this.vagrantID = vagrantID;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	

}
