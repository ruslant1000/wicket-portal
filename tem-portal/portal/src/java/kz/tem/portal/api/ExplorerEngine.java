package kz.tem.portal.api;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.apache.wicket.protocol.http.WebApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kz.tem.portal.api.model.LayoutInfo;
import kz.tem.portal.server.bean.ITable;

public class ExplorerEngine {
	
	private static Logger log = LoggerFactory.getLogger(ExplorerEngine.class);
	
	public static ExplorerEngine instance = null;
	
	private Map<String, LayoutInfo> layouts = new HashMap<String, LayoutInfo>();
	
	public static ExplorerEngine getInstance(){
		if(instance==null)
			instance = new ExplorerEngine();
		return instance;
	}
	
	private ExplorerEngine(){}
	
	public Map<String, LayoutInfo> getLayouts(){
		return layouts;
	}
	
	public void initLayouts(WebApplication application){
		log.debug("Инициализация списка layout...");
		layouts.clear();
		String layoutsPath = application.getServletContext().getRealPath("layouts");
		System.out.println("!!!!!!! layoutsPath: "+layoutsPath);
		File dir = new File(layoutsPath);
		if(!dir.exists())
			throw new RuntimeException("Не найдена директория layouts");
		
		File[] fls = dir.listFiles();
		System.out.println("!!!! "+fls.length);
		if(fls==null || fls.length==0)
			throw new RuntimeException("Не найдено ни одного layout в директории layouts");
		
		boolean anyRead = false;
		for(File f:fls){
			if(f.getName().toLowerCase().endsWith(".html")){
				try {
					readFile(f);
					anyRead = true;
				} catch (Exception e) {
					e.printStackTrace();
					log.error("Ошибка при чтении layout "+f.getName());
				}
			}
		}
		if(!anyRead){
			log.error("Нет ни одного валидного layout в директории layouts");
			throw new RuntimeException("Нет ни одного валидного layout в директории layouts");
		}
	}
	
	private void readFile(File file)throws Exception{
		System.out.println("..."+file.getAbsolutePath());
		log.debug("Инициализация layout "+file.getAbsolutePath()+"...");
		FileReader fr = null;
		BufferedReader br = null;
		System.out.println("--------------");
		try{
			fr = new FileReader(file);
			br = new BufferedReader(fr);
			LayoutInfo layout = new LayoutInfo();
			layout.setName(file.getName());
			layout.setLocations(new LinkedList<String>());
			while(br.ready()){
				String line = br.readLine();
				
				if(line!=null && line.trim().length()!=0){
					line=line.toLowerCase();
					if(line.indexOf("wicket:id")!=-1){
						String wicketId = readWicketId(line);
						System.out.println("new location "+wicketId);
						layout.getLocations().add(wicketId);
					}
				}
			}
			System.out.println("!!!! add layout: "+layout.getName());
			layouts.put(layout.getName(), layout);
			System.out.println("--------------");
		}finally{
			try{br.close();}catch(Exception ex){}
			try{fr.close();}catch(Exception ex){}
		}
	}
	
	private static String readWicketId(String line){
		line=line.substring(line.indexOf("wicket:id=")+"wicket:id=".length()+1);
		line=line.substring(0,line.indexOf("\""));
		return line;
	}
	public static void main(String[] args) {
		System.out.println(readWicketId("<td><div wicket:id=\"portlet1\"></div></td>"));
	}
	

}
