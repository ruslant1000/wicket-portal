package kz.tem.portal.explorer.portlet;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;

import kz.tem.portal.explorer.application.PortalSession;
import kz.tem.portal.explorer.page.AbstractThemePage;
import kz.tem.portal.explorer.page.IComponentCreator;
import kz.tem.portal.server.model.Portlet;
import kz.tem.portal.server.plugin.Module;
import kz.tem.portal.server.plugin.ModuleConfig;
import kz.tem.portal.server.plugin.ModuleMeta;
import kz.tem.portal.server.plugin.engine.ModuleEngine;

/**
 * 
 * @author Ruslan Temirbulatov
 *
 */
@SuppressWarnings("serial")
public class PortletContainer extends Panel {

	private Module module;
	private String moduleName;
	private ModuleConfig config;
	private Portlet portlet;
	
	public PortletContainer(String id, String moduleName, ModuleConfig config,
			final Portlet portlet) {
		super(id);
		this.moduleName=moduleName;
		this.config=config;
		this.portlet=portlet;
		create();

	}

	private void create(){
try {
			
			if(!ModuleEngine.getInstance().getModuleMap().containsKey(moduleName))
				throw new Exception("Модуль не найден: "+moduleName);
			ModuleMeta meta = ModuleEngine.getInstance().getModuleMap()
					.get(moduleName);

			module = ModuleEngine.getInstance().init("module",
					meta, config);
			
			
			if(PortalSession.get().isSignedIn()){
			
				add(new AjaxLink<Void>("config") {
					@Override
					public void onClick(AjaxRequestTarget target) {
						((AbstractThemePage) getWebPage()).showModal(
								"modal window", target, new IComponentCreator() {
	
									@Override
									public Component create(String id)
											throws Exception {
										return new PortletSettingsPanel(id, module,
												portlet);
									}
								});
					}
				});
			}else{
				add(new WebMarkupContainer("config").setVisible(false));
			}
			
			
			try { 

				module.create();
//				add(new Label("module", "OK"));
				add(module);
			} catch (NoClassDefFoundError e) {
				e.printStackTrace();
				add(new Label("module", "модуль недоступен. Не найден класс " + e.getMessage()));
			} catch (RuntimeException e) {
				e.printStackTrace();
				add(new Label("module", "модуль недоступен. " + e.getMessage()));
			} catch (Exception e) {
				e.printStackTrace();
				add(new Label("module", "модуль недоступен. " + e.getMessage()));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			add(new Label("module", "модуль недоступен. " + ex.getMessage()));
			if(PortletContainer.this.get("config")==null){
				add(new WebMarkupContainer("config"));
			}
		}
	}
//	private void writeObject(ObjectOutputStream o) throws IOException {
//		System.out.println("write");
//		o.writeObject(moduleName);
//		o.writeObject(config);
//		o.writeObject(portlet);
//	}
//
//	private void readObject(ObjectInputStream o) throws IOException, ClassNotFoundException {
//		System.out.println("read");
//		moduleName = (String) o.readObject();
//		config = (ModuleConfig) o.readObject();
//		portlet = (Portlet) o.readObject();
//		create();
//	}
	
}
