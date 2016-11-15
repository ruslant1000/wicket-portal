package kz.tem.portal.api;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.wicket.protocol.http.WebApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kz.tem.portal.PortalException;
import kz.tem.portal.api.model.LayoutInfo;
import kz.tem.portal.api.model.ThemeInfo;
import kz.tem.portal.server.model.Settings;
/**
 * 
 * @author Ruslan Temirbulatov
 *
 */
public class ExplorerEngine {
	
	private static Logger log = LoggerFactory.getLogger(ExplorerEngine.class);
	
	public static ExplorerEngine instance = null;
	
	private Map<String, LayoutInfo> layouts = new HashMap<String, LayoutInfo>();
	private Map<String, ThemeInfo> themes = new HashMap<String, ThemeInfo>();
	
	/**
	 * Настройки портала. относительный URL страницы, которая используется в качестве главной
	 */
	public static final String SETTINGS_MAIN_PAGE="Main page";
	/**
	 * общие настройки портала
	 */
	private Map<String, String> settings = new HashMap<String, String>();
	
	public static ExplorerEngine getInstance(){
		if(instance==null)
			instance = new ExplorerEngine();
		return instance;
	}
	
	private ExplorerEngine(){
		settings.clear();
		settings.put(SETTINGS_MAIN_PAGE, null);
		loadSettings();
	}
	
	public Map<String, String> getSettings() {
		return settings;
	}

	public void setSettings(Map<String, String> settings) {
		this.settings = settings;
	}
	public String getSettingsValue(String name){
		return settings.get(name);
	}
	/**
	 * Загрузка значений всех настроек в память, чтобы не делать вызов базы по много раз
	 */
	public void loadSettings(){
		try {
			for(Settings sett:RegisterEngine.getInstance().getSettingsRegister().table(0, 0).records()){
				this.settings.put(sett.getName(), sett.getValue());
			}
		} catch (PortalException e) {
			e.printStackTrace();
			log.error("Could not load settings",e);
		}
		
	}

	//************************************
//  Работа с Layout's
	public Map<String, LayoutInfo> getLayouts(){
		return layouts;
	}
	public List<LayoutInfo> getLayoutsList(){
		List<LayoutInfo> list = new LinkedList<LayoutInfo>();
		list.addAll(getLayouts().values());
		return list;
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
					readLayoutFile(f);
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
	
	private void readLayoutFile(File file)throws Exception{
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
						Set<String> wicketId = readWicketId(line);
						System.out.println("new location "+wicketId);
						Iterator<String> it=wicketId.iterator(); 
						while(it.hasNext()){
							layout.getLocations().add(it.next());
							
						}
						
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
	
	public LayoutInfo getLayout(String name){
		if(layouts.containsKey(name))
			return layouts.get(name);
		return null;
	}
	
	private static Set<String> readWicketId(String line){
		Set<String> set = new HashSet<String>();
		
		String line2=new String(line.getBytes());
		
		while(line2.indexOf("wicket:id=")!=-1){
			line2=line2.substring(line2.indexOf("wicket:id=")+"wicket:id=".length()+1);
			line2=line2.substring(0,line2.indexOf("\""));
			set.add(line2);
			line2 = line.substring(line.indexOf("wicket:id=\""+line2+"\"")+10);
		}
		
		
		return set;
	}
	
//************************************
	
	
//************************************
//  Работа с Theme's
	public void initThemes(WebApplication application){
		log.debug("Инициализация списка theme...");
		themes.clear();
		String themesPath = application.getServletContext().getRealPath("themes");
		System.out.println("!!!!!!! themesPath: "+themesPath);
		File dir = new File(themesPath);
		if(!dir.exists())
			throw new RuntimeException("Не найдена директория themes");
		
		File[] fls = dir.listFiles();
		System.out.println("!!!! "+fls.length);
		if(fls==null || fls.length==0)
			throw new RuntimeException("Не найдено ни одного theme в директории themes");
		
		boolean anyRead = false;
		for(File f:fls){
			if(f.getName().toLowerCase().endsWith(".html")){
				try {
					readThemeFile(f);
					anyRead = true;
				} catch (Exception e) {
					e.printStackTrace();
					log.error("Ошибка при чтении theme "+f.getName());
				}
			}
		}
		if(!anyRead){
			log.error("Нет ни одного валидного theme в директории themes");
			throw new RuntimeException("Нет ни одного валидного theme в директории themes");
		}
	}
	
	private void readThemeFile(File file)throws Exception{
		System.out.println("..."+file.getAbsolutePath());
		log.debug("Инициализация layout "+file.getAbsolutePath()+"...");
		FileReader fr = null;
		BufferedReader br = null;
		System.out.println("--------------");
		try{
			fr = new FileReader(file);
			br = new BufferedReader(fr);
			ThemeInfo theme = new ThemeInfo();
			theme.setName(file.getName());
			theme.setFileName(file.getName());
			
			while(br.ready()){
				String line = br.readLine();
				
				if(line!=null && line.trim().length()!=0){
					line=line.toLowerCase();
					if(line.indexOf("wicket:id")!=-1){
						Set<String> wicketId = readWicketId(line);
						if(wicketId.contains("layout"))
							theme.setLayout(true);
						if(wicketId.contains("menu"))
							theme.setMenu(true);
						if(wicketId.contains("menu-link"))
							theme.setMenuLink(true);
						if(wicketId.contains("sub-menu"))
							theme.setSubMenu(true);
						if(wicketId.contains("sub-menu-link"))
							theme.setSubMenuLink(true);
						if(wicketId.contains("user"))
							theme.setUser(true);
						if(wicketId.contains("user-name"))
							theme.setUserName(true);
						if(wicketId.contains("user-logout"))
							theme.setUserLogout(true);
						if(wicketId.contains("guest"))
							theme.setGuest(true);
						if(wicketId.contains("guest-login"))
							theme.setGuestLogin(true);
					}
				}
			}
			themes.put(theme.getName(), theme);
			System.out.println("--------------");
		}finally{
			try{br.close();}catch(Exception ex){}
			try{fr.close();}catch(Exception ex){}
		}
	}
	
	public Map<String, ThemeInfo> getThemes() {
		return themes;
	}
	
	public List<ThemeInfo> getThemesList(){
		List<ThemeInfo> list = new LinkedList<ThemeInfo>();
		list.addAll(getThemes().values());
		return list;
	}
	//************************************
	
	
	public static void main(String[] args) {
		System.out.println(readWicketId("<td><div wicket:id=\"portlet1\"><a wicket:id=\"aaa\"/></div></td>"));
	}


	

}
