package com.paxcel.trial;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.paxcel.boxupp.beans.BoxuppVMData;
import com.paxcel.boxupp.beans.PortForwardingMapping;
import com.paxcel.boxupp.beans.SyncFolderMapping;
import com.paxcel.boxupp.beans.VMConfiguration;

public class VagrantParser {
	
	private BoxuppVMData vmConfigurations;
	private ArrayList<VMConfiguration> vmConfigurationList;
	private VMConfiguration activeVM;
	private String dataLine;
	private Stack<ParserElement> elementsStack;
	private ArrayList<Pattern> blockMarkersPatterns;
	private VagrantConfigExtractor configExtractor;
	private int machineCount;
	
	public VagrantParser(){
		
		this.vmConfigurations = new BoxuppVMData();
//		VMConfiguration vmConfiguration = new VMConfiguration();
		this.vmConfigurationList = vmConfigurations.getVmData();
//		this.vmConfigurationList.add(vmConfiguration);
		
//		this.activeVM = vmConfiguration;
		elementsStack = new Stack<ParserElement>();

		blockMarkersPatterns = new ArrayList<Pattern>();
		//Indexes are important//
		blockMarkersPatterns.add(this.compilePatterns("vagrant.configure"));
		blockMarkersPatterns.add(this.compilePatterns(".vm.define"));
		blockMarkersPatterns.add(this.compilePatterns(".vm.provider"));
		blockMarkersPatterns.add(this.compilePatterns(".vm.provision[ ]*:[a-zA-Z\\d\\w\\s]*[ ]*do[ ]*\\|[a-zA-Z\\d\\w\\s]*\\|"));
		
		configExtractor = VagrantConfigExtractor.getInstance();
		this.machineCount = 0;
	}
	
	public BoxuppVMData fetchConfigurations(){
		return vmConfigurations;
	}
	
	public Pattern compilePatterns(String regexString){
		Pattern pattern = Pattern.compile(regexString);
		return pattern;
	}
	
	public void addData(String content){
		System.out.println(content);
		this.dataLine = content.trim().toLowerCase();
		if(isRelevantData()){
			updateMachineCount();
			submitForParsing();
		}
	}
	
	private void updateMachineCount(){
		/*if(dataLine.indexOf(".vm.define") > -1){
			machineCount++;
			System.out.println(machineCount);
		}*/
		if(denotesNewMachine(dataLine)){
			machineCount++;
			addNewVM();
		}
	}
	
	private void submitForParsing(){
		
		if(isBlockLevelElement()){
			ParserElement parserElement = this.new ParserElement(true,dataLine);
			elementsStack.push(parserElement);
			
		}else{
			ParserElement parserElement = this.new ParserElement(false,dataLine);
			elementsStack.push(parserElement);
			if(dataLine.equalsIgnoreCase("end")){
				popStackElements();
			}
		}
	}

	private boolean isBlockLevelElement(){
		for(int counter = 0; counter < blockMarkersPatterns.size() ; counter++){
			/*if(dataLine.indexOf(blockMarkersPatterns.get(counter).toLowerCase()) > -1){
				return true;
			}*/
			Pattern pattern = blockMarkersPatterns.get(counter);
			Matcher matcher = pattern.matcher(dataLine);
			while(matcher.find()){
				return true;
			}
		}
		return false;
	}
	
	private void addNewVM(){
		VMConfiguration newVMConfig = new VMConfiguration();
		this.activeVM = newVMConfig;
		vmConfigurationList.add(newVMConfig);
	}
	
	private void popStackElements(){
		while(true){
			ParserElement elementContent = elementsStack.pop();
			populateConfigurations(elementContent.getContent());
			if(elementContent.isBlockLevelElement()){
				break;
			}
			
		}
	}
	
