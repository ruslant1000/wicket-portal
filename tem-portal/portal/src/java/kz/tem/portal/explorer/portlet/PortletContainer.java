package kz.tem.portal.explorer.portlet;

import kz.tem.portal.server.plugin.Module;
import kz.tem.portal.server.plugin.ModuleMeta;
import kz.tem.portal.server.plugin.engine.ModuleEngine;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
/**
 * 
 * @author Ruslan Temirbulatov
 *
 */
@SuppressWarnings("serial")
public class PortletContainer extends Panel{

	public PortletContainer(String id, String moduleName) {
		super(id);
		ModuleMeta meta = ModuleEngine.getInstance().getModuleMap().get(moduleName);
		
		Module module;
		try {
			module = ModuleEngine.getInstance().create("module",meta);
			add(module);
		} catch (Exception e) {
			e.printStackTrace();
			add(new Label("module","Модуль недоступен"));
		}
		
	}

	
}
