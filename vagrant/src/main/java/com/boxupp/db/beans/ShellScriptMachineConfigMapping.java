package com.boxupp.db.beans;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "shellScriptMapping")
public class ShellScriptMachineConfigMapping {
	public final static String SCRIPT_ID_FIELD_NAME = "script_id";
	public final static String PROJECT_ID_FIELD_NAME = "project_id";
	public final static String MACHINE_ID_FIELD_NAME = "machine_id";
	public ShellScriptMachineConfigMapping(	MachineConfigurationBean machineConfig, ShellScriptBean script,
			ProjectBean project) {
		super();
		this.machineConfig = machineConfig;
		this.script = script;
		this.project = project;
	}

	@DatabaseField(canBeNull = false, generatedId = true, useGetSet = true)
	private Integer id;

	@DatabaseField(foreign = true, columnName ="machinConfig_id")
	private MachineConfigurationBean machineConfig;
	
	@DatabaseField(foreign = true, columnName ="script_id")
	private ShellScriptBean script ;
	
	@DatabaseField(foreign = true, columnName ="project_id")
	private ProjectBean project;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	
}
