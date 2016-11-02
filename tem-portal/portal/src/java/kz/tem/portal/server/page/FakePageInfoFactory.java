package kz.tem.portal.server.page;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class FakePageInfoFactory implements IPageInfoFactory{

	private static FakePageInfoFactory instance = null;
	
	public static FakePageInfoFactory getInstance(){
		if(instance==null){
			instance = new FakePageInfoFactory();
			instance.infoMap.clear();
			
			PageInfo pi1 = new PageInfo("page1", "kz.tem.portal.explorer.theme.Theme1", "kz.tem.portal.explorer.layout.Layout1");
			pi1.getModules().put("portlet1", new LinkedList<String>());
			pi1.getModules().get("portlet1").add("ftpclient");
			pi1.getModules().put("portlet2", new LinkedList<String>());
			pi1.getModules().get("portlet2").add("ftpclient");
			pi1.getModules().get("portlet2").add("ftpclient");
			instance.infoMap.put("pg/page1", pi1);
			
			PageInfo  pi2 = new PageInfo("page2", "kz.tem.portal.explorer.theme.Theme1", "kz.tem.portal.explorer.layout.Layout2");
			instance.infoMap.put("pg/page2", pi2);
		}
		return instance;
	}
	
	private Map<String, PageInfo> infoMap = new HashMap<String, PageInfo>();
	@Override
	public PageInfo getPageInfo(String url) throws Exception {
		System.out.println(url);
		if(!infoMap.containsKey(url))
			return infoMap.get("page1");
		return infoMap.get(url);
	}

}
