package kz.tem.portal.explorer.page;

import kz.tem.portal.PortalException;
import kz.tem.portal.api.ExplorerEngine;
import kz.tem.portal.explorer.application.PortalSession;
import kz.tem.portal.explorer.theme.AbstractTheme;
import kz.tem.portal.server.model.Page;
import kz.tem.portal.server.register.IPageRegister;
import kz.tem.portal.server.register.IUserRegister;

import org.apache.wicket.Application;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authroles.authentication.AuthenticatedWebApplication;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.http.WebResponse;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * 
 * @author Ruslan Temirbulatov
 *
 */
@SuppressWarnings("serial")
public class AbstractThemePage extends WebPage{
	
	
	
	public static final String PAGE_404 = "404";
	
	@SpringBean
	private IUserRegister userRegister;  

//	private IPageInfoFactory factory = FakePageInfoFactory.getInstance();
	@SpringBean
	private IPageRegister pageRegister;
	
	
	private Page info = null;
	
	@Override
	protected void onConfigure() {
		super.onConfigure();
//		userRegister.defaults();
		
	}


	
	
	
	public AbstractThemePage(){

		dropOriginalDestination();
		
		String url = getRequestCycle().getRequest().getClientUrl().getPath();
		if(url.trim().length()==0){
			url = ExplorerEngine.getInstance().getSettingsValue(ExplorerEngine.SETTINGS_MAIN_PAGE);
		}
		if(url.startsWith("pg/"))
			url=url.substring(3);
		try {
			info = pageRegister.getPage(url);
		} catch (PortalException e) {
			if(e.getKey().equals(PortalException.NOT_FOUND)){
				try {
					info = pageRegister.getPage(PAGE_404);
				} catch (PortalException e1) {
					e1.printStackTrace();
					throw new RuntimeException(e);
				}	
			}else{
				e.printStackTrace();
				throw new RuntimeException(e);
			}
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

			txt=info.getUrl()+"   "+info.getTheme()+"   "+info.getLayout();
			
			AbstractTheme theme = new AbstractTheme("theme", info);
			add(theme);
		} catch (Exception e) {
			e.printStackTrace();
		}

		
		//**********************
		// модальное окно
		WebMarkupContainer modal = new WebMarkupContainer("modal");
		add(modal);
		modal.add(new AttributeModifier("style", "display:none;"));
		modal.setOutputMarkupId(true);
		modal.add(new WebMarkupContainer("content").setOutputMarkupId(true));
		//**********************
		//**********************
		// Контекстное меню
		
//		PopupMenu popup = new PopupMenu("popup");
//		add(popup);
		
		//**********************
//		setVersioned(false);
		
		
		WebMarkupContainer adminPage = new WebMarkupContainer("admin-page");
		adminPage.add(new AttributeModifier("href", "/admin/pages"));
		if(!PortalSession.isAdmin()){
			adminPage.setVisible(false);
		}
		add(adminPage);
		
		
		
	}
	
	
	
	public void closeModal(){
		WebMarkupContainer wmc = new WebMarkupContainer("content");
		wmc.setOutputMarkupId(true);
		AbstractThemePage.this.get("modal").get("content").replaceWith(wmc);
		AbstractThemePage.this.get("modal").add(new AttributeModifier("style", "display:none;"));
	}
	public void closeModal(AjaxRequestTarget target){
		closeModal();
		target.add(AbstractThemePage.this.get("modal"));
		
		
	}
	
	public void showModal(String title,AjaxRequestTarget target, IComponentCreator creator){
		
		try {
			AbstractThemePage.this.get("modal").add(new AttributeModifier("style", "display:block;"));
			AbstractThemePage.this.get("modal").get("content").replaceWith(creator.create("content").setOutputMarkupId(true));
			target.add(AbstractThemePage.this.get("modal"));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
	}

	
	

	@Override
	protected void onInitialize() {
		super.onInitialize();
	}





	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
		response.render(OnDomReadyHeaderItem.forScript("Wicket.Event.subscribe('/ajax/call/beforeSend',function( attributes, jqXHR, settings ) {       $('#m_ajax_loading-animation').css('left','0px');$('#m_ajax_loading-animation').find('.loading-animation-mask').css('opacity','0.5');$('#m_ajax_loading-animation').find('.loading-animation-inner').css('opacity','1');        });"));
		response.render(OnDomReadyHeaderItem.forScript("Wicket.Event.subscribe('/ajax/call/complete',function( attributes, jqXHR, settings ) {       $('#m_ajax_loading-animation').css('left','-99999px');$('#m_ajax_loading-animation').find('.loading-animation-mask').css('opacity','0');$('#m_ajax_loading-animation').find('.loading-animation-inner').css('opacity','0');        });"));
	}
	
	
	/**
	 * 
	 */
	public void dropOriginalDestination(){
		clearOriginalDestination();
	}

	
	




	
	
}
