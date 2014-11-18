package com.boxupp.velocity;

import java.util.Properties;

import org.apache.velocity.Template;
import org.apache.velocity.app.VelocityEngine;

public class VelocityInit {
	
	public static VelocityEngine getVelocityInstance(){
		VelocityEngine ve = new VelocityEngine();
		setVelocityProperties(ve);
		return ve;
	}
	
	static void setVelocityProperties(VelocityEngine ve){
		Properties prop = new Properties();
		prop.setProperty("resource.loader", "class");
		prop.setProperty("class.resource.loader.description", "Velocity Classpath Resource Loader");
		prop.setProperty("class.resource.loader.class", 
					  "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
		prop.setProperty("math", "org.apache.velocity.tools.generic.MathTool");
		ve.init(prop);
	}
	
	public static Template getTemplate(VelocityEngine ve, String provider){
		Template template = null;
		if(provider.equalsIgnoreCase("virtualBox") ){
			template = ve.getTemplate("virtualBox.vm");
		}else if(provider.equalsIgnoreCase("docker")){
			template = ve.getTemplate("docker.vm");
		}
		return template;
	}
	
	public static Template getNodeTemplate(VelocityEngine ve){
		return ve.getTemplate("node.vm");
	}
}

