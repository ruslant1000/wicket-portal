package kz.tem.portal.server.plugin.engine;

import java.io.File;


public class ModuleEngine {
	
	private TestBean execute = null;
	JarClassLoader loader2=null;
	public ModuleEngine(String modulePath){
		
	    /**
	     * Создаем загрузчик модулей.
	     */
//	    ModuleLoader loader = new ModuleLoader(modulePath, ClassLoader.getSystemClassLoader());
	    
	    loader2 = new JarClassLoader(modulePath);
	    
	    /**
	     * Получаем список доступных модулей.
	     */
//	    File dir = new File(modulePath);
//	    String[] modules = dir.list();
	    
	    try{
	    	Class clazz = loader2.loadClass("kz.xxx.module.ModulePrinter");
	    	execute = (TestBean) clazz.newInstance(); 
//	    	execute.load();
//	    	execute.run();
//	    	execute.unload();
	    }catch(Exception ex){
	    	ex.printStackTrace();
	    }
	    
	    /**
	     * Загружаем и исполняем каждый модуль. 
	     */
//	    for (String module: modules) {
//	    	System.out.println(module);
//	      try {
//	        String moduleName = module.split(".class")[0];
//	        Class clazz = loader2.loadClass(moduleName);
//	        Module execute = (Module) clazz.newInstance(); 
//	        
//	        execute.load();
//	        execute.run();
//	        execute.unload();
//	        
//	      } catch (ClassNotFoundException e) {
//	        e.printStackTrace();
//	      } catch (InstantiationException e) {
//	        e.printStackTrace();
//	      } catch (IllegalAccessException e) {
//	        e.printStackTrace();
//	      }
//	    }
		
		
	}
	
	public static void main(String args[]) {
		ModuleEngine m2 = new ModuleEngine("E:/projects/xxx2");
		ModuleEngine m1 = new ModuleEngine("E:/projects/xxx");
	    m2.execute.load();
	    m2.execute.load();
	    m1.execute.load();
	    m2.execute.load();
	    m1.execute.load();
	    m2.execute.load();
	    m1.execute.load();
	    
	    m2.execute.load();
	    m1.execute.load();
	    m2.execute.load();
	    m1.execute.load();
	    m2.execute.load();
	    m1.execute.load();
	    m2.execute.load();
	    m1.execute.load();
	    m2.execute.load();
	    m1.execute.load();
	    m2.execute.load();
	    m1.execute.load();
	    m2.execute.load();
	    m1.execute.load();
	    m2.execute.load();
	    m1.execute.load();
	    m2.execute.load();
	    m1.execute.load();
	    m2.execute.load();
	    m1.execute.load();
	    m2.execute.load();
	    
	    m1.loader2.clear();
	    m1.execute.load();
    }
}
