package kz.tem.portal.server.register.impl;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import kz.tem.portal.PortalException;
import kz.tem.portal.server.bean.ITable;
import kz.tem.portal.server.model.Portlet;
import kz.tem.portal.server.model.User;
import kz.tem.portal.server.register.IUserRegister;

@SuppressWarnings("serial")
public class UserRegisterImpl implements IUserRegister{
	
	public static UserRegisterImpl instance = null;
	
	private static Logger log = LoggerFactory.getLogger(UserRegisterImpl.class); 

	@Autowired
	private SessionFactory sessionFactory;
	
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	public void inits(){
		instance=this;
		
	}
	@Override
	@Transactional
	public void defaults(){
		Session session = null;
		try{
			session = sessionFactory.getCurrentSession();
			Criteria crt = session.createCriteria(User.class);
			crt.setProjection(Projections.rowCount());
			Long total = (Long)crt.uniqueResult();
			if(total<1){
				log.info("Инициализация супер пользователя");
				User user = new User();
				user.setEmail("portal@example.com");
				user.setLogin("admin");
				user.setPassword(encryptPassword("admin"));
				
				session.persist(user);
			}
			session.flush();
		}catch(Exception ex){
			session.clear();
			log.error("Ошибка инициализации UserRegisterImpl",ex);
		}
	}
	public static String encryptPassword(String password) throws NoSuchAlgorithmException{
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(password.getBytes());
		byte byteData[] = md.digest();
		StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
         sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
        
	}
	
	@Override
	@Transactional
	public User addNewUser(User user) throws PortalException {
		Session session = null;
		try{
			session = sessionFactory.getCurrentSession();
			user.setPassword(encryptPassword(user.getPassword()));
			session.persist(user);
			session.flush();
			return user;
		}catch(Exception ex){
			session.clear();
			throw new PortalException("Ошибка добавления нового пользователя",ex);
		}
	}

	@Override
	@Transactional
	public void updateUser(User user) throws PortalException {
		Session session = null;
		try{
			session = sessionFactory.getCurrentSession();
			User mu = session.get(User.class, user.getId());
			user.setPassword(mu.getPassword());
			session.merge(user);
			session.flush();
			session.evict(user);
		}catch(Exception ex){
			session.clear();
			throw new PortalException("Ошибка при сохранении пользователя",ex);
		}
		
	}

	@Override
	@Transactional
	public void deleteUser(Long userId) throws PortalException {
		Session session = null;
		try{
			session = sessionFactory.getCurrentSession();
			session.delete(session.get(User.class, userId));
			session.flush();
		}catch(Exception ex){
			session.clear();
			throw new PortalException("Ошибка при удалении пользователя",ex);
		}
		
	}
	
	

	@Override
	@Transactional(readOnly=true)
	public User authenticateByLogin(String login, String password){
		System.out.println("============= "+login+"   "+password+"  "+(sessionFactory==null));
		Session session = null;
		try{
			session = sessionFactory.getCurrentSession();
			Criteria criteria = session.createCriteria(User.class);
			criteria.add(Restrictions.eq("login", login));
			User user = (User)criteria.uniqueResult();
			if(user==null){
				throw new PortalException(PortalException.KEY_LOGIN_ERROR,"User not found with login: "+login);
			}
			if(!encryptPassword(password).equals(user.getPassword())){
				
				throw new PortalException(PortalException.KEY_LOGIN_ERROR,"Password not match");
			}
			return user;
		}catch(Exception ex){
			log.error("Ошибка аутентификации",ex);
			return null;
		}
		
	}

	@Override
	@Transactional
	public void updateUserPassword(Long userId, String oldPassword,
			String newPassword) throws PortalException {
		Session session = null;
		try{
			session = sessionFactory.getCurrentSession();
			User mu = session.get(User.class, userId);
			if(encryptPassword(oldPassword).equals(mu.getPassword()))
				mu.setPassword(encryptPassword(newPassword));
			session.merge(mu);
			session.flush();
			session.evict(mu);
		}catch(Exception ex){
			session.clear();
			throw new PortalException("Ошибка при изменении пароля",ex);
		}
		
	}
	@Override
	@Transactional(readOnly=true)
	public ITable<User> table(int first, int count) throws PortalException {
		Session session = null;
		try{
			session = sessionFactory.getCurrentSession();
			Criteria criteria = session.createCriteria(User.class);
			
			if(count>0){
				criteria.setFirstResult(first);
				criteria.setMaxResults(count);
			}
			final List<User> list = criteria.list();
			
			Criteria crit2 = session.createCriteria(User.class);
			crit2.setProjection(Projections.rowCount());
			final long total = (Long) crit2.uniqueResult();
			
			return new ITable<User>() {
				private static final long serialVersionUID = 1L;

				@Override
				public Long total() {
					return total;
				}
				
				@Override
				public List<User> records() {
					return list;
				}
			};
		}catch(Exception ex){
			throw new PortalException("Не удалось получить список пользователей",ex);
		}
	}
}
