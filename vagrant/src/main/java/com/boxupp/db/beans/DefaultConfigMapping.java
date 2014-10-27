package com.boxupp.db.beans;

import com.j256.ormlite.field.DatabaseField;

public class DefaultConfigMapping {
	
	@DatabaseField(canBeNull = false, generatedId= true, useGetSet = true)
	private Integer Id;
	
	@DatabaseField(canBeNull = false, useGetSet = true)
	private String projectId;
	
	@DatabaseField( canBeNull = false, useGetSet = true)
	private Integer defaultConfigId;

}
