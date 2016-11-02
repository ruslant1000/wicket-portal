package kz.tem.portal.explorer.layout;

import kz.tem.portal.server.page.PageInfo;

@SuppressWarnings("serial")
public class Layout1 extends AbstractLayout{

	public Layout1(String id, PageInfo info) {
		super(id);
		addPortletPosition("portlet1");
		addPortletPosition("portlet2");
		
		for(String position:info.getModules().keySet()){
			for(String module:info.getModules().get(position))
				addPortlet(position, module);
		}
	}
	
	

}
