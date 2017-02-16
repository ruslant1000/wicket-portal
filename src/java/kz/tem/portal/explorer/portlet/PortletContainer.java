package kz.tem.portal.explorer.portlet;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.cycle.RequestCycle;

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
		setOutputMarkupId(true);

	}
	
	

	@Override
	protected void onInitialize() {
		super.onInitialize();
		create();
	}


	private void createConfig(){
		if(PortletContainer.this.get("config")!=null){
			PortletContainer.this.remove("config");
		}
		if(PortalSession.get().isAdmin()){
			
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
	}

	private void create(){
		try {
			
			if(!ModuleEngine.getInstance().getModuleMap().containsKey(moduleName))
				throw new Exception("Модуль не найден: "+moduleName);
			ModuleMeta meta = ModuleEngine.getInstance().getModuleMap()
					.get(moduleName);

			module = ModuleEngine.getInstance().init("module",
					meta, config);
			
			
			
			createConfig();
			
			try { 
				
				
				module.create();
//				add(new Label("module", "OK"));
				add(module);
			} catch (NoClassDefFoundError e) {
				moduleError(e);
			} catch (RuntimeException e) {
				moduleError(e);
			} catch (Exception e) {
				moduleError(e);
			}  catch (Throwable e) {
				moduleError(e);
			}
		} catch (Exception ex) {
			moduleError(ex);
			if(PortletContainer.this.get("config")==null){
				add(new WebMarkupContainer("config"));
			}
		}
	}
	
	public void moduleError(Throwable ex){
		if(PortletContainer.this.get("module")!=null){
			PortletContainer.this.remove("module");
		}
		add(new Label("module", "модуль недоступен. " + (ex==null?"":ex.getMessage())));
	}

	public static void showError(MarkupContainer current, Throwable ex){
		MarkupContainer t = current.getParent();
		if(t == null)
			return;
		if(t instanceof PortletContainer){
			PortletContainer pc = (PortletContainer) t;
			pc.moduleError(ex);
			AjaxRequestTarget art = RequestCycle.get().find(AjaxRequestTarget.class);
			if(art!=null){
				art.add(pc);
			}
		}else
			showError(t,ex);
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
