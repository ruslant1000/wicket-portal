package kz.tem.portal.explorer.theme.menu;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;


@SuppressWarnings("serial")
public class MenuItem implements Serializable{
	public String title;
	public String url;
	public boolean selected = false;
	public List<MenuItem> childs = new LinkedList<MenuItem>();
	
	public MenuItem(String title,String url, boolean selected){
		this.title=title;
		this.url=url;
		this.selected=selected;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public List<MenuItem> getChilds() {
		return childs;
	}

	public void setChilds(List<MenuItem> childs) {
		this.childs = childs;
	}
	
	
}
