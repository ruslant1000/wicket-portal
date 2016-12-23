package kz.tem.portal.server.plugin.engine;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

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
		if(!loaders.containsKey(meta.getArtifactId())){
			throw new Exception("не найден JarClassLoader: "+meta.getArtifactId());
		}
		Class cls = loaders.get(meta.getArtifactId()).loadClass(meta.getModuleClass());
		Module module = (Module)cls.getConstructor(new Class[]{String.class,ModuleConfig.class}).newInstance(new Object[]{id,config});
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
				try{
					loadArtifact(module.getAbsolutePath());
				}catch(Exception ex){
					ex.printStackTrace();
				}
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
	public void loadArtifact(String modulePath)throws Exception{
		log.info("Load modules artifact "+modulePath+"...");
		
		File moduleDir = new File(modulePath);
		if(!moduleDir.exists())
			throw new Exception("Filed loading "+modulePath);
		File moduleXml = new File(moduleDir,"module.xml");
		if(!moduleXml.exists())
			throw new Exception("Filed loading "+modulePath);
		
		DocumentBuilderFactory dbf = null;
		DocumentBuilder db = null;
		Document xml = null;
		try{
			dbf = DocumentBuilderFactory.newInstance();
			db = dbf.newDocumentBuilder();
			xml = db.parse(new FileInputStream(moduleXml));
			
			xml.getDocumentElement().normalize();
			Element modules = (Element)xml.getElementsByTagName("modules").item(0);
			String id = modules.getElementsByTagName("id").item(0).getTextContent();
			
			NodeList nlist = modules.getElementsByTagName("module");
			if(nlist!=null)
				for(int i=0;i<nlist.getLength();i++){
					ModuleMeta meta = new ModuleMeta();
					Element moduleE = (Element)nlist.item(i);
					String moduleName = ((Element)moduleE.getElementsByTagName("module-name").item(0)).getTextContent();
					String displayName = ((Element)moduleE.getElementsByTagName("display-name").item(0)).getTextContent();
					String moduleClass = ((Element)moduleE.getElementsByTagName("module-class").item(0)).getTextContent();
					
					meta.setArtifactId(id);
					meta.setDisplayName(displayName);
					meta.setModuleClass(moduleClass);
					meta.setModuleName(moduleName);
					meta.setModuleDirectoryPath(modulePath);
					
					moduleMap.put(meta.getModuleName(), meta);
					log.info("\tloaded module "+meta.getModuleName());
				}
			
//			Element module = (Element)xml.getElementsByTagName("module").item(0);
//			Element moduleE = (Element)module;
//			String moduleName = ((Element)moduleE.getElementsByTagName("module-name").item(0)).getTextContent();
//			String displayName = ((Element)moduleE.getElementsByTagName("display-name").item(0)).getTextContent();
//			String moduleClass = ((Element)moduleE.getElementsByTagName("module-class").item(0)).getTextContent();
			
			
					
			
			
//			if(loaders.containsKey(moduleName)){
//				return moduleMap.get(moduleName);
//			}
			
			JarClassLoader jcl = new JarClassLoader(new File(moduleDir,"lib").getPath());
			
			String contextListener = null;
			
			if(modules.getElementsByTagName("context-listener").getLength()>0){
				contextListener = ((Element)modules.getElementsByTagName("context-listener").item(0)).getTextContent();
				ModuleContextListener mcl = (ModuleContextListener)
						jcl.loadClass(contextListener).newInstance();
				mcl.initialize();
				moduleContextListeners.put(id, mcl);
			}
			
			
			if(loaders.containsKey(id)){
				throw new Exception("Error loading mocules artifact "+id+"");
			}
			loaders.put(id, jcl);
		}finally{
			  
		}
		
		log.info("Load modules artifact "+modulePath+" complete");
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
	
	public JarClassLoader getClassLoader(String artifactId){
		if(loaders.containsKey(artifactId))
			return loaders.get(artifactId);
		return null;
	}
	/**
	 * Выгрузка модуля из JVM
	 * @param artifactId
	 */
	public void undeploy(String artifactId){
		System.out.println("********************************************");
		String mp = null;
		
		if(moduleContextListeners.containsKey(artifactId)){
			try {
				moduleContextListeners.get(artifactId).destroy();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		Set<String> rems = new HashSet<String>();
		
		for(String moduleName:moduleMap.keySet()){
			
			if(moduleMap.get(moduleName).getArtifactId().equals(artifactId)){
				mp = new String(moduleMap.get(moduleName).getModuleDirectoryPath());
				rems.add(moduleName);
				
			}
		}
		for(String rem:rems)
			moduleMap.remove(rem);
		
		if(loaders.containsKey(artifactId)){
			loaders.get(artifactId).destroy();
			loaders.remove(artifactId);
		}
		
		
        
		
		try {
			System.gc();
			if(mp!=null){
				FileUtils.deleteFile(mp+".zip");
				Thread.sleep(1000);
				FileUtils.deleteDirectory(mp);
			}
			
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
