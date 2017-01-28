package kz.tem.portal.explorer.portlet;

import java.util.Arrays;
import java.util.List;

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

	private List<String> booleanChoices = Arrays.asList("Yes","No");
	
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

				@Override
				public void build() throws Exception {
					super.build();
					for(final String name:module.getModuleConfig().getNames()){
						if(module.getModuleConfig().getValues().containsKey(name)){
							addFieldString(name, new IModel<String>() {
								
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
						}else if(module.getModuleConfig().getBooles().containsKey(name)){
							addCombobox(name, new IModel<String>() {

								@Override
								public void detach() {
									// TODO Auto-generated method stub
									
								}

								@Override
								public String getObject() {
									if(module.getModuleConfig().getBooles().get(name).equals("true"))
										return booleanChoices.get(0);
									else
										return booleanChoices.get(1);
								}

								@Override
								public void setObject(String object) {
									boolean val = (object!=null && object.equals(booleanChoices.get(0)));
									module.getModuleConfig().getBooles().put(name, ""+val);
									
								}
							}, booleanChoices, false);
						}
					}
				}
				
			};
			add(form);
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

}
