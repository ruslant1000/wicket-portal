package kz.tem.portal.explorer.portlet;

import kz.tem.portal.PortalException;
import kz.tem.portal.explorer.panel.common.form.DefaultInputForm;
import kz.tem.portal.server.model.Portlet;
import kz.tem.portal.server.plugin.Module;
import kz.tem.portal.server.plugin.ModuleConfig;
import kz.tem.portal.server.register.IPortletRegister;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

@SuppressWarnings("serial")
public class PortletSettingsPanel extends Panel{
	
	@SpringBean
	private IPortletRegister portletRegister;

	public PortletSettingsPanel(String id,final Module module,final Portlet portlet) {
		super(id);
		add(new Label("name","Configurations of "+portlet.getModuleName()));
		try {
			
			DefaultInputForm form = new DefaultInputForm("form"){

				@Override
				public void onSubmit() throws Exception {
					super.onSubmit();
					portletRegister.updatePortletSettings(portlet.getId(), module.getModuleConfig());
				}
				
			};
			add(form);
			for(final String name:module.getModuleConfig().getNames()){
				form.addFieldString(name, new IModel<String>() {
					
					@Override
					public void detach() {
						
					}
					
					@Override
					public void setObject(String object) {
						module.getModuleConfig().getValues().put(name, object);
						
					}
					
					@Override
					public String getObject() {
						return module.getModuleConfig().getValues().get(name);
					}
				}, false);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

}
