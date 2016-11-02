package kz.tem.portal.explorer.page;

import kz.tem.portal.server.plugin.Module;
import kz.tem.portal.server.plugin.ModuleMeta;
import kz.tem.portal.server.plugin.engine.ModuleEngine;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.panel.Panel;

@SuppressWarnings("serial")
public class TestPanel extends Panel{

	public TestPanel(String id) {
		super(id);
		
		final ModuleMeta meta = ModuleEngine.getInstance().getModuleMap().values()
				.iterator().next();
		Module module;
		try {
			module = ModuleEngine.getInstance().create("xxx",meta);
			add(module);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		AjaxLink link = new AjaxLink<Void>("ccc") {

			@Override
			public void onClick(AjaxRequestTarget target) {
				ModuleEngine.getInstance().undeploy(meta.getModuleName());
				try {
					Thread.sleep(30000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				ModuleEngine.getInstance().loadNewModules();
				
			}
		};		
		add(link);
	}

}
