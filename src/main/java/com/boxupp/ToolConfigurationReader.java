package com.boxupp;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

import javax.xml.bind.Binder;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import com.boxupp.beans.Config;

public class ToolConfigurationReader {

	public Config getConfiguration(){

		JAXBContext jc; //jaxbContext
		Config config = null ;
		try {
			jc = JAXBContext.newInstance(Config.class);

//			File file = new File(this.getClass().getResource("/config.xml").toURI());
			InputStream toolSettings = getClass().getResourceAsStream("/config.xml");
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db;
			db = dbf.newDocumentBuilder();
			Document doc;
			doc = db.parse(toolSettings);
			Binder<Node> binder = jc.createBinder();
		    config = (Config) binder.unmarshal(doc);
			binder.updateXML(config);
			
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return config;

	}
}