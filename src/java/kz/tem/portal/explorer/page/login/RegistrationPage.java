package kz.tem.portal.explorer.page.login;

import kz.tem.portal.explorer.panel.login.RegistrationPanel;

import org.apache.wicket.markup.html.WebPage;

@SuppressWarnings("serial")
public class RegistrationPage extends WebPage{
	
	public RegistrationPage(){
		add(new RegistrationPanel("regpanel"));
	}

}
