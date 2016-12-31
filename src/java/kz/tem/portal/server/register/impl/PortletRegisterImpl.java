package kz.tem.portal.server.register.impl;

import java.util.LinkedList;
import java.util.List;

import kz.tem.portal.PortalException;
import kz.tem.portal.server.bean.ITable;
import kz.tem.portal.server.model.Portlet;
import kz.tem.portal.server.plugin.ModuleConfig;
import kz.tem.portal.server.register.IPortletRegister;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
/**
 * 
 * @author Ruslan Temirbulatov
 *
 */
@SuppressWarnings("serial")
@Transactional
public class PortletRegisterImpl extends AbstractRegister implements IPortletRegister{

	
	
	@Override
	public Portlet addPortlet(Portlet portlet) throws PortalException {
		try{
			ht.persist(portlet);
			ht.flush();
			return portlet;
		}catch(Exception ex){
			ht.clear();
			throw new PortalException("Ошибка добавления нового портлета",ex);
		}
	}

	@Override
	public void updatePortlet(Portlet portlet) throws PortalException {
		try{
			ht.merge(portlet);
			ht.flush();
		}catch(Exception ex){
			ht.clear();
			throw new PortalException("Ошибка при сохранении портлета",ex);
		}
		
	}

	@Override
	public void deletePortlet(Long id) throws PortalException {
		try{
			ht.delete(ht.get(Portlet.class, id));
			ht.flush();
		}catch(Exception ex){
			ht.clear();
			throw new PortalException("Ошибка при удалении портлета",ex);
		}
		
	}

	@Override
	public ITable<Portlet> table(int first, int count, Long pageId,String location)
			throws PortalException {
		List<Criterion> crits = new LinkedList<Criterion>();
		crits.add(Restrictions.eq("page.id", pageId));
		if(location!=null)
			crits.add(Restrictions.eq("position", location));
		return getTable(Portlet.class, first, count, crits, null, true);
	}
	@Override
	public ITable<Portlet> table(int first, int count, Long pageId)
			throws PortalException {
		return table(first, count, pageId, null);
	}
	@Override
	public void updatePortletSettings(Long portletId, ModuleConfig config)
			throws PortalException {
		try{
			Portlet portlet = ht.get(Portlet.class, portletId);
			portlet.setSettings(config.toXML().getBytes("utf-8"));
			ht.merge(portlet);
			ht.flush();
		}catch(Exception ex){
			ht.clear();
			throw new PortalException("Ошибка при сохранении портлета",ex);
		}
		
	}
	@Override
	public ModuleConfig getPortletSettings(Long portletId)
			throws PortalException {
		try{
			ModuleConfig config= null;
			Portlet portlet = ht.get(Portlet.class, portletId);
			byte[] settings = portlet.getSettings();
			
			
			if(settings!=null){
				System.out.println("++++++++++++");
				System.out.println(new String(settings));
				System.out.println("++++++++++++");
				config = ModuleConfig.parse(new String(settings));
			}
			return config;
		}catch(Exception ex){
			throw new PortalException("Could not get portlet settings",ex);
		}
	}
}
