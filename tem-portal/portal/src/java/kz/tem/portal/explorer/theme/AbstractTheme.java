package kz.tem.portal.explorer.theme;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import kz.tem.portal.api.PortalEngine;
import kz.tem.portal.api.model.ThemeInfo;
import kz.tem.portal.explorer.application.PortalApplication;
import kz.tem.portal.explorer.layout.AbstractLayout;
import kz.tem.portal.server.model.Page;
import kz.tem.portal.server.register.IPageRegister;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.core.util.resource.UrlResourceStream;
import org.apache.wicket.markup.IMarkupCacheKeyProvider;
import org.apache.wicket.markup.IMarkupResourceStreamProvider;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.resource.IResourceStream;

@SuppressWarnings("serial")
public class AbstractTheme extends Panel implements IMarkupResourceStreamProvider, IMarkupCacheKeyProvider{

	private String MARKUP_CACHE_KEY="AbstractTheme";
	
	private ThemeInfo theme;
	
	@SpringBean
	private IPageRegister pageRegister;
	public AbstractTheme(String id, Page info) {
		super(id);
		theme = PortalEngine.getInstance().getExplorerEngine().getThemes().get(info.getTheme());
		MARKUP_CACHE_KEY = MARKUP_CACHE_KEY + info.getTheme();
		try {
			if(theme.isLayout()){
				AbstractLayout layout = new AbstractLayout("layout", info);
				add(layout);
			}
			if(theme.isMenu()){
				List<Page> tree = pageRegister.pagesTree();
				RepeatingView menu = new RepeatingView("menu");
				add(menu);
				for(Page p1:tree){
					Component m1 = new WebMarkupContainer(menu.newChildId());
					Label ln = null;
					if(theme.isMenuLink()){
						ln = new Label("menu-link",p1.getTitle());
						ln.add(new AttributeModifier("href", PortalApplication.toPortalUrl(p1) ));
						((WebMarkupContainer)m1).add(ln);
						if(info.getUrl().equals(p1.getUrl()))
							ln.add(new AttributeAppender("class", " selected-item"));
					}
					
					
					if(theme.isSubMenu()){
						RepeatingView submemu = new RepeatingView("sub-menu");
						((WebMarkupContainer)m1).add(submemu);
						for(Page p2:p1.getChilds()){
							WebMarkupContainer m2 =new WebMarkupContainer(submemu.newChildId());
							submemu.add(m2);
							if(theme.isSubMenuLink()){
								Label ln2 = new Label("sub-menu-link",p2.getTitle());
								ln2.add(new AttributeModifier("href", PortalApplication.toPortalUrl(p2) ));
								((WebMarkupContainer)m2).add(ln2);
								
								if(info.getUrl().equals(p2.getUrl()))
									ln.add(new AttributeAppender("class", " selected-item"));
							}
						}
						 
					}
					
					menu.add(m1);
					
					
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	@Override
	public String getCacheKey(MarkupContainer container, Class<?> containerClass) {
		return MARKUP_CACHE_KEY;
	}

	@Override
	public IResourceStream getMarkupResourceStream(MarkupContainer container,
			Class<?> containerClass) {
		System.out.println("!!!!!! getMarkupResourceStream "+theme.getName());
		URL url;
		try {
			url = WebApplication.get().getServletContext().getResource("themes/"+theme.getFileName());
			 if (url != null)
		        {
		            return new UrlResourceStream(url);
		        }
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
       
		return null;
	}
	

}
