package com.boxupp.db.beans;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "gitRepoMapping")
public class GitRepoMapping {
	
	@DatabaseField(canBeNull = false, generatedId = true, useGetSet = true)
	private Integer id;
	
	@DatabaseField(canBeNull = false, useGetSet = true)
	private Integer userId;
	
	@DatabaseField(canBeNull = false, useGetSet = true)
	private Integer projectId;
	

}
