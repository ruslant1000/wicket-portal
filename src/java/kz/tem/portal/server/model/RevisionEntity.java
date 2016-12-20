package kz.tem.portal.server.model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.envers.DefaultRevisionEntity;

@SuppressWarnings("serial")
@Entity
@Table(name="REVISIONS")
@org.hibernate.envers.RevisionEntity(RevisionListener2.class)
public class RevisionEntity extends DefaultRevisionEntity{

	private String username;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	
//	@ManyToOne
//	private User user;
//
//	public User getUser() {
//		return user;
//	}
//
//	public void setUser(User user) {
//		this.user = user;
//	}
	
	
	
}
