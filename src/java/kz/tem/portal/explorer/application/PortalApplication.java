package kz.tem.portal.explorer.application;

import kz.tem.portal.explorer.page.AbstractThemePage;
import kz.tem.portal.explorer.page.TestPage;

import org.apache.wicket.Page;
import org.apache.wicket.protocol.http.WebApplication;

public class PortalApplication extends WebApplication{

	
	@Override
	protected void init() {
		super.init();
		getResourceSettings().setResourceStreamLocator(new PortalStreamLocator());
		mountPage("pg/${seg1}", AbstractThemePage.class);
//		getResourceSettings().setResourceStreamLocator(new CustomResourceStreamLocator());
	}

	@Override
	public Class<? extends Page> getHomePage() {
		return AbstractThemePage.class;
	}


}
