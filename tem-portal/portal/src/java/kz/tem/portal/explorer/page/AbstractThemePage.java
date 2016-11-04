package kz.tem.portal.explorer.page;

import kz.tem.portal.explorer.panel.admin.pages.PagesTable;
import kz.tem.portal.explorer.panel.common.form.DefaultInputForm;
import kz.tem.portal.explorer.theme.AbstractTheme;
import kz.tem.portal.server.page.FakePageInfoFactory;
import kz.tem.portal.server.page.IPageInfoFactory;
import kz.tem.portal.server.page.PageInfo;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.protocol.http.WebApplication;

@SuppressWarnings("serial")
public class AbstractThemePage extends WebPage{

	private IPageInfoFactory factory = FakePageInfoFactory.getInstance();
	
	public AbstractThemePage(){
		//**********************************
		// Это нудно для того, чтобы kz/tem/portal/explorer/layout/AbstractLayout не подгружал один и тот же HTML-layout (первый загруженный Layout).
		
//		WebApplication.get().getMarkupSettings().getMarkupFactory().getMarkupCache().clear();
		//**********************************
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
		add(new PagesTable("table"));
		/*add(new DefaultInputForm("form"));*/
		
	}
}
