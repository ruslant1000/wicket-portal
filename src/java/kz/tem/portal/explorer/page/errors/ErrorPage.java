package kz.tem.portal.explorer.page.errors;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.link.Link;

import kz.tem.portal.explorer.application.PortalApplication;
import kz.tem.portal.explorer.application.PortalSession;
import kz.tem.portal.explorer.page.AbstractThemePage;

@SuppressWarnings("serial")
public class ErrorPage extends WebPage {
	
	public static final String PAGE_ERROR_URL = "error";
	
	
	public ErrorPage() {
		super();
		
		add(new Link<Void>("back") {

			@Override
			public void onClick() {
				
			}
		});
	}

//	@Override
//	public void dropOriginalDestination() {
//	}
}
