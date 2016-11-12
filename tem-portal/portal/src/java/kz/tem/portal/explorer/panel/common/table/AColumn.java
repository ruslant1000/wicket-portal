package kz.tem.portal.explorer.panel.common.table;

import java.io.Serializable;

import org.apache.wicket.Component;
/**
 * 
 * @author Ruslan Temirbulatov
 *
 * @param <T>
 */
@SuppressWarnings("serial")
public abstract class AColumn<T> implements Serializable{
	
	private String title;
	private String name;
	
	public AColumn(String title, String name) {
		super();
		this.title = title;
		this.name = name;
	}
	public AColumn(String title) {
		super();
		this.title = title;
	}
	
	public abstract Component cell(String id, T record)throws Exception;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	

}
