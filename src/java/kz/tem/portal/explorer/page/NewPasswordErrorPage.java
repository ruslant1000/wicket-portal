package kz.tem.portal.explorer.page;

import kz.tem.portal.api.RegisterEngine;
import kz.tem.portal.server.model.User;
import kz.tem.portal.server.model.UserSecretKey;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.flow.RedirectToUrlException;
import org.apache.wicket.util.string.StringValue;

@SuppressWarnings("serial")
public class NewPasswordErrorPage extends AbstractThemePage{
	
	public static final String NEW_PASSWORD_ERROR_PAGE_URL="newpassworderror";
	
	public NewPasswordErrorPage(){
		super();
		
	}
	
	
}
