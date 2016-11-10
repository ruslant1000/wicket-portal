package kz.tem.portal.server.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name="PT_PORTLET")
public class Portlet extends IdEntity implements Serializable{
	
	private Page page;
	
	private String position;
	
	private String moduleName;

	@ManyToOne(fetch=FetchType.EAGER)
	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}
	@Column(nullable=false)
	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}
	@Column(nullable=false)
	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}
	
}
