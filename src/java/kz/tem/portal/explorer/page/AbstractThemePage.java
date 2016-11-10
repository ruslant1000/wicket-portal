package kz.tem.portal.explorer.page;

import kz.tem.portal.explorer.panel.admin.pages.PagesTable;
import kz.tem.portal.explorer.panel.common.form.DefaultInputForm;
import kz.tem.portal.explorer.panel.common.form.DefaultInputStatelesForm;
import kz.tem.portal.explorer.theme.AbstractTheme;
import kz.tem.portal.server.model.Page;
import kz.tem.portal.server.register.IPageRegister;
import kz.tem.portal.server.register.IUserRegister;
import kz.tem.portal.server.register.impl.UserRegisterImpl;

import org.apache.wicket.Application;
import org.apache.wicket.authroles.authentication.AuthenticatedWebApplication;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.request.mapper.info.PageInfo;
import org.apache.wicket.spring.injection.annot.SpringBean;

@SuppressWarnings("serial")
public class AbstractThemePage extends WebPage{
	
	@SpringBean
	private IUserRegister userRegister;  

//	private IPageInfoFactory factory = FakePageInfoFactory.getInstance();
	@SpringBean
	private IPageRegister pageRegister;
	
	
	@Override
	protected void onConfigure() {
		super.onConfigure();
		userRegister.defaults();
		
		AuthenticatedWebApplication app = (AuthenticatedWebApplication)Application.get();
		if(!AuthenticatedWebSession.get().isSignedIn())
	         app.restartResponseAtSignInPage();
	}




	public AbstractThemePage(){
		//**********************************
		// Это нудно для того, чтобы kz/tem/portal/explorer/layout/AbstractLayout не подгружал один и тот же HTML-layout (первый загруженный Layout).
		
//		WebApplication.get().getMarkupSettings().getMarkupFactory().getMarkupCache().clear();
		//**********************************
		String txt = "unknown";
		try {
			String url = getRequestCycle().getRequest().getClientUrl().getPath();
			System.out.println(url);
			if(url.startsWith("pg/"))
				url=url.substring(3);
			System.out.println(url);
			Page info=  pageRegister.getPage(url);
//			PageInfo info = factory.getPageInfo(getRequestCycle().getRequest().getContextPath());
//			info = factory.getPageInfo(getRequestCycle().getRequest().getClientUrl().getFragment());
//			info = factory.getPageInfo(getRequestCycle().getRequest().getClientUrl().getPath());
			txt=info.getUrl()+"   "+info.getTheme()+"   "+info.getLayout();
			
//			AbstractTheme theme = (AbstractTheme)AbstractThemePage.class.forName(info.getTheme()).getConstructor(new Class[]{String.class,Page.class}).newInstance(new Object[]{"theme",info});
			AbstractTheme theme = new AbstractTheme("theme", info);
			add(theme);
		} catch (Exception e) {
			e.printStackTrace();
		}
		add(new Label("info",txt));
		add(new PagesTable("table"));
		add(new DefaultInputForm("form"));
		add(new DefaultInputStatelesForm("form2"));
		
	}
}
