package kz.tem.portal.server.register.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import kz.tem.portal.PortalException;
import kz.tem.portal.server.bean.ITable;
import kz.tem.portal.server.model.Page;
import kz.tem.portal.server.model.Portlet;
import kz.tem.portal.server.register.IPortletRegister;

@SuppressWarnings("serial")
public class PortletRegisterImpl implements IPortletRegister{

	@Autowired
	private SessionFactory sessionFactory;
	
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	
	@Override
	@Transactional
	public Portlet addPortlet(Portlet portlet) throws PortalException {
		Session session = null;
		try{
			session = sessionFactory.getCurrentSession();
			session.persist(portlet);
			session.flush();
			return portlet;
		}catch(Exception ex){
			session.clear();
			throw new PortalException("Ошибка добавления нового портлета",ex);
		}
	}

	@Override
	@Transactional
	public void updatePortlet(Portlet portlet) throws PortalException {
		Session session = null;
		try{
			session = sessionFactory.getCurrentSession();
			session.merge(portlet);
			session.flush();
			session.evict(portlet);
		}catch(Exception ex){
			session.clear();
			throw new PortalException("Ошибка при сохранении портлета",ex);
		}
		
	}

	@Override
	@Transactional
	public void deletePortlet(Long id) throws PortalException {
		Session session = null;
		try{
			session = sessionFactory.getCurrentSession();
			session.delete(session.get(Portlet.class, id));
			session.flush();
		}catch(Exception ex){
			session.clear();
			throw new PortalException("Ошибка при удалении портлета",ex);
		}
		
	}

	@Override
	@Transactional(readOnly=true)
	public ITable<Portlet> table(int first, int count, Long pageId,String location)
			throws PortalException {
		Session session = null;
		try{
			session = sessionFactory.getCurrentSession();
			Criteria criteria = session.createCriteria(Portlet.class);
			criteria.add(Restrictions.eq("page.id", pageId));
			if(location!=null)
				criteria.add(Restrictions.eq("position", location));
			
			if(count>0){
				criteria.setFirstResult(first);
				criteria.setMaxResults(count);
			}
			final List<Portlet> list = criteria.list();
			
			Criteria crit2 = session.createCriteria(Portlet.class);
			crit2.add(Restrictions.eq("page.id", pageId));
			if(location!=null)
				crit2.add(Restrictions.eq("position", location));
			crit2.setProjection(Projections.rowCount());
			final long total = (Long) crit2.uniqueResult();
			return new ITable<Portlet>() {
				private static final long serialVersionUID = 1L;

				@Override
				public Long total() {
					return total;
				}
				
				@Override
				public List<Portlet> records() {
					return list;
				}
			};
		}catch(Exception ex){
			throw new PortalException("Не удалось получить список портлетов",ex);
		}
	}
	@Override
	@Transactional(readOnly=true)
	public ITable<Portlet> table(int first, int count, Long pageId)
			throws PortalException {
		return table(first, count, pageId, null);
	}
}
