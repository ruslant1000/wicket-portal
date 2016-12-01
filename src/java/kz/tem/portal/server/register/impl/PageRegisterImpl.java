package kz.tem.portal.server.register.impl;

import java.util.LinkedList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.transaction.annotation.Transactional;

import kz.tem.portal.PortalException;
import kz.tem.portal.server.bean.ITable;
import kz.tem.portal.server.model.Page;
import kz.tem.portal.server.register.IPageRegister;
/**
 * 
 * @author Ruslan Temirbulatov
 *
 */
@SuppressWarnings("serial")
public class PageRegisterImpl implements IPageRegister{

	@Autowired
	private SessionFactory sessionFactory;
	
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	private HibernateTemplate ht;
	
	public HibernateTemplate getHt() {
		return ht;
	}
	public void setHt(HibernateTemplate ht) {
		this.ht = ht;
	}

	@Override
	@Transactional(rollbackFor={ConstraintViolationException.class})
	public Page addNewPage(Page page) throws PortalException {
		Session session = null;
		try{
			session = sessionFactory.getCurrentSession();
//			if(page.getParentPage()!=null)
//				page.setParentPageId(page.getParentPage().getId());
//			else
//				page.setParentPageId(null);
			session.persist(page);
			session.flush();
			return page;
		}catch(Exception ex){
			session.clear();
			ex.printStackTrace();
			throw new PortalException("Не удалось добавить новую страницу",ex);
		}
		
		
	}
	@Override
	@Transactional(rollbackFor=Exception.class)
	public void savePage(Page page) throws PortalException {
		Session session = null;
		try{
			session = sessionFactory.getCurrentSession();
//			if(page.getParentPage()!=null)
//				page.setParentPageId(page.getParentPage().getId());
//			else
//				page.setParentPageId(null);
			session.merge(page);
			session.flush();
//			session.evict(page);
		}catch(Exception ex){
			session.clear();
			throw new PortalException("Не удалось сохранить страницу",ex);
		}
		
	}
	@Override
	@Transactional(readOnly=true)
	public Page getPage(Long id) throws PortalException {
		Session session = null;
		try{
			session = sessionFactory.getCurrentSession();
			Page page = session.get(Page.class, id);
//			if(page!=null){
//				session.evict(page);
//			}
			return page;
		}catch(Exception ex){
			throw new PortalException("Не удалось получить данные страницы",ex);
		}
	}
	@Override
	@Transactional(readOnly=true)
	public Page getPage(String url) throws PortalException {
		Session session = null;
		try{
			session = sessionFactory.getCurrentSession();
			Criteria criteria = session.createCriteria(Page.class);
			criteria.add(Restrictions.eq("url", url));
			Page page = (Page) criteria.uniqueResult();
			Hibernate.initialize(page.getRole());
//			if(page!=null){
//				session.evict(page);
//			}
			return page;
		}catch(Exception ex){
			throw new PortalException("Не удалось получить данные страницы",ex);
		}
	}

	@Override
	@Transactional
	public void deletePage(Long id) throws PortalException {
		Session session = null;
		try{
			session = sessionFactory.getCurrentSession();
			session.delete(session.get(Page.class, id));
			session.flush();
		}catch(Exception ex){
			session.clear();
			throw new PortalException("Не удалось удалить страницу",ex);
		}
		
	}
	@Override
	@Transactional(readOnly=true)
	public ITable<Page> pages(int first, int count) throws PortalException {
		Session session = null;
		try{
			session = sessionFactory.getCurrentSession();
			Criteria criteria = session.createCriteria(Page.class);
			if(count>0){
				criteria.setFirstResult(first);
				criteria.setMaxResults(count);
			}
			final List<Page> list = criteria.list();
			if(list!=null && list.size()>0)
				for(Page p:list)
					Hibernate.initialize(p.getRole());
			Criteria crit2 = session.createCriteria(Page.class);
			crit2.setProjection(Projections.rowCount());
			final long total = (Long) crit2.uniqueResult();
			return new ITable<Page>() {
				private static final long serialVersionUID = 1L;

				@Override
				public Long total() {
					return total;
				}
				
				@Override
				public List<Page> records() {
					return list;
				}
			};
		}catch(Exception ex){
			throw new PortalException("Не удалось получить таблицу страниц",ex);
		}
	}
	@Override
	@Transactional(readOnly=true)
	public List<Page> pagesTree() throws PortalException {
		Session session = null;
		try{
			List<Page> tree = new LinkedList<Page>();
			session = sessionFactory.getCurrentSession();
			Criteria criteria = session.createCriteria(Page.class);
			List<Page> list = criteria.list();
			
			if(list!=null && list.size()>0){
				for(Page page:list){
					if(page.getParentPage()==null){
						tree.add(page);
					}
					Hibernate.initialize(page.getRole());
				}
				for(Page page:list){
					if(page.getParentPage()!=null){
						for(Page key:tree){
							if(key.getId().longValue()==page.getParentPage().getId().longValue()){
								key.getChilds().add(page);
							}
						}
					}
				}
			}
			return tree;
			
		}catch(Exception ex){
			throw new PortalException("Не удалось получить дерево страниц",ex);
		}
	}

}
