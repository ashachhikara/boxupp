package com.paxcel.boxupp.beans;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.*;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Config {
	
	@XmlElement(name = "provider")
	private ArrayList<Provider> ProviderList;
	
	@XmlElement(name = "setting")
	private Setting setting;

	public ArrayList<Provider> getProviderList() {
		return ProviderList;
	}

	public void setProviderList(ArrayList<Provider> providerList) {
		ProviderList = providerList;
	}

	public Setting getSetting() {
		return setting;
	}

	public void setSetting(Setting setting) {
		this.setting = setting;
	}

}