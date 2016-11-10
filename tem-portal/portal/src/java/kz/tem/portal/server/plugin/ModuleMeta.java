package kz.tem.portal.server.plugin;

import java.io.Serializable;
/**
 * 
 * @author prog_01
 * Описание модуля
 *
 */
@SuppressWarnings("serial")
public class ModuleMeta implements Serializable{
	
	public String moduleName;
	public String displayName;
	public String moduleClass;
	/**
	 * Полный путь к директории расположения модуля 
	 */
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
	public String toString(){
		return moduleName;
	}
	

}
