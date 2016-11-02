package kz.tem.portal.server.page;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("serial")
public class PageInfo implements Serializable{
 
	private String url;
	
	private String theme;
	
	private String layout;
	
	private Map<String, List<String>> modules = new HashMap<String, List<String>>();

	public PageInfo(String url, String theme, String layout) {
		super();
		this.url = url;
		this.theme = theme;
		this.layout = layout;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

	public String getLayout() {
		return layout;
	}

	public void setLayout(String layout) {
		this.layout = layout;
	}

	public Map<String, List<String>> getModules() {
		return modules;
	}

	public void setModules(Map<String, List<String>> modules) {
		this.modules = modules;
	}
	
	
	
}
