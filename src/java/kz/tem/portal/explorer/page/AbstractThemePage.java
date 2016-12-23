package kz.tem.portal.explorer.page;

import org.apache.wicket.Application;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.authroles.authentication.AuthenticatedWebApplication;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.spring.injection.annot.SpringBean;

import kz.tem.portal.PortalException;
import kz.tem.portal.api.ExplorerEngine;
import kz.tem.portal.explorer.application.PortalSession;
import kz.tem.portal.explorer.panel.admin.pages.PagesTable;
import kz.tem.portal.explorer.panel.common.component.popup.PopupMenu;
import kz.tem.portal.explorer.panel.common.component.repeater.StatelessRepeater;
import kz.tem.portal.explorer.panel.common.form.DefaultInputForm;
import kz.tem.portal.explorer.panel.common.form.DefaultInputStatelesForm;
import kz.tem.portal.explorer.panel.common.toolbar.IToolListener;
import kz.tem.portal.explorer.panel.login.LoginPanel;
import kz.tem.portal.explorer.theme.AbstractTheme;
import kz.tem.portal.server.model.Page;
import kz.tem.portal.server.register.IPageRegister;
import kz.tem.portal.server.register.IUserRegister;

/**
 * 
 * @author Ruslan Temirbulatov
 *
 */
@SuppressWarnings("serial")
public class AbstractThemePage extends WebPage{
	
	@SpringBean
	private IUserRegister userRegister;  

//	private IPageInfoFactory factory = FakePageInfoFactory.getInstance();
	@SpringBean
	private IPageRegister pageRegister;
	
	
	private Page info = null;
	
	@Override
	protected void onConfigure() {
		super.onConfigure();
		userRegister.defaults();
		
		
		
		
	}

	public AbstractThemePage(){

		dropOriginalDestination();
		
		String url = getRequestCycle().getRequest().getClientUrl().getPath();
		if(url.trim().length()==0){
			url = ExplorerEngine.getInstance().getSettingsValue(ExplorerEngine.SETTINGS_MAIN_PAGE);
		}
		if(url.startsWith("pg/"))
			url=url.substring(3);
		System.out.println(url);
		try {
			info = pageRegister.getPage(url);
		} catch (PortalException e) {
			e.printStackTrace();
			System.out.println("!!!!!!!!! xx");
			throw new RuntimeException(e);
		}
		
		AuthenticatedWebApplication app = (AuthenticatedWebApplication)Application.get();
		if(!AuthenticatedWebSession.get().isSignedIn() && !info.getPublicPage()){
			 throw new RestartResponseAtInterceptPageException(AuthenticatePage.class);
//	         app.restartResponseAtSignInPage();
		}
		
		if(!info.getPublicPage() && AuthenticatedWebSession.get().isSignedIn() && !PortalSession.get().access(info.getRole()))
			setResponsePage(AccessDeniedPage.class);
			
			
		String txt = "unknown";
		try {
			/*String url = getRequestCycle().getRequest().getClientUrl().getPath();
			if(url.trim().length()==0){
				url = ExplorerEngine.getInstance().getSettingsValue(ExplorerEngine.SETTINGS_MAIN_PAGE);
			}
			if(url.startsWith("pg/"))
				url=url.substring(3);
			System.out.println(url);
			Page info=  pageRegister.getPage(url);*/
			
			
			
			
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
//		add(new Label("info",txt));
//		add(new PagesTable("table"));
//		add(new DefaultInputForm("form"));
//		add(new DefaultInputStatelesForm("form2"));
		
		//**********************
		// модальное окно
		WebMarkupContainer modal = new WebMarkupContainer("modal");
		add(modal);
		modal.setOutputMarkupId(true);
//		modal.add(new AjaxLink<Void>("close") {
//
//			@Override
//			public void onClick(AjaxRequestTarget target) {
//				AbstractThemePage.this.get("modal").add(new AttributeModifier("style", "display:none"));
//				AbstractThemePage.this.get("modal").get("content").replaceWith(new WebMarkupContainer("content").setOutputMarkupId(true));
//				target.add(AbstractThemePage.this.get("modal"));
//				
//			}
//		});
		modal.add(new WebMarkupContainer("content").setOutputMarkupId(true));
		//**********************
		//**********************
		// Контекстное меню
		
//		PopupMenu popup = new PopupMenu("popup");
//		add(popup);
		
		//**********************
//		setVersioned(false);
		
		
	}
	
	public void showModal(String title,AjaxRequestTarget target, IComponentCreator creator){
		
		
		
		
		try {
			AbstractThemePage.this.get("modal").add(new AttributeModifier("style", "display:block"));
			AbstractThemePage.this.get("modal").get("content").replaceWith(creator.create("content").setOutputMarkupId(true));
			
			target.add(AbstractThemePage.this.get("modal"));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		
		
	}


	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
		response.render(OnDomReadyHeaderItem.forScript("Wicket.Event.subscribe('/ajax/call/beforeSend',function( attributes, jqXHR, settings ) {       $('#m_ajax_loading-animation').css('left','0px');$('#m_ajax_loading-animation').find('.loading-animation-mask').css('opacity','0.5');$('#m_ajax_loading-animation').find('.loading-animation-inner').css('opacity','1');        });"));
		response.render(OnDomReadyHeaderItem.forScript("Wicket.Event.subscribe('/ajax/call/complete',function( attributes, jqXHR, settings ) {       $('#m_ajax_loading-animation').css('left','-99999px');$('#m_ajax_loading-animation').find('.loading-animation-mask').css('opacity','0');$('#m_ajax_loading-animation').find('.loading-animation-inner').css('opacity','0');        });"));
	}
	
	
	
	public void dropOriginalDestination(){
		clearOriginalDestination();
	}
	
}
