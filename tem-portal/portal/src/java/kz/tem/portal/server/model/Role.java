package kz.tem.portal.server.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name="PT_ROLE")
public class Role extends IdEntity implements Serializable{

	private String name;
	
	private Set<User> user = new HashSet<User>();
	private Set<Page> page = new HashSet<Page>();

	@Column(nullable=false,unique=true)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@ManyToMany(mappedBy="role", fetch=FetchType.LAZY)
	public Set<User> getUser() {
		return user;
	}

	public void setUser(Set<User> user) {
		this.user = user;
	}
	
	@ManyToMany(mappedBy="role", fetch=FetchType.LAZY)
	public Set<Page> getPage() {
		return page;
	}

	public void setPage(Set<Page> page) {
		this.page = page;
	}

	public String toString(){
		return name;
	} 
	
}
