package com.boxupp.db.beans;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import org.eclipse.jetty.util.security.Password;

@DatabaseTable(tableName = "gitRebo")
public class GitRepoBean {
	
	@DatabaseField(canBeNull = false, generatedId = true, useGetSet = true)
	private Integer id;
	
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
	private String comment;;

}
