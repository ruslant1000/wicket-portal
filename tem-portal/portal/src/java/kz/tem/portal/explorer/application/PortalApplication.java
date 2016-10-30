package kz.tem.portal.explorer.application;

import org.apache.wicket.Page;
import org.apache.wicket.protocol.http.WebApplication;

import kz.tem.portal.explorer.page.TestPage;

public class PortalApplication extends WebApplication{

	@Override
	public Class<? extends Page> getHomePage() {
		
		return TestPage.class;
	}

}
