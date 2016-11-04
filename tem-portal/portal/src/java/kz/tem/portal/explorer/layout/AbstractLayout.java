package kz.tem.portal.explorer.layout;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import kz.tem.portal.api.PortalEngine;
import kz.tem.portal.api.model.LayoutInfo;
import kz.tem.portal.explorer.portlet.PortletContainer;
import kz.tem.portal.server.page.PageInfo;
import kz.tem.portal.server.plugin.Module;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.core.util.resource.UrlResourceStream;
import org.apache.wicket.markup.IMarkupCacheKeyProvider;
import org.apache.wicket.markup.IMarkupResourceStreamProvider;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.util.resource.IResourceStream;

@SuppressWarnings("serial")
public class AbstractLayout extends Panel implements IMarkupResourceStreamProvider, IMarkupCacheKeyProvider {

	private static String MARKUP_CACHE_KEY="AbstractLayout";
	private Map<String, RepeatingView> portletsMap = new HashMap<String, RepeatingView>();
	
	private String layoutName;
	
	public AbstractLayout(String id, PageInfo pageInfo) {
		super(id);
		//**********************************
		// Это нудно для того, чтобы kz/tem/portal/explorer/layout/AbstractLayout не подгружал один и тот же HTML-layout (первый загруженный Layout).
		WebApplication.get().getMarkupSettings().getMarkupFactory().getMarkupCache().removeMarkup(MARKUP_CACHE_KEY);
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
		
		for(String position:pageInfo.getModules().keySet()){
			System.out.println("### "+position);
			for(String module:pageInfo.getModules().get(position)){
				System.out.println("##### "+position+"  "+module);
				addPortlet(position, module);
			}
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
