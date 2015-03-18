/*******************************************************************************
 *  Copyright 2014 Paxcel Technologies
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *******************************************************************************/
package com.boxupp.db.beans;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "projectProviderMapping")
public class ProjectProviderMappingBean {
	
	public ProjectProviderMappingBean(Integer projectID, Integer providerID) {
		super();
		this.projectID = projectID;
		this.providerID = providerID;
	}
	public ProjectProviderMappingBean(){
		
	}
	@DatabaseField(canBeNull = false, generatedId = true, useGetSet = true)
	private Integer ID;
	
	@DatabaseField(canBeNull = false, useGetSet = true)
	private Integer projectID;
	
	@DatabaseField(canBeNull = false, useGetSet = true)
	private Integer providerID;

	public Integer getID() {
		return ID;
	}

	public void setID(Integer ID) {
		this.ID = ID;
	}
	public Integer getProjectID() {
		return projectID;
	}

	public void setProjectID(Integer projectID) {
		this.projectID = projectID;
	}
	public Integer getProviderID() {
		return providerID;
	}

	public void setProviderID(Integer providerID) {
		this.providerID = providerID;
	}

}
