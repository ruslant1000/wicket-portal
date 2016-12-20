package kz.tem.portal.server.register.impl;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import kz.tem.portal.server.bean.ITable;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Subqueries;
import org.springframework.orm.hibernate5.HibernateTemplate;

@SuppressWarnings("serial")
public abstract class AbstractRegister implements Serializable{

	public transient HibernateTemplate ht;

	public HibernateTemplate getHt() {
		return ht;
	}

	public void setHt(HibernateTemplate ht) {
		this.ht = ht;
	}
	
	
	
	public <T>ITable getTable(Class entity,int first, int count){
		return getTable(entity, first, count, null, null, true,null);
	}
	public <T>ITable getTable(Class entity,int first, int count, List<Criterion> restrictions,String sort, boolean asc){
		return getTable(entity, first, count, restrictions, sort, asc, null);
	}
	public <T>ITable getTable(Class entity,int first, int count, List<Criterion> restrictions,String sort, boolean asc, InSessionAction<T> action){
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
//			log.error("Error getting table "+entity.getName() , ex);
			throw new RuntimeException(
					"Error getting table "+entity.getName(), ex);
		} finally {
			if(sss!=null)try{sss.close();}catch(Exception ex){}
		}

	}	
	
	
}
