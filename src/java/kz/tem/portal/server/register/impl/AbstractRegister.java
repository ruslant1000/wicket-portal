package kz.tem.portal.server.register.impl;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import kz.tem.portal.PortalException;
import kz.tem.portal.server.bean.ITable;
import kz.tem.portal.server.model.RevisionEntity;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQuery;
import org.springframework.orm.hibernate5.HibernateTemplate;

@SuppressWarnings("serial")
public abstract class AbstractRegister implements Serializable{
	
	private static Logger log = Logger.getLogger(AbstractRegister.class);

	public transient HibernateTemplate ht;

	public HibernateTemplate getHt() {
		return ht;
	}

	public void setHt(HibernateTemplate ht) {
		this.ht = ht;
	}
	
	
	
	public <T>ITable getTable(Class entity,int first, int count)throws PortalException{
		return getTable(entity, first, count, null, null, true,null);
	}
	public <T>ITable getTable(Class entity,int first, int count, List<Criterion> restrictions,String sort, boolean asc)throws PortalException{
		return getTable(entity, first, count, restrictions, sort, asc, null);
	}
	public <T>ITable getTable(Class entity,int first, int count, List<Criterion> restrictions,String sort, boolean asc, InSessionAction<T> action)throws PortalException{
		Session sss = null;
		try {
			sss = getHt().getSessionFactory().openSession();
			Criteria crit = sss.createCriteria(entity);
			crit.setProjection(Projections.countDistinct("id"));
			if(restrictions!=null){
				for(Criterion restriction:restrictions)
					if(restriction!=null){
						String str = restriction.toString();
						crit.add(restriction);
					}
			}
			final Long total = ((Long) crit.uniqueResult());
			
			
			
			crit = sss.createCriteria(entity);
			crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			if(restrictions!=null){
				for(Criterion restriction:restrictions)
					if(restriction!=null){
						crit.add(restriction);
					}
			}
			
//			crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			if(sort!=null && sort.trim().length()>0){
				crit.addOrder(asc?Order.asc(sort):Order.desc(sort));
			}
			
			
			if(count>0){
				crit.setFirstResult(first);
				crit.setMaxResults(count);
//				crit.setFetchSize(count);
			}
			
			final List<T> list = crit.list();
			if(action!=null){
				for(T t:list)
					action.action(t);
			}
			return new ITable<T>() {
				
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public List<T> records() {
					return list;
				}

				@Override
				public Long total() {
					return total;
				}

			

			};			
		} catch (Exception ex) {
			log.error("Error getting table "+entity.getName(), ex);
			throw new PortalException(
					"Error getting table "+entity.getName(), ex);
		} finally {
			if(sss!=null)try{sss.close();}catch(Exception ex){}
		}

	}	
	
	public <T>ITable getTableAudit(Class entity, Long entityId, InSessionAuditAction<T> action)throws PortalException{
		final List<T> slist = new LinkedList<T>();
		
		
		AuditReader reader = AuditReaderFactory.get(ht.getSessionFactory().getCurrentSession());
		AuditQuery query = reader.createQuery().forRevisionsOfEntity(entity, false, false);
		query.add(AuditEntity.id().eq(entityId));
		
		final List<Object[]> list = query.getResultList();
		for(Object[] s:list){
			T sb =(T) s[0]; 
			slist.add(sb);
			RevisionEntity re = (RevisionEntity) s[1];
			try {
				action.action(sb, re);
			} catch (Exception e) {
				e.printStackTrace();
				throw new PortalException("Could not get audited table",e);
			}
		}
		return new ITable<T>() {
			
			@Override
			public Long total() {
				return (long) slist.size();
			}
			
			@Override
			public List<T> records() {
				return slist;
			}
		};
	}
}
