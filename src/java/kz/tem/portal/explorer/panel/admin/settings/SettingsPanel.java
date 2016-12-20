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
	private String smtpHost = null;
	private String smtpPort = null;
	private String smtpUser = null;
	private String smtpPassword = null;

	public SettingsPanel(String id) {
		super(id);
		mainPage = ExplorerEngine.getInstance().getSettingsValue(ExplorerEngine.SETTINGS_MAIN_PAGE);
		smtpHost = ExplorerEngine.getInstance().getSettingsValue(ExplorerEngine.SETTINGS_SMTP_HOST);
		smtpPort = ExplorerEngine.getInstance().getSettingsValue(ExplorerEngine.SETTINGS_SMTP_PORT);
		smtpUser = ExplorerEngine.getInstance().getSettingsValue(ExplorerEngine.SETTINGS_SMTP_USER);
		smtpPassword = ExplorerEngine.getInstance().getSettingsValue(ExplorerEngine.SETTINGS_SMTP_PASSWORD);
		DefaultInputForm form = new DefaultInputForm("form"){

			@Override
			public void onSubmit() throws Exception {
				super.onSubmit();
				Map<String, String> settings = new HashMap<String, String>();
				settings.put(ExplorerEngine.SETTINGS_MAIN_PAGE, mainPage);
				settings.put(ExplorerEngine.SETTINGS_SMTP_HOST, smtpHost);
				settings.put(ExplorerEngine.SETTINGS_SMTP_PORT, smtpPort);
				settings.put(ExplorerEngine.SETTINGS_SMTP_USER, smtpUser);
				settings.put(ExplorerEngine.SETTINGS_SMTP_PASSWORD, smtpPassword);
				settingsRegister.saveAllSettings(settings);
				ExplorerEngine.getInstance().loadSettings();
 				for(String key:ExplorerEngine.getInstance().getSettings().keySet()){
					System.out.println(key+" = "+ExplorerEngine.getInstance().getSettings().get(key));
				}		
			}
			
		};
		form.addFieldString(ExplorerEngine.SETTINGS_MAIN_PAGE, new PropertyModel<String>(SettingsPanel.this, "mainPage"), false);
		form.addFieldString(ExplorerEngine.SETTINGS_SMTP_HOST, new PropertyModel<String>(SettingsPanel.this, "smtpHost"), false);
		form.addFieldString(ExplorerEngine.SETTINGS_SMTP_PORT, new PropertyModel<String>(SettingsPanel.this, "smtpPort"), false);
		form.addFieldString(ExplorerEngine.SETTINGS_SMTP_USER, new PropertyModel<String>(SettingsPanel.this, "smtpUser"), false);
		form.addFieldString(ExplorerEngine.SETTINGS_SMTP_PASSWORD, new PropertyModel<String>(SettingsPanel.this, "smtpPassword"), false);
		
		add(form);
		
	}

}
