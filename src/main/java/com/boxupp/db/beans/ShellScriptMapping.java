package com.boxupp.db.beans;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "shellScriptMapping")
public class ShellScriptMapping {
	public final static String SCRIPT_ID_FIELD_NAME = "scriptID";
	public final static String PROJECT_ID_FIELD_NAME = "projectID";
	public final static String MACHINE_ID_FIELD_NAME = "machineID";
	public ShellScriptMapping(	MachineConfigurationBean machineConfig, ShellScriptBean script,
			ProjectBean project) {
		super();
		this.machineConfig = machineConfig;
		this.script = script;
		this.project = project;
	}
	
	public ShellScriptMapping(){
		
	}

	@DatabaseField(canBeNull = false, generatedId = true, useGetSet = true)
	private Integer ID;

	@DatabaseField(foreign = true, columnName ="machineID")
	private MachineConfigurationBean machineConfig;
	
	@DatabaseField(foreign = true, columnName ="scriptID")
	private ShellScriptBean script ;
	
	@DatabaseField(foreign = true, columnName ="projectID")
	private ProjectBean project;
	
	public Integer getID() {
		return ID;
	}

	public void setID(Integer ID) {
		this.ID = ID;
	}
	
	
}
