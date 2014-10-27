package com.boxupp.db.beans;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName="defaultConfiguration")
public class DefaultConfigurationBean {
	
	@DatabaseField(canBeNull = false, generatedId= true, useGetSet = true)
	private Integer Id;
	
	@DatabaseField( useGetSet = true)
	private String boxType;
	
	@DatabaseField( useGetSet = true)
	private String boxUrl;
	
	@DatabaseField( useGetSet = true)
	private String networkIP;
	
	@DatabaseField( useGetSet = true)
	private String bootTimeout;

	@DatabaseField( useGetSet = true)
	private boolean guiMode;
	
	@DatabaseField(useGetSet = true)
	private String dockerImage;
	
	
	
	
	

}
