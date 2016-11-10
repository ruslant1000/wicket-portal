package kz.tem.portal.server.model;


import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;



@SuppressWarnings("serial")
@Table(name="PT_PAGE")
@Entity
public class Page extends IdEntity implements Serializable{


	private String url;
	
	private String title;
	
	private String theme;
	
	private String layout;
	
//	private Long parentPageId;
	
	private Page parentPage;
	
	private List<Portlet> portlets = new LinkedList<Portlet>();
	
	private List<Page> childs = new LinkedList<Page>();
	
	@Column(unique=true, nullable=false)
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	@Column(nullable=false)
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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

	@OneToMany(fetch=FetchType.LAZY,cascade={CascadeType.ALL},mappedBy="page")
	public List<Portlet> getPortlets() {
		return portlets;
	}

	public void setPortlets(List<Portlet> portlets) {
		this.portlets = portlets;
	}
	
	@Transient
	public String toString(){
		return title+"("+url+")";
	}
	@ManyToOne(fetch=FetchType.EAGER, optional=true)
	public Page getParentPage() {
		return parentPage;
	}

	public void setParentPage(Page parentPage) {
		this.parentPage = parentPage;
	}
	@Transient
	public List<Page> getChilds() {
		return childs;
	}

	public void setChilds(List<Page> childs) {
		this.childs = childs;
	}
	
	
	
//	public Long getParentPageId() {
//		return parentPageId;
//	}
//
//	public void setParentPageId(Long parentPageId) {
//		this.parentPageId = parentPageId;
//	}
//	
	
}
