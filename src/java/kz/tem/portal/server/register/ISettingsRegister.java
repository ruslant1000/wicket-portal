package kz.tem.portal.server.register;

import java.io.Serializable;
import java.util.Map;

import kz.tem.portal.PortalException;
import kz.tem.portal.server.bean.ITable;
import kz.tem.portal.server.model.Settings;
/**
 * 
 * @author Ruslan Temirbulatov
 *
 */
public interface ISettingsRegister extends Serializable{
	
	public void saveAllSettings(Map<String,String> settings)throws PortalException;
	
	public Settings addNewSettings(Settings settings)throws PortalException;
	public void updateSettings(Settings settings)throws PortalException;
	public void deleteSettings(Long settingsId)throws PortalException;
	public ITable<Settings> table(int first,int count)throws PortalException;

}
