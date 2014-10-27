package com.paxcel.boxupp;

import java.io.StringWriter;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

import com.boxupp.db.beans.MachineConfigurationBean;
import com.boxupp.db.beans.PuppetModuleBean;
import com.boxupp.db.beans.PuppetModuleMachineConfigMapping;
import com.boxupp.db.beans.ShellScriptBean;
import com.boxupp.db.beans.ShellScriptMachineConfigMapping;
import com.paxcel.boxupp.beans.BoxuppVMData;
import com.paxcel.velocity.VelocityInit;


public class ConfigurationGenerator {	
	
	private static Logger logger = LogManager.getLogger(ConfigurationGenerator.class.getName());
	private static boolean configurationGenerated;
	private static String velocityFinalTemplate = "";
		
	public static boolean isConfigurationGenerated() {
		return configurationGenerated;
	}

	public static void setConfigurationGenerated(boolean configurationGenerated) {
		ConfigurationGenerator.configurationGenerated = configurationGenerated;
	}

	public static String getVelocityFinalTemplate() {
		return velocityFinalTemplate;
	}

	public static void setVelocityFinalTemplate(String velocityFinalTemplate) {
		ConfigurationGenerator.velocityFinalTemplate = velocityFinalTemplate;
	}

	//Note: The output after template merger can also be redirected to a file on disk //
	 public static boolean generateConfig(
			List<MachineConfigurationBean> machineConfigList,
			List<PuppetModuleBean> puppetModules,
			List<ShellScriptBean> shellScripts,
			List<ShellScriptMachineConfigMapping> scriptMappings,
			List<PuppetModuleMachineConfigMapping> moduleMappings, String provider){
		
		VelocityEngine ve = VelocityInit.getVelocityInstance();
		Template template = VelocityInit.getTemplate(ve, provider);
		VelocityContext context = new VelocityContext();
		context.put("boxuppConfig", machineConfigList);
		context.put("scripts", shellScripts );
		context.put("modules", puppetModules);
		context.put("moduleMappings", moduleMappings);
		context.put("scriptMappings", scriptMappings);
		
		try{
			StringWriter stringWriter = new StringWriter();
			template.merge(context, stringWriter);
			velocityFinalTemplate = stringWriter.toString();
			setConfigurationGenerated(true);
		}
		catch(ResourceNotFoundException e){
			logger.error("Boxupp Template not found");
			setConfigurationGenerated(false);
		} 
		catch(ParseErrorException e){
			logger.error("Error parsing Boxupp template");
			setConfigurationGenerated(false);
		}
		return configurationGenerated;
	}

	
}
