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
import kz.tem.portal.server.model.enums.EnumPageType;
import kz.tem.portal.server.register.IPageRegister;
/**
 * 
 * @author Ruslan Temirbulatov
 *
 */
@SuppressWarnings("serial")
@Transactional
public class PageRegisterImpl extends AbstractRegister implements IPageRegister{


	@Override
	public Page addNewPage(Page page) throws PortalException {
		try{
			if(page.getPageType()==null)
				page.setPageType(EnumPageType.ADDED);
//			if(page.getParentPage()!=null)
//				page.setParentPageId(page.getParentPage().getId());
//			else
//				page.setParentPageId(null);
			ht.persist(page);
			ht.flush();
			return page;
		}catch(Exception ex){
			ht.clear();
			ex.printStackTrace();
			throw new PortalException("Не удалось добавить новую страницу",ex);
		}
		
		
	}
	@Override
	public void savePage(Page page) throws PortalException {
		try{
//			if(page.getParentPage()!=null)
//				page.setParentPageId(page.getParentPage().getId());
//			else
//				page.setParentPageId(null);
			ht.merge(page);
			ht.flush();
//			session.evict(page);
		}catch(Exception ex){
			ht.clear();
			throw new PortalException("Не удалось сохранить страницу",ex);
		}
		
	}
	@Override
	public Page getPage(Long id) throws PortalException {
		try{
			Page page = ht.get(Page.class, id);
			return page;
		}catch(Exception ex){
			throw new PortalException("Не удалось получить данные страницы",ex);
		}
	}
	@Override
	public Page getPage(String url) throws PortalException {
		try{
			Criteria criteria = ht.getSessionFactory().getCurrentSession().createCriteria(Page.class);
			criteria.add(Restrictions.eq("url", url));
			Page page = (Page) criteria.uniqueResult();
			if(page==null)
				throw new PortalException(PortalException.NOT_FOUND,"Page not found");
			Hibernate.initialize(page.getRole());
			return page;
		}catch(PortalException ex){
			throw ex;
		}catch(Exception ex){
			ex.printStackTrace();
			throw new PortalException("Could not get page",ex);
		}
	}

	@Override
	public void deletePage(Long id) throws PortalException {
		try{
			ht.delete(ht.get(Page.class, id));
			ht.flush();
		}catch(Exception ex){
			ht.clear();
			throw new PortalException("Не удалось удалить страницу",ex);
		}
		
	}
	@Override
	public ITable<Page> pages(int first, int count) throws PortalException {
		
		return getTable(Page.class, first, count,null,null,true, new InSessionAction<Page>() {

			@Override
			public void action(Page entity) throws Exception {
				Hibernate.initialize(entity.getRole());
			}
		});
		
		
		
	}
	@Override
	public List<Page> pagesTree() throws PortalException {
		Session session = null;
		try{
			List<Page> tree = new LinkedList<Page>();
			session = ht.getSessionFactory().getCurrentSession();
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
