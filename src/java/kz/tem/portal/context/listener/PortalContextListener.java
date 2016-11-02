package kz.tem.portal.context.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kz.tem.portal.server.plugin.engine.ModuleEngine;
import kz.tem.portal.server.plugin.engine.ModuleFinder;
 
//import org.slf4j.LoggerFactory;

public class PortalContextListener implements ServletContextListener{

	private Logger log = LoggerFactory.getLogger(PortalContextListener.class);
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		System.out.println("contextDestroyed");
//		LoggerFactory.getLogger("XXXX").info("sdsfsdsdfsdfgsd dfgdffsd");
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		System.out.println("contextInitialized");
		System.out.println(arg0.getServletContext().getRealPath("modules"));
		
		try {
			ModuleEngine.getInstance().loadModules(arg0.getServletContext().getRealPath("modules"));
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Не удалось загрузить все модули",e);
		}
//		LoggerFactory.getLogger("XXXX").info("sdsfsdfsd");
	}
	
	public static void main(String[] args) {
		LoggerFactory.getLogger("A").info("aaaa");
	}

}
