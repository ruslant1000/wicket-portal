package kz.tem.portal.server.plugin;

import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
/**
 * 
 * @author Ruslan Temirbulatov
 * Конфигурация конкретного экземпляра модуля.
 */
@SuppressWarnings("serial")
public class ModuleConfig implements Serializable{

	private Map<String, String> values = new HashMap<String, String>();
	private Map<String, String> booles = new HashMap<String, String>();
	
	private List<String> names = new LinkedList<String>();

	public ModuleConfig() {
		super();
	}

	public Map<String, String> getValues() {
		return values;
	}

	public void setValues(Map<String, String> values) {
		this.values = values;
	}

	public Map<String, String> getBooles() {
		return booles;
	}

	public void setBooles(Map<String, String> booles) {
		this.booles = booles;
	}

	public List<String> getNames() {
		return names;
	}

	public void setNames(List<String> names) {
		this.names = names;
	}
	
	
	public void addDefaultConfig(String name, String value)throws Exception{
		if(names.contains(name))
			throw new Exception("Конфигурация '"+name+"' не может быть добавлена дважды");
		names.add(name);
		values.put(name, value);
	}
	public void addDefaultConfig(String name, boolean value)throws Exception{
		if(names.contains(name))
			throw new Exception("Конфигурация '"+name+"' не может быть добавлена дважды");
		names.add(name);
		booles.put(name, ""+value);
	}
	
	
	public String toXML(){
		String xml = "<config>";
		for(String name:names){
			xml=xml+"<param>"
					+ "<name>"+name+"</name>";
			if(values.containsKey(name)){
				xml = xml + "<type>string</type>"
						+ "<value>"+values.get(name)+"</value>";
			}else if(booles.containsKey(name)){
				xml = xml + "<type>boolean</type>"
						+ "<value>"+booles.get(name)+"</value>";
			}else{
				xml = xml + "<type>string</type>"
						+ "<value></value>";
			}
			xml=xml+ "</param>";
		}
		xml = xml+"</config>";
		return xml;
	} 
	public static ModuleConfig parse(String xml)throws Exception{
		ByteArrayInputStream bis = null;
		try{
			ModuleConfig config = new ModuleConfig();
			bis = new ByteArrayInputStream(xml.getBytes());
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(bis);
			doc.normalize();
			
			NodeList params = doc.getElementsByTagName("param");
			for(int i=0;i<params.getLength();i++){
				Node param = params.item(i);
				if(param.getNodeType()!=Node.ELEMENT_NODE)
					continue;
				Element e = (Element)param;
				
				String name = e.getElementsByTagName("name").item(0).getTextContent();
				String type = e.getElementsByTagName("type").item(0).getTextContent();
				String value = e.getElementsByTagName("value").item(0).getTextContent();
				if(type.equals("boolean"))
					config.addDefaultConfig(name,Boolean.parseBoolean(value));
				else
					config.addDefaultConfig(name, value);
			}
			return config;
		}catch(Exception ex){
			ex.printStackTrace();
			throw new Exception("Could not parse ModuleConfig from xml: "+xml);
		}finally{
			try{bis.close();}catch(Exception ex){}
		}
	}
	
	
	public void copyFrom(ModuleConfig config){
		for(String key:config.getValues().keySet()){
			getValues().put(key, config.getValues().get(key));
		}
		for(String key:config.getBooles().keySet()){
			getBooles().put(key, config.getBooles().get(key));
		}
		
	}
	public static void main(String[] args) throws Exception {
		ModuleConfig config = new ModuleConfig();
		config.addDefaultConfig("param1", "value1");
		config.addDefaultConfig("param2", "ыва выаывары  fsdf sdfsdв !");
		config.addDefaultConfig("param3", null);
		config.addDefaultConfig("param4", true);
		
		System.out.println(config.toXML());
		
		ModuleConfig config2 = ModuleConfig.parse(config.toXML());
		config2.getBooles().put("param4", "false");
		System.out.println(config2.getBooles().get("param4").equals("true"));
		
	}
}
