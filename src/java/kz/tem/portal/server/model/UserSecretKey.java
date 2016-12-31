package kz.tem.portal.server.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
/**
 * 
 * @author Ruslan Temirbulatov
 * 
 * Секретный ключ, который привязан к пользователю.
 * Например это используется для добавления одноразового уникального ключа в URL для подтверждения регистрации пользователя.
 *
 */
@SuppressWarnings("serial")
@Entity
@Table(name="PT_SECRET_KEY", uniqueConstraints={@UniqueConstraint(columnNames={"user_id","name"})})
public class UserSecretKey extends IdEntity{

	private String name;
	private String value;
	private Date created;
	private User user;
	
	@Column(nullable=false)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Column(nullable=false)
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	@ManyToOne(fetch=FetchType.LAZY)
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
	
	
	
}
