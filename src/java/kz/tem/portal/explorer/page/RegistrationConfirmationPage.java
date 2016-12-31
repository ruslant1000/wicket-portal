package kz.tem.portal.explorer.page;

import kz.tem.portal.api.RegisterEngine;
import kz.tem.portal.server.model.User;
import kz.tem.portal.server.model.UserSecretKey;

import org.apache.log4j.Logger;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.RequestUtils;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.flow.RedirectToUrlException;
import org.apache.wicket.util.string.StringValue;

@SuppressWarnings("serial")
public class RegistrationConfirmationPage extends WebPage{
	
	private static Logger log = Logger.getLogger(RegistrationConfirmationPage.class);
	
	public static final String USER_SECRET_KEY_CONFIRM_REGISTRATION_EMAIL = "Подтверждение регистрации";
	
	public static final String PAGE_REQUEST_URL = "registrationconfirmation";
//	public static final String PAGE_RESPONCE_ERROR_URL = "registrationconfirmationerror";
//	public static final String PAGE_RESPONCE_SUCCESS_URL = "registrationconfirmationsuccess";
	
	public RegistrationConfirmationPage(){
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
			ok = checkRegistrationConfirmation(svU.toString(),svK.toString(),USER_SECRET_KEY_CONFIRM_REGISTRATION_EMAIL);
		}else{
			log.info("In confirmation url U or K is empty");
		}
		if(ok)
			throw new RedirectToUrlException("/"+RegistrationConfirmationSuccessPage.PAGE_RESPONCE_SUCCESS_URL);
		else
			throw new RedirectToUrlException("/"+RegistrationConfirmationErrorPage.PAGE_RESPONCE_ERROR_URL);
	}
	
	public static boolean checkRegistrationConfirmation(String u,String k, String keyName){
		try{
			log.info("checkRegistrationConfirmation: u="+u+"; k="+k+".");
			Long userId = Long.parseLong(u);
			User user = RegisterEngine.getInstance().getUserRegister().getUserById(userId);
			if(user==null){
				log.info("Not found user by id: "+userId);
				return false;
			}
			
			return RegisterEngine.getInstance().getUserRegister().checkSecretKey(user, keyName, k);
		}catch(Exception ex){
			ex.printStackTrace();
			return false;
		}
	}
	
	public static String newRegistrationConfirmationUrl(User user)throws Exception{
		RegisterEngine.getInstance().getUserRegister().deleteSecretKeyIfExists(user, USER_SECRET_KEY_CONFIRM_REGISTRATION_EMAIL);
		UserSecretKey usk = RegisterEngine.getInstance().getUserRegister().createSecretKey(user, USER_SECRET_KEY_CONFIRM_REGISTRATION_EMAIL);
		Url clientUrl = RequestCycle.get().getRequest().getClientUrl();
		return clientUrl.getProtocol()+"://"+clientUrl.getHost()+(clientUrl.getPort()==80?"":":"+clientUrl.getPort())+"/"+PAGE_REQUEST_URL+"?u="+user.getId()+"&k="+usk.getValue();
		
	}

}
