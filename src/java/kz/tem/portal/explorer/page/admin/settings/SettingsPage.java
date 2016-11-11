package kz.tem.portal.explorer.page.admin.settings;

import kz.tem.portal.explorer.page.AdminPage;
import kz.tem.portal.explorer.panel.admin.settings.SettingsPanel;

@SuppressWarnings("serial")
public class SettingsPage extends AdminPage{

	public SettingsPage(){
		add(new SettingsPanel("settings"));
	}
}
