package kz.tem.portal.explorer.layout;

import java.util.HashMap;
import java.util.Map;

import kz.tem.portal.explorer.portlet.PortletContainer;
import kz.tem.portal.server.plugin.Module;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;

@SuppressWarnings("serial")
public class AbstractLayout extends Panel{

	private Map<String, RepeatingView> portletsMap = new HashMap<String, RepeatingView>();
	
	public AbstractLayout(String id) {
		super(id);
	}
	
	public void addPortletPosition(String name){
		RepeatingView view=  new RepeatingView(name);
		portletsMap.put(name, view);
		add(view);
	}

	public void addPortlet(String position, String module){
		RepeatingView rep =portletsMap.get(position);
		PortletContainer cnt = new PortletContainer(rep.newChildId(), module);
		rep.add(cnt);
		
	}
}