	public boolean denotesNewMachine(String content){
		/*if(content.indexOf(blockMarkersPatterns.get(1)) > -1){
			return true;
		}*/
		Pattern pattern = blockMarkersPatterns.get(1);
		Matcher matcher = pattern.matcher(content);
		while(matcher.find()){
			return true;
		}
		return false;
	}
	
	private void populateConfigurations(String content){
	
		if(content.indexOf(".vm.define") > -1){
			activeVM.setVagrantID(configExtractor.parseVagrantID(content));
		}
		else if(content.indexOf(".vm.hostname") > -1){
			activeVM.setHostName(configExtractor.parseHostName(content));
		}
		else if((content.indexOf(".vm.box") > -1) && (content.indexOf(".vm.box_url") == -1)){
			activeVM.setBoxType(configExtractor.parseBoxType(content));
		}
		else if(content.indexOf(".vm.box_url") > -1){
			activeVM.setBoxUrl(configExtractor.parseBoxUrl(content));
		}
		else if(content.indexOf(".vm.boot_timeout") > -1){
			activeVM.setBootTimeout(configExtractor.parseBootTimeout(content));
		}
		else if((content.indexOf(".vm.network") > -1) && (content.indexOf("private_network") > -1)){
			activeVM.setNetworkIP(configExtractor.parseNetworkIP(content));
		}
		else if((content.indexOf("forwarded_port") > -1) && (content.indexOf(".vm.network") > -1)){
			ArrayList<PortForwardingMapping> portMappings = activeVM.getPortMappings();
			if(portMappings == null){
				portMappings = new ArrayList<PortForwardingMapping>();
			}
			portMappings.add(configExtractor.parsePortMapping(content));
			activeVM.setPortMappings(portMappings);
		}
		else if(content.indexOf(".vm.provider") > -1){
			activeVM.setProviderType(configExtractor.parseProviderType(content));
		}
		else if(content.indexOf("modifyvm") > -1 && (content.indexOf("--memory") > -1)){
			activeVM.setMemory(configExtractor.parseMemory(content));
		}
		else if(content.indexOf("modifyvm") > -1 && (content.indexOf("--cpuexecutioncap") > -1)){
			activeVM.setCpuExecCap(configExtractor.parseCPUExecCap(content));
		}
		else if(content.indexOf(".gui") > -1){
			activeVM.setGuiMode(configExtractor.parseGUIMode(content));
		}
		else if(content.indexOf(".name") > -1){
			activeVM.setProvisionerName(configExtractor.parseProvisionerName(content));
		}
		else if(content.indexOf(".vm.synced_folder") > -1){
			ArrayList<SyncFolderMapping> folderMappings = activeVM.getSyncFolders();
			if(folderMappings == null){
				folderMappings = new ArrayList<SyncFolderMapping>();
				activeVM.setSyncFolders(folderMappings);
			}
			configExtractor.parseFolderMapping(folderMappings,content);
		}
	}
	
	private boolean isRelevantData(){
		if(dataLine.isEmpty() || dataLine.startsWith("#")){ 
			return false;
		}
		return true;
	}
	
	
	class ParserElement{
		private boolean isBlockLevelElement;
		private String content;
		
		public ParserElement(boolean isBlockLevel, String content){
			this.isBlockLevelElement = isBlockLevel;
			this.content = content;
		}

		public boolean isBlockLevelElement() {
			return isBlockLevelElement;
		}

		public void setBlockLevelElement(boolean isBlockLevelElement) {
			this.isBlockLevelElement = isBlockLevelElement;
		}

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}
	}
	
	public static void main(String args[]) throws IOException{
		VagrantParser parser = new VagrantParser();
		BufferedReader reader = new BufferedReader(new FileReader(new File("D:\\Vagrantfile3")));
		String line;
		while((line = reader.readLine()) != null){
			parser.addData(line);
		}
//		System.out.println(parser.fetchConfigurations().getVmData().size());
		VMConfiguration vmConfig = parser.fetchConfigurations().getVmData().get(0);
		System.out.println("Hello");
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
