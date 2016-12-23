package kz.tem.portal.server.register.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import kz.tem.portal.PortalException;
import kz.tem.portal.server.bean.ITable;
import kz.tem.portal.server.model.Settings;
import kz.tem.portal.server.register.ISettingsRegister;
/**
 * 
 * @author Ruslan Temirbulatov
 *
 */
@SuppressWarnings("serial")
public class SettingsRegisterImpl implements ISettingsRegister{

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
	public Settings addNewSettings(Settings settings) throws PortalException {
		Session session = null;
		try{
			session = sessionFactory.getCurrentSession();
			session.persist(settings);
			session.flush();
			return settings;
		}catch(Exception ex){
			session.clear();
			throw new PortalException("Ошибка добавления новой настройки",ex);
		}
	}

	@Override
	@Transactional
	public void updateSettings(Settings settings) throws PortalException {
		Session session = null;
		try{
			session = sessionFactory.getCurrentSession();
			session.merge(settings);
			session.flush();
		}catch(Exception ex){
			session.clear();
			throw new PortalException("Ошибка изменения настройки",ex);
		}
		
	}

	@Override
	@Transactional
	public void deleteSettings(Long settingsId) throws PortalException {
		Session session = null;
		try{
			session = sessionFactory.getCurrentSession();
			session.delete(session.get(Settings.class, settingsId));
			session.flush();
		}catch(Exception ex){
			session.clear();
			throw new PortalException("Ошибка удаления настройки",ex);
		}
		
	}

	@Override
	@Transactional(readOnly=true)
	public ITable<Settings> table(int first, int count) throws PortalException {
		Session session = null;
		try{
			session = sessionFactory.getCurrentSession();
			Criteria criteria = session.createCriteria(Settings.class);
			if(count>0){
				criteria.setFirstResult(first);
				criteria.setMaxResults(count);
			}
			final List<Settings> list = criteria.list();
			
			Criteria crit2 = session.createCriteria(Settings.class);
			crit2.setProjection(Projections.rowCount());
			final long total = (Long) crit2.uniqueResult();
			return new ITable<Settings>() {
				private static final long serialVersionUID = 1L;

				@Override
				public Long total() {
					return total;
				}
				
				@Override
				public List<Settings> records() {
					return list;
				}
			};
		}catch(Exception ex){
			throw new PortalException("Не удалось получить список настроек",ex);
		}
	}
	@Override
	@Transactional(noRollbackFor={Exception.class})
	public void saveAllSettings(Map<String,String> settings) throws PortalException {
		Session session = null;
		try{
			session = sessionFactory.getCurrentSession();
			Criteria crt = session.createCriteria(Settings.class);
			List<Settings> list = crt.list();
			Set<String> olds = new HashSet<String>();
			for(Settings s:list){
				if(settings.containsKey(s.getName())){
					s.setValue(settings.get(s.getName()));
					session.merge(s);
					olds.add(s.getName());
				}
			}
			for(String key:settings.keySet()){
				if(!olds.contains(key)){
					Settings s = new Settings();
					s.setName(key);
					s.setValue(settings.get(key));
					session.persist(s);
				}
			}
			session.flush();
		}catch(Exception ex){
			session.clear();
			throw new PortalException("Ошибка сохранения настроек",ex);
		}
		
	}

}