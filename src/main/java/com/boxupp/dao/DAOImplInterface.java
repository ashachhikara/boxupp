package com.boxupp.dao;

import org.codehaus.jackson.JsonNode;

import com.boxupp.responseBeans.StatusBean;

public interface DAOImplInterface {
	public StatusBean create(JsonNode data);
	
	public <T>T read(String id);
	
	public StatusBean update(JsonNode updatedData);
	
	public StatusBean delete(String id);
	
	/*public StatusBean createMappedDB(String MappedId, JsonNode newData);
	
	public <E> List<E> readAllDB();
	
	public <E> List<E> readMappedData(String MappedId);
	
	public <T>T populateMappingBean(T mappingBean, String...ids);*/
	
	
}
