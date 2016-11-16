package kz.tem.portal.explorer.page;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.panel.Panel;

import kz.tem.portal.server.plugin.Module;
import kz.tem.portal.server.plugin.ModuleMeta;
import kz.tem.portal.server.plugin.engine.ModuleEngine;

@SuppressWarnings("serial")
public class TestPanel extends Panel{

	public TestPanel(String id) {
		super(id);
		
		final ModuleMeta meta = ModuleEngine.getInstance().getModuleMap().values()
				.iterator().next();
		Module module;
		try {
			module = ModuleEngine.getInstance().init("xxx",meta,null);
			module.create();
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
