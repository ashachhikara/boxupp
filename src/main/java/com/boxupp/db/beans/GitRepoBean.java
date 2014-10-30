package com.boxupp.db.beans;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "git_repo_details")
public class GitRepoBean {
	
	@DatabaseField(canBeNull = false, generatedId = true, useGetSet = true)
	private Integer gitRepoID;
	
	@DatabaseField(useGetSet = true)
	private String userName;
	
	@DatabaseField(useGetSet = true)
	private  String Password;

	@DatabaseField(useGetSet = true)
	private String repoName;

	@DatabaseField(useGetSet = true)
	private String branch;

	@DatabaseField(useGetSet = true)
	private String path;

	@DatabaseField(useGetSet = true)
	private String comment;

	public Integer getGitRepoID() {
		return gitRepoID;
	}

	public void setGitRepoID(Integer gitRepoID) {
		this.gitRepoID = gitRepoID;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return Password;
	}

	public void setPassword(String password) {
		Password = password;
	}

	public String getRepoName() {
		return repoName;
	}

	public void setRepoName(String repoName) {
		this.repoName = repoName;
	}

	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
}
