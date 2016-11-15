package kz.tem.portal.explorer.page;

import org.apache.wicket.markup.html.WebPage;

import kz.tem.portal.explorer.panel.login.LoginPanel;

/**
 * 
 * @author Ruslan Temirbulatov
 *
 * Страница для аутентификации. После успешной аутентификации открывается запрашиваемая страница.
 */
@SuppressWarnings("serial")
public class AuthenticatePage extends WebPage {

	
	public AuthenticatePage(){
		add(new LoginPanel("login"){

			@Override
			public void onLogin() {
				super.onLogin();
				continueToOriginalDestination();
			}
			
		});	
		
	}
	
	
	
	
}
