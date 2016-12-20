package kz.tem.portal.explorer.page;

import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.flow.RedirectToUrlException;

import kz.tem.portal.explorer.panel.login.LoginPanel;
/**
 * 
 * @author Ruslan Temirbulatov
 * 
 * Страница для аутентификации. После успешной аутентификации открывается главная страница портала.
 */
@SuppressWarnings("serial")
public class LoginPage extends WebPage {


	
	public LoginPage(){
		if(AuthenticatedWebSession.get().isSignedIn())
			throw new RedirectToUrlException("/"+RequestCycle.get().getRequest().getContextPath());
		
		add(new LoginPanel("login"){

			@Override
			public void onLogin() {
				super.onLogin();
				throw new RedirectToUrlException("/"+RequestCycle.get().getRequest().getContextPath());
			}
			
		});
	}
	
	
	
}
