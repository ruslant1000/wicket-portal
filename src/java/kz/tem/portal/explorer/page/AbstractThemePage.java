package kz.tem.portal.explorer.page;

import kz.tem.portal.explorer.theme.AbstractTheme;
import kz.tem.portal.server.page.FakePageInfoFactory;
import kz.tem.portal.server.page.IPageInfoFactory;
import kz.tem.portal.server.page.PageInfo;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;

@SuppressWarnings("serial")
public class AbstractThemePage extends WebPage{

	private IPageInfoFactory factory = FakePageInfoFactory.getInstance();
	
	public AbstractThemePage(){
		String txt = "unknown";
		try {
			PageInfo info = factory.getPageInfo(getRequestCycle().getRequest().getContextPath());
			info = factory.getPageInfo(getRequestCycle().getRequest().getClientUrl().getFragment());
			info = factory.getPageInfo(getRequestCycle().getRequest().getClientUrl().getPath());
			txt=info.getUrl()+"   "+info.getTheme()+"   "+info.getLayout();
			
			AbstractTheme theme = (AbstractTheme)AbstractThemePage.class.forName(info.getTheme()).getConstructor(new Class[]{String.class,PageInfo.class}).newInstance(new Object[]{"theme",info});
			add(theme);
		} catch (Exception e) {
			e.printStackTrace();
		}
		add(new Label("info",txt));
		
		
	}
}
