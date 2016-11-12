package kz.tem.portal.server.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
/**
 * 
 * @author Ruslan Temirbulatov
 *
 */
@MappedSuperclass
public class IdEntity{

	private Long id;
	
	@Id
	@GeneratedValue(strategy=GenerationType.TABLE)
	@Column(name="id")
	public Long getId() {
		return id;
	}
	
	
	public void setId(Long id) {
		this.id = id;
	}
}
