package kz.tem.portal.api.model;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
public class LayoutInfo implements Serializable{

	private String name;
	
	private List<String> locations = null;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getLocations() {
		return locations;
	}

	public void setLocations(List<String> locations) {
		this.locations = locations;
	}
	
	
}