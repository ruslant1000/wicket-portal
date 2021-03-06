package kz.tem.portal.explorer.page;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.flow.RedirectToUrlException;

import kz.tem.portal.explorer.application.PortalSession;
/**
 * 
 * @author Ruslan Temirbulatov
 *
 */
@SuppressWarnings("serial")
public class SignOutPage extends WebPage{

	public SignOutPage(){
		PortalSession.get().invalidate();
		throw new RedirectToUrlException("/"+RequestCycle.get().getRequest().getContextPath());
	}
}
