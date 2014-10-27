package com.boxupp.db.beans;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "machineProjectMapping")
public class MachineProjectMapping {
	public final static String MACHINE_ID_FIELD_NAME = "machine_id";
	public final static String PROJECT_ID_FIELD_NAME = "project_id";
	public MachineProjectMapping( ProjectBean project,
			MachineConfigurationBean machineConfig) {
		super();
		this.project = project;
		this.machineConfig = machineConfig;
	}
	
	public MachineProjectMapping(){
		
	}
	
	@DatabaseField(canBeNull = false, generatedId = true, useGetSet = true)
	private Integer id;

	@DatabaseField(foreign = true, useGetSet = true, columnName =PROJECT_ID_FIELD_NAME)
	ProjectBean project;

	@DatabaseField(foreign = true, useGetSet = true, columnName = MACHINE_ID_FIELD_NAME)
	MachineConfigurationBean machineConfig;

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public ProjectBean getProject() {
		return project;
	}
	public void setProject(ProjectBean project) {
		this.project = project;
	}
	public MachineConfigurationBean getMachineConfig() {
		return machineConfig;
	}
	public void setMachineConfig(MachineConfigurationBean machineConfig) {
		this.machineConfig = machineConfig;
	}
	
}
