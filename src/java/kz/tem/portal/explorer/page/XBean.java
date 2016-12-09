package kz.tem.portal.explorer.page;

import kz.tem.portal.server.model.IdEntity;

@SuppressWarnings("serial")
public class XBean extends IdEntity{

	private String name;

	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public XBean(Long id, String name) {
		super();
		setId(id);
		this.name = name;
	}
	
	
}
