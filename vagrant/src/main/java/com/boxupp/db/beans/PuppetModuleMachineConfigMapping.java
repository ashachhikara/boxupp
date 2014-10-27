package com.boxupp.db.beans;

import com.j256.ormlite.table.DatabaseTable;
import com.j256.ormlite.field.DatabaseField;

@DatabaseTable(tableName = "puppetModuleMapping")
public class PuppetModuleMachineConfigMapping {
	public final static String MODULE_ID_FIELD_NAME = "module_id";
	public final static String PROJECT_ID_FIELD_NAME = "project_id";
	public final static String MACHINE_ID_FIELD_NAME = "machine_id";
	public PuppetModuleMachineConfigMapping(
			MachineConfigurationBean machineConfig, ProjectBean project,
			PuppetModuleBean puppetModule) {
		super();
		this.machineConfig = machineConfig;
		this.project = project;
		this.puppetModule = puppetModule;
	}

	@DatabaseField(canBeNull = false, generatedId = true, useGetSet = true)
	private Integer id;

	@DatabaseField(foreign = true, useGetSet= true, columnName = MACHINE_ID_FIELD_NAME)
	MachineConfigurationBean machineConfig;

	@DatabaseField(foreign = true, useGetSet= true, columnName = PROJECT_ID_FIELD_NAME)
	ProjectBean project;

	@DatabaseField(foreign = true, useGetSet= true, columnName = MODULE_ID_FIELD_NAME)
	PuppetModuleBean puppetModule;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public MachineConfigurationBean getMachineConfig() {
		return machineConfig;
	}

	public void setMachineConfig(MachineConfigurationBean machineConfig) {
		this.machineConfig = machineConfig;
	}

	public ProjectBean getProject() {
		return project;
	}

	public void setProject(ProjectBean project) {
		this.project = project;
	}

}
