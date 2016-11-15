package kz.tem.portal.explorer.panel.admin.settings;

import java.util.HashMap;
import java.util.Map;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import kz.tem.portal.api.ExplorerEngine;
import kz.tem.portal.explorer.panel.common.form.DefaultInputForm;
import kz.tem.portal.server.register.ISettingsRegister;
/**
 * 
 * @author Ruslan Temirbulatov
 *
 */
@SuppressWarnings("serial")
public class SettingsPanel extends Panel{
	
	@SpringBean
	private ISettingsRegister settingsRegister;
	
	private String mainPage = null;

	public SettingsPanel(String id) {
		super(id);
		mainPage = ExplorerEngine.getInstance().getSettingsValue(ExplorerEngine.SETTINGS_MAIN_PAGE);
		DefaultInputForm form = new DefaultInputForm("form"){

			@Override
			public void onSubmit() throws Exception {
				super.onSubmit();
				Map<String, String> settings = new HashMap<String, String>();
				settings.put(ExplorerEngine.SETTINGS_MAIN_PAGE, mainPage);
				settingsRegister.saveAllSettings(settings);
				ExplorerEngine.getInstance().loadSettings();
 				for(String key:ExplorerEngine.getInstance().getSettings().keySet()){
					System.out.println(key+" = "+ExplorerEngine.getInstance().getSettings().get(key));
				}		
			}
			
		};
		form.addFieldString(ExplorerEngine.SETTINGS_MAIN_PAGE, new PropertyModel<String>(SettingsPanel.this, "mainPage"), false);
		
		add(form);
		
	}

}
