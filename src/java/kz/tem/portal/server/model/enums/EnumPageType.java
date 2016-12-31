package kz.tem.portal.server.model.enums;

public enum EnumPageType {

	SYSTEM("Системная",""),ADDED("Добавленная","/pg");
private String description;
	
private String pathPrefix;

	private EnumPageType(String description,String pathPrefix){
		this.description=description;
		this.pathPrefix=pathPrefix;
	}
	
	public String toString(){
		return description;
	}

	public String getPathPrefix() {
		return pathPrefix;
	}
	
	
}
