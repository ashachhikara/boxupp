package com.paxcel.boxupp.db.beans.manager;

import java.util.List;

import org.codehaus.jackson.JsonNode;

import com.paxcel.responseBeans.StatusBean;

public interface DBBeansManager {
	
	public StatusBean create(String MappedId, JsonNode newData);
	
	public StatusBean update(JsonNode updatedData);
	
	public StatusBean delete(String id);
	
	public <E> List<E> read(String MappedId);
}
