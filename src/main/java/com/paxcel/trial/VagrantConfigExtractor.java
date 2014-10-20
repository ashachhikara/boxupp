package com.paxcel.trial;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.paxcel.boxupp.beans.PortForwardingMapping;
import com.paxcel.boxupp.beans.SyncFolderMapping;

public class VagrantConfigExtractor {
	
	private static VagrantConfigExtractor configExtractor = null;
	
	private static final String IPADDRESS_PATTERN = "(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)";
	private static final String PORT_MAPPING_PATTERN = "guest[: ]*[\\d]{1,5},[ ]*host[: ]*[\\d]{1,5}";
//	private static final String PROVIDER_PATTERN = ".vm.provider[ ]*\"[a-z]*\"";
	private static final String QUOTES_PATTERN = "\\\"[a-zA-Z]*\\\"";
	private static final String RAM_PATTERN = "\\\"--memory\\\"[ ]*,[ ]*\\\"[ ]*[0-9]*[ ]*\\\"";
	private static final String CPU_PATTERN = "\\\"--cpuexecutioncap\\\"[ ]*,[ ]*\\\"[ ]*[0-9]*[ ]*\\\"";
	private static final String GUI_MODE_PATTERN = ".gui[ ]*=[ ]*true|false";
	private static final String PROVISIONER_NAME_PATTERN = "[a-zA-Z]*.name[ ]*=[ ]*\\\"[a-zA-Z]*\\\"";
	private static final String FOLDER_MAPPING = "\\\"[a-zA-Z]*\\\"[ ]*,[ ]*\\\"[a-zA-Z./\\\\]*\\\"";
	
	public static VagrantConfigExtractor getInstance(){
		if(configExtractor == null){
			configExtractor = new VagrantConfigExtractor();
		}
		return configExtractor;
	}
	
	public VagrantConfigExtractor(){
		
	}
	
	public String parseVagrantID(String line){
		int colonIndex = line.indexOf(":");
		int doKeywordIndex = line.indexOf("do");
		String vagrantID = line.substring(colonIndex + 1, doKeywordIndex).trim(); 
		//Restrict vagrantID characters length
		return vagrantID.length() > 7 ? vagrantID.substring(0,7) : vagrantID;
	}
	
	public String parseNetworkIP(String content){
		 
		Pattern pattern = Pattern.compile(IPADDRESS_PATTERN);
		Matcher  matcher = pattern.matcher(content);
		String ipAddress  = "";
		while(matcher.find()){
			ipAddress = matcher.group();
		}
		return ipAddress;
	}
	
	public PortForwardingMapping parsePortMapping(String content){
		PortForwardingMapping portMapping = new PortForwardingMapping();
		String matchedPattern = matchRegex(PORT_MAPPING_PATTERN,content);
		
		String patterns[] = matchedPattern.split(",");
		for(int counter = 0; counter <=1 ; counter++){
			String mappings[] = patterns[counter].split(":");
			if(mappings[0].trim().equalsIgnoreCase("host")){
				portMapping.setHostPort(mappings[1].trim());
			}else{
				portMapping.setVmPort(mappings[1].trim());
			}	
		}
		return portMapping;
	}
	
	public String parseHostName(String content){
		return content.split("=")[1].replaceAll("\"", " ").trim();
	}
	
	public String parseBootTimeout(String content){
		return content.split("=")[1].replaceAll("\"", " ").trim();
	}
	
	public String parseBoxUrl(String content){
		return content.split("=")[1].replaceAll("\"", " ").trim();
	}
	
	public String parseBoxType(String content){
		return content.split("=")[1].replaceAll("\"", " ").trim();
	}
	
	public String parseMemory(String content){
		return matchRegex(RAM_PATTERN,content).split(",")[1].replaceAll("\"", " ").trim();
	}
	
	public String parseCPUExecCap(String content){
		return matchRegex(CPU_PATTERN,content).split(",")[1].replaceAll("\"", " ").trim();
	}
	
	public String parseProviderType(String content){
		return matchRegex(QUOTES_PATTERN,content).replaceAll("\"", " ").trim();
	}
	
	public boolean parseGUIMode(String content){
		return matchRegex(GUI_MODE_PATTERN,content).split("=")[1].replaceAll("\"", " ").trim().equalsIgnoreCase("true");
	}
	
	public String parseProvisionerName(String content){
		return matchRegex(PROVISIONER_NAME_PATTERN,content).split("=")[1].replaceAll("\"", " ").trim();
	}
	
	public void parseFolderMapping(ArrayList<SyncFolderMapping> folderMappings, String content){
		String matchedData = matchRegex(FOLDER_MAPPING, content);
		SyncFolderMapping folderMapping = new SyncFolderMapping();
		folderMapping.setHostFolder(matchedData.split(",")[0].replaceAll("\"", " ").trim());
		folderMapping.setVmFolder(matchedData.split(",")[1].replaceAll("\"", " ").trim());
		folderMappings.add(folderMapping);
	}
	
	private String matchRegex(String pattern, String content){
		Pattern regexPattern = Pattern.compile(pattern);
		Matcher matcher = regexPattern.matcher(content);
		String matchedData = "";
		while(matcher.find()){
			matchedData = matcher.group();
		}
		return matchedData;
	}
	
	public static void main(String args[]){
		/*VagrantConfigExtractor configExtractor = new VagrantConfigExtractor();
		boolean result = configExtractor.parseNetworkIP("boxTest.vm.network \"private_network\", ip : \"192.168.111.38\"");
		System.out.println(result);*/
	}
}
