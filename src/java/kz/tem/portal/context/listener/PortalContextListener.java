package kz.tem.portal.context.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.wicket.spring.SpringBeanLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import kz.tem.portal.server.plugin.engine.ModuleEngine;
 
//import org.slf4j.LoggerFactory;
/**
 * 
 * @author Ruslan Temirbulatov
 *
 */
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
			log.error("�� ������� ��������� ��� ������",e);
		}
		
		
//		LoggerFactory.getLogger("XXXX").info("sdsfsdfsd");
	}
	
	public static void main(String[] args) {
		LoggerFactory.getLogger("A").info("aaaa");
	}

}
