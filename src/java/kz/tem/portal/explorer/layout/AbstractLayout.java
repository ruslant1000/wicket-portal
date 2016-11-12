package kz.tem.portal.explorer.layout;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import kz.tem.portal.PortalException;
import kz.tem.portal.api.PortalEngine;
import kz.tem.portal.api.model.LayoutInfo;
import kz.tem.portal.explorer.portlet.PortletContainer;
import kz.tem.portal.server.model.Page;
import kz.tem.portal.server.model.Portlet;
import kz.tem.portal.server.register.IPortletRegister;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.core.util.resource.UrlResourceStream;
import org.apache.wicket.markup.IMarkupCacheKeyProvider;
import org.apache.wicket.markup.IMarkupResourceStreamProvider;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.resource.IResourceStream;
/**
 * 
 * @author Ruslan Temirbulatov
 *
 */
@SuppressWarnings("serial")
public class AbstractLayout extends Panel implements IMarkupResourceStreamProvider, IMarkupCacheKeyProvider {

	private String MARKUP_CACHE_KEY="AbstractLayout";
	private Map<String, RepeatingView> portletsMap = new HashMap<String, RepeatingView>();
	
	private String layoutName;
	
	@SpringBean
	private IPortletRegister portletRegister;
	
	public AbstractLayout(String id, Page pageInfo) {
		super(id);
		MARKUP_CACHE_KEY=MARKUP_CACHE_KEY+pageInfo.getLayout();
//		System.out.println("~~~ "+pageInfo.getUrl());
//		try {
//			Thread.sleep(5000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		//**********************************
		// Это нудно для того, чтобы kz/tem/portal/explorer/layout/AbstractLayout не подгружал один и тот же HTML-layout (первый загруженный Layout).
//		WebApplication.get().getMarkupSettings().getMarkupFactory().getMarkupCache().removeMarkup(MARKUP_CACHE_KEY);
		//**********************************
		layoutName = pageInfo.getLayout();
		System.out.println(layoutName);
		if(!PortalEngine.getInstance().getExplorerEngine().getLayouts().containsKey(layoutName))
			layoutName="NotFoundLayout.html";
		
		LayoutInfo layout = PortalEngine.getInstance().getExplorerEngine().getLayouts().get(layoutName);
		if(layout.getLocations()!=null){
			for(String location:layout.getLocations())
				addPortletPosition(location);
		}
		
		
		try {
			for(Portlet p:portletRegister.table(0, 0, pageInfo.getId()).records()){
				addPortlet(p.getPosition(), p.getModuleName());
			}
		} catch (PortalException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
	}
	
	public void addPortletPosition(String name){
		System.out.println("#### addPortletPosition "+name);
		RepeatingView view=  new RepeatingView(name);
		portletsMap.put(name, view);
		add(view);
	}

	public void addPortlet(String position, String module){
		RepeatingView rep =portletsMap.get(position);
		PortletContainer cnt = new PortletContainer(rep.newChildId(), module);
		rep.add(cnt);
	}

	@Override
	public IResourceStream getMarkupResourceStream(MarkupContainer container,
			Class<?> containerClass) {
		System.out.println("!!!!!! getMarkupResourceStream "+layoutName);
		URL url;
		try {
			url = WebApplication.get().getServletContext().getResource("layouts/"+layoutName);
			 if (url != null)
		        {
		            return new UrlResourceStream(url);
		        }
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
       
		return null;
	}

	@Override
	public String getCacheKey(MarkupContainer container, Class<?> containerClass) {
		return MARKUP_CACHE_KEY;
	}
	
	
}
