package kz.tem.portal.server.model;


import java.io.Serializable;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import kz.tem.portal.server.model.enums.EnumPageType;

import org.hibernate.envers.Audited;


/**
 * 
 * @author Ruslan Temirbulatov
 *
 */
@SuppressWarnings("serial")
@Table(name="PT_PAGE")
@Entity
@Audited
public class Page extends IdEntity implements Serializable{

	private String url;
	
	private String title;
	
	private String theme;
	
	private String layout;
	
//	private Long parentPageId;
	
	private Page parentPage;
	
	private List<Portlet> portlets = new LinkedList<Portlet>();
	
	private List<Page> childs = new LinkedList<Page>();
	
	private boolean publicPage =false;
	
	private boolean menu = true;
	
	private Set<Role> role = new HashSet<Role>();
	
	private EnumPageType pageType = null; 
	
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

	public boolean getPublicPage() {
		return publicPage;
	}

	public void setPublicPage(boolean publicPage) {
		this.publicPage = publicPage;
	}
	
	
	public boolean getMenu() {
		return menu;
	}

	public void setMenu(boolean menu) {
		this.menu = menu;
	}

	@ManyToMany(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	@JoinTable(name="PT_PAGE_ROLE", joinColumns={@JoinColumn( name="PAGE_ID",nullable=false) }, 
	inverseJoinColumns={@JoinColumn(name="ROLE_ID", nullable=false)}
	)
	public Set<Role> getRole() {
		return role;
	}
	public void setRole(Set<Role> role) {
		this.role = role;
	}
	@Enumerated(EnumType.STRING)
	@Column(nullable=false)
	public EnumPageType getPageType() {
		return pageType;
	}

	public void setPageType(EnumPageType pageType) {
		this.pageType = pageType;
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
