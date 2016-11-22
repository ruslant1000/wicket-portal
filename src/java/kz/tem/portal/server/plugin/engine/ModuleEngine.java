package kz.tem.portal.server.plugin.engine;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import kz.tem.portal.context.listener.ModuleContextListener;
import kz.tem.portal.server.plugin.Module;
import kz.tem.portal.server.plugin.ModuleConfig;
import kz.tem.portal.server.plugin.ModuleMeta;
import kz.tem.portal.utils.FileUtils;


/**
 * 
 * @author Ruslan Temirbulatov
 * 
 */
public class ModuleEngine {
	
	private static Logger log = LoggerFactory.getLogger(ModuleEngine.class); 
	
	public static ModuleEngine instance = null;
	
	private Map<String, JarClassLoader> loaders = new HashMap<String, JarClassLoader>();
	
	private Map<String, ModuleMeta> moduleMap = new HashMap<String, ModuleMeta>();
	
	private Map<String, ModuleContextListener> moduleContextListeners = new HashMap<String, ModuleContextListener>();
	
	private String modulesPath;
	
	public static ModuleEngine getInstance(){
		if(instance==null){
			instance = new ModuleEngine();
		}
		return instance;
			
	}
	private ModuleEngine(){}
	
	/**
	 * Инициализация модуля
	 * @param id - wicket:id
	 * @param meta - информация о модуле
	 * @return - 
	 * @throws Exception
	 */
	public Module init(String id, ModuleMeta meta, ModuleConfig config)throws Exception{
		if(!loaders.containsKey(meta.getModuleName())){
			throw new Exception("не найден JarClassLoader: "+meta.getModuleName());
		}
		log.debug("�������� �������� ������ "+meta.getModuleName()+"...");
		Class cls = loaders.get(meta.getModuleName()).loadClass(meta.getModuleClass());
		Module module = (Module)cls.getConstructor(new Class[]{String.class,ModuleConfig.class}).newInstance(new Object[]{id,config});
		log.debug("�������� �������� ������ "+meta.getModuleName()+" ���������");
		return module;
	}
	 /**
	  * Загрузка модулей из директории modulesPath.
	  * @param modulesPath - директория расположения модулей
	  * @throws Exception
	  */
	public void loadModules(String modulesPath)throws Exception{
		log.info("Загрузка модулей...");
		this.modulesPath=modulesPath;
		new ModuleFinder().findNewModules(modulesPath);
		
		File modulesDir = new File(modulesPath);
		if(!modulesDir.exists())
			throw new Exception("Не удалось загрузить модуль: "+modulesPath);
		for(File module:modulesDir.listFiles()){
			if(module.isDirectory()){
				load(module.getAbsolutePath());
			}
		}
		log.info("Загрузка модулей завершена");
	}
	/**
	 * Загрузка модуля
	 * @param modulePath
	 * @return
	 * @throws Exception
	 */
	public ModuleMeta load(String modulePath)throws Exception{
		log.info("Загрузка модуля "+modulePath+"...");
		ModuleMeta meta = new ModuleMeta();
		File moduleDir = new File(modulePath);
		if(!moduleDir.exists())
			throw new Exception("�� ������� ���������� "+modulePath);
		File moduleXml = new File(moduleDir,"module.xml");
		if(!moduleXml.exists())
			throw new Exception("�� ������ ���������� ������ "+modulePath);
		
		DocumentBuilderFactory dbf = null;
		DocumentBuilder db = null;
		Document xml = null;
		try{
			dbf = DocumentBuilderFactory.newInstance();
			db = dbf.newDocumentBuilder();
			xml = db.parse(new FileInputStream(moduleXml));
			
			xml.getDocumentElement().normalize();
			Element module = (Element)xml.getElementsByTagName("module").item(0);
			Element moduleE = (Element)module;
			String moduleName = ((Element)moduleE.getElementsByTagName("module-name").item(0)).getTextContent();
			String displayName = ((Element)moduleE.getElementsByTagName("display-name").item(0)).getTextContent();
			String moduleClass = ((Element)moduleE.getElementsByTagName("module-class").item(0)).getTextContent();
			
			
					
			meta.setDisplayName(displayName);
			meta.setModuleClass(moduleClass);
			meta.setModuleName(moduleName);
			meta.setModuleDirectoryPath(modulePath);
			
			if(loaders.containsKey(moduleName)){
				return moduleMap.get(moduleName);
			}
			
			JarClassLoader jcl = new JarClassLoader(new File(moduleDir,"lib").getPath());
			
			String contextListener = null;
			
			if(moduleE.getElementsByTagName("context-listener").getLength()>0){
				contextListener = ((Element)moduleE.getElementsByTagName("context-listener").item(0)).getTextContent();
				ModuleContextListener mcl = (ModuleContextListener)
						jcl.loadClass(contextListener).newInstance();
				mcl.initialize();
				moduleContextListeners.put(moduleName, mcl);
			}
			
			
			if(loaders.containsKey(moduleName)){
				throw new Exception("Error loading mocule "+moduleName+"");
			}
			loaders.put(moduleName, jcl);
		}finally{
			  
		}
		moduleMap.put(meta.getModuleName(), meta);
		log.info("�������� ������ "+modulePath+" ���������");
		return meta;
	}
	
	public List<ModuleMeta> getModulesList(){
		List<ModuleMeta> list = new LinkedList<ModuleMeta>();
		list.addAll(getModuleMap().values());
		return list;
	}
	public Map<String, ModuleMeta> getModuleMap() {
		return moduleMap;
	}
	public void setModuleMap(Map<String, ModuleMeta> moduleMap) {
		this.moduleMap = moduleMap;
	} 
	
	public JarClassLoader getClassLoader(String moduleName){
		if(loaders.containsKey(moduleName))
			return loaders.get(moduleName);
		return null;
	}
	/**
	 * Выгрузка модуля из JVM
	 * @param moduleName
	 */
	public void undeploy(String moduleName){
		String mp = new String(moduleMap.get(moduleName).getModuleDirectoryPath());
		
		if(moduleContextListeners.containsKey(moduleName)){
			try {
				moduleContextListeners.get(moduleName).destroy();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		moduleMap.remove(moduleName);
		loaders.get(moduleName).destroy();
		loaders.remove(moduleName);
		
		
        
		
		try {
			System.gc();
			FileUtils.deleteFile(mp+".zip");
			Thread.sleep(1000);
			FileUtils.deleteDirectory(mp);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Дозагрузка незагруженных модулей 
	 */
	public void loadNewModules(){
		try {
			loadModules(modulesPath);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	public void destroy(){
		
	}
	
}
