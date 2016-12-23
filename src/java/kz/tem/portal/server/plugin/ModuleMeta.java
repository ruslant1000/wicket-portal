package kz.tem.portal.server.plugin;

import java.io.Serializable;

/**
 * 
 * @author Ruslan Temirbulatov
 * 
 * Мета данные модуля
 */
@SuppressWarnings("serial")
public class ModuleMeta implements Serializable{
	
	public String artifactId;
	
	public String moduleName;
	public String displayName;
	public String moduleClass;
	
	private String contextLocation;
	
	public String moduleDirectoryPath;
	
	public String getModuleName() {
		return moduleName; 
	}
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getModuleClass() {
		return moduleClass;
	}
	public void setModuleClass(String moduleClass) {
		this.moduleClass = moduleClass;
	}
	public String getModuleDirectoryPath() {
		return moduleDirectoryPath;
	}
	public void setModuleDirectoryPath(String moduleDirectoryPath) {
		this.moduleDirectoryPath = moduleDirectoryPath;
	}
	
	public String getContextLocation() {
		return contextLocation;
	}
	public void setContextLocation(String contextLocation) {
		this.contextLocation = contextLocation;
	}
	public String toString(){
		return moduleName;
	}
	public String getArtifactId() {
		return artifactId;
	}
	public void setArtifactId(String artifactId) {
		this.artifactId = artifactId;
	}
	

}
