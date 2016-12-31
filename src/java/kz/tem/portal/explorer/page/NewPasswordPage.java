package kz.tem.portal.explorer.page;

import kz.tem.portal.PortalException;
import kz.tem.portal.api.RegisterEngine;
import kz.tem.portal.explorer.application.PortalSession;
import kz.tem.portal.server.model.User;
import kz.tem.portal.server.model.UserSecretKey;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.flow.RedirectToUrlException;
import org.apache.wicket.util.string.StringValue;

@SuppressWarnings("serial")
public class NewPasswordPage extends WebPage{
	
	public static final String NEW_PASSWORD_SET_USER="NEW_PASSWORD_SET_USER";
	
	public static final String USER_SECRET_KEY_SET_NEW_PASSWORD = "Восстановление пароля";

	public static final String NEW_PASSWORD_PAGE_URL="newpassword";
	
	public NewPasswordPage(){
		super();
		StringValue svU = RequestCycle.get().getRequest().getRequestParameters().getParameterValue("u");
		StringValue svK = RequestCycle.get().getRequest().getRequestParameters().getParameterValue("k");
		try {
			// Специально делаем паузу, чтобы помешать позможному перебору ключей 
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		boolean ok = false;
		if(!(svU.isNull() || svU.isEmpty() || svK.isNull() || svK.isEmpty())){
			ok = RegistrationConfirmationPage.checkRegistrationConfirmation(svU.toString(),svK.toString(),USER_SECRET_KEY_SET_NEW_PASSWORD);
		}
		if(ok){
			
			try {
				Long userId = Long.parseLong(svU.toString());
				User user = RegisterEngine.getInstance().getUserRegister().getUserById(userId);
				RegisterEngine.getInstance().getUserRegister().deleteSecretKeyIfExists(user, USER_SECRET_KEY_SET_NEW_PASSWORD);
				PortalSession.get().setUser(user);
				throw new RedirectToUrlException("/"+NewPasswordSuccessPage.NEW_PASSWORD_SUCCESS_PAGE_URL);
				
			} catch (PortalException e) {
				e.printStackTrace();
				PortalSession.get().setUser(null);
				PortalSession.get().invalidate();
				throw new RedirectToUrlException("/"+NewPasswordErrorPage.NEW_PASSWORD_ERROR_PAGE_URL);
			}	
		}else
			throw new RedirectToUrlException("/"+NewPasswordErrorPage.NEW_PASSWORD_ERROR_PAGE_URL);
	}
	
	public static String newPasswordPageUrl(User user)throws Exception{
		RegisterEngine.getInstance().getUserRegister().deleteSecretKeyIfExists(user, USER_SECRET_KEY_SET_NEW_PASSWORD);
		UserSecretKey usk = RegisterEngine.getInstance().getUserRegister().createSecretKey(user, USER_SECRET_KEY_SET_NEW_PASSWORD);
		Url clientUrl = RequestCycle.get().getRequest().getClientUrl();
		return clientUrl.getProtocol()+"://"+clientUrl.getHost()+(clientUrl.getPort()==80?"":":"+clientUrl.getPort())+"/"+NEW_PASSWORD_PAGE_URL+"?u="+user.getId()+"&k="+usk.getValue();
		
	}
}
