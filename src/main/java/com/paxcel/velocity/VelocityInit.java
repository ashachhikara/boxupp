package com.paxcel.velocity;

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
		ve.init(prop);
	}
	
	public static Template getTemplate(VelocityEngine ve){
		Template template = null;
		template = ve.getTemplate("boxupp.vm");
		return template;
	}
	
}

