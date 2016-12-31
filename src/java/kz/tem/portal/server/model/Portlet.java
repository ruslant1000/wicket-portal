package kz.tem.portal.server.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;
/**
 * 
 * @author Ruslan Temirbulatov
 *
 */
@SuppressWarnings("serial")
@Entity
@Table(name="PT_PORTLET")
@Audited
public class Portlet extends IdEntity implements Serializable{
	
	private Page page;
	
	private String position;
	
	private String moduleName;
	
	
	private byte[] settings;
	
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

	@Lob
	@Basic(fetch=FetchType.LAZY)
	public byte[] getSettings() {
		return settings;
	}

	public void setSettings(byte[] settings) {
		this.settings = settings;
	}


	
}
