package com.boxupp.db.beans;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "local_git_repo_details")
public class LocalGitRepoBean {
	@DatabaseField(canBeNull = false, generatedId = true, useGetSet = true)
	private Integer localGitRepoID;
	
	@DatabaseField(useGetSet = true)
	private String localGitURI;
	
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

	public Integer getLocalGitRepoID() {
		return localGitRepoID;
	}

	public void setGitRepoID(Integer localGitRepoID) {
		this.localGitRepoID = localGitRepoID;
	}

	public String getLocalGitURI() {
		return localGitURI;
	}

	public void setLocalGitURI(String localGitURI) {
		this.localGitURI = localGitURI;
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
