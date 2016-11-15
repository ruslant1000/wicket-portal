package kz.tem.portal.server.register;

import java.io.Serializable;

import kz.tem.portal.PortalException;
import kz.tem.portal.server.bean.ITable;
import kz.tem.portal.server.model.Portlet;
import kz.tem.portal.server.plugin.ModuleConfig;
/**
 * 
 * @author Ruslan Temirbulatov
 *
 */
public interface IPortletRegister extends Serializable{

	public Portlet addPortlet(Portlet portlet)throws PortalException;
	
	public void updatePortlet(Portlet portlet)throws PortalException;
	
	public void deletePortlet(Long id)throws PortalException;
	
	public ITable<Portlet> table(int first, int count, Long pageId, String location)throws PortalException;
	public ITable<Portlet> table(int first, int count, Long pageId)throws PortalException;

	public void updatePortletSettings(Long portletId, ModuleConfig config)throws PortalException;
	 
	public ModuleConfig getPortletSettings(Long portletId)throws PortalException;
	

}
