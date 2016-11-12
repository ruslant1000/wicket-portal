package kz.tem.portal.server.register.impl;

import java.util.List;

import kz.tem.portal.PortalException;
import kz.tem.portal.server.bean.ITable;
import kz.tem.portal.server.model.Role;
import kz.tem.portal.server.register.IRoleRegister;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.springframework.transaction.annotation.Transactional;
/**
 * 
 * @author Ruslan Temirbulatov
 *
 */
@SuppressWarnings("serial")
public class RoleRegisterImpl implements IRoleRegister{

	private SessionFactory sessionFactory;
	
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@Override
	@Transactional
	public Role addNewRole(Role role) throws PortalException {
		Session session = null;
		try{
			session = sessionFactory.getCurrentSession();
			session.persist(role);
			session.flush();
			return role;
		}catch(Exception ex){
			session.clear();
			throw new PortalException("Ошибка добавления новой роли",ex);
		}
	}

	@Override
	@Transactional
	public void updateRole(Role role) throws PortalException {
		Session session = null;
		try{
			session = sessionFactory.getCurrentSession();
			session.merge(role);
			session.flush();
		}catch(Exception ex){
			session.clear();
			throw new PortalException("Ошибка изменения роли",ex);
		}
		
	}

	@Override
	@Transactional
	public void deleteRole(Long id) throws PortalException {
		Session session = null;
		try{
			session = sessionFactory.getCurrentSession();
			session.delete(session.get(Role.class, id));
			session.flush();
		}catch(Exception ex){
			session.clear();
			throw new PortalException("Ошибка удаления роли",ex);
		}
		
	}

	@Override
	@Transactional(readOnly=true)
	public ITable<Role> table(int first, int count) throws PortalException {
		Session session = null;
		try{
			session = sessionFactory.getCurrentSession();
			Criteria criteria = session.createCriteria(Role.class);
			if(count>0){
				criteria.setFirstResult(first);
				criteria.setMaxResults(count);
			}
			final List<Role> list = criteria.list();
			
			Criteria crit2 = session.createCriteria(Role.class);
			crit2.setProjection(Projections.rowCount());
			final long total = (Long) crit2.uniqueResult();
			return new ITable<Role>() {
				private static final long serialVersionUID = 1L;

				@Override
				public Long total() {
					return total;
				}
				
				@Override
				public List<Role> records() {
					return list;
				}
			};
		}catch(Exception ex){
			throw new PortalException("Не удалось получить список ролей",ex);
		}
	}

}
