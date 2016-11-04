package kz.tem.portal.explorer.application;

import kz.tem.portal.api.PortalEngine;
import kz.tem.portal.explorer.page.AbstractThemePage;
import kz.tem.portal.explorer.page.TestPage;

import org.apache.wicket.Page;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;

public class PortalApplication extends WebApplication{

	
	@Override
	protected void init() {
		super.init();
		getComponentInstantiationListeners().add(new SpringComponentInjector(this));
		getResourceSettings().setResourceStreamLocator(new PortalStreamLocator());
		
		PortalEngine.getInstance().getExplorerEngine().initLayouts(this);
		
		mountPage("pg/${seg1}", AbstractThemePage.class);
//		getResourceSettings().setResourceStreamLocator(new CustomResourceStreamLocator());
	}

	@Override
	public Class<? extends Page> getHomePage() {
		return AbstractThemePage.class;
	}


}
