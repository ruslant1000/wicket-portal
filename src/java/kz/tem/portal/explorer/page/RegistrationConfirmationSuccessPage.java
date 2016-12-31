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
public class RegistrationConfirmationSuccessPage extends AbstractThemePage{
	
	public static final String PAGE_RESPONCE_SUCCESS_URL = "registrationconfirmationsuccess";
	
	public RegistrationConfirmationSuccessPage(){
		super();
	}
	
	

}
