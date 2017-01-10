package kz.tem.portal.server.register.impl;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;
import java.util.Random;

import kz.tem.portal.PortalException;
import kz.tem.portal.server.bean.ITable;
import kz.tem.portal.server.model.User;
import kz.tem.portal.server.model.UserSecretKey;
import kz.tem.portal.server.register.IUserRegister;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
/**
 * 
 * @author Ruslan Temirbulatov
 *
 */
@SuppressWarnings("serial")
@Transactional
public class UserRegisterImpl extends AbstractRegister implements IUserRegister{
	
	private static Logger log = LoggerFactory.getLogger(UserRegisterImpl.class); 

	@Override
	public void defaults(){
		Session session = null;
		try{
			session = ht.getSessionFactory().openSession();
			session.beginTransaction();
			Criteria crt = session.createCriteria(User.class);
			crt.setProjection(Projections.rowCount());
			Long total = (Long)crt.uniqueResult();
			if(total<1){
				User user = new User();
				user.setEmail("portal@example.com");
				user.setLogin("admin");
				user.setPassword(encryptPassword("admin"));
				
				session.persist(user);
			}
			session.flush();
			session.getTransaction().commit();
		}catch(Exception ex){
			ex.printStackTrace();
			log.error("Ошибка инициализации UserRegisterImpl",ex);
		}finally{
			try{session.clear();}catch(Exception ex2){}
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
	public User addNewUser(User user) throws PortalException {
		try{
			ht.persist(user);
			user.setPassword(encryptPassword(user.getPassword()));
			ht.flush();
			return user;
		}catch(Exception ex){
			ht.clear();
			throw new PortalException("Не удалось добавить пользователя",ex);
		}
	}

	@Override
	public void updateUser(User user) throws PortalException {
		try{
			User mu =  ht.get(User.class, user.getId());
			user.setPassword(mu.getPassword());
			ht.merge(user);
			ht.flush();
		}catch(Exception ex){
			ht.clear();
			throw new PortalException("Не удалось сохранить изменения",ex);
		}
		
	}

	@Override
	public void deleteUser(Long userId) throws PortalException {
		try{
			ht.delete(ht.get(User.class, userId));
			ht.flush();
		}catch(Exception ex){
			ht.clear();
			throw new PortalException("Не удалось удалить запись",ex);
		}
		
	}
	
	

	@Override
	public User authenticateByLogin(String login, String password){
		Session session = null;
		try{
			session = ht.getSessionFactory().getCurrentSession();
			Criteria criteria = session.createCriteria(User.class);
			criteria.add(Restrictions.eq("login", login));
			User user = (User)criteria.uniqueResult();
			if(user==null){
				throw new PortalException(PortalException.KEY_LOGIN_ERROR,"User not found with login: "+login);
			}
			if(!encryptPassword(password).equals(user.getPassword())){
				
				throw new PortalException(PortalException.KEY_LOGIN_ERROR,"Password not match");
			}
			
			Hibernate.initialize(user.getRole());
			return user;
		}catch(Exception ex){
			session.clear();
			log.error("Ошибка аутентификации",ex);
			return null;
		}
		
	}

	@Override
	public void updateUserPassword(Long userId, String oldPassword,
			String newPassword) throws PortalException {
		try{
			User mu = ht.get(User.class, userId);
			if(encryptPassword(oldPassword).equals(mu.getPassword()))
				mu.setPassword(encryptPassword(newPassword));
			ht.merge(mu);
			ht.flush();
		}catch(Exception ex){
			ht.clear();
			throw new PortalException("Ошибка",ex);
		}
		
	}
	@Override
	public ITable<User> table(int first, int count) throws PortalException {
		return getTable(User.class, first, count,null,null,true, new InSessionAction<User>() {

			@Override
			public void action(User entity) throws Exception {
				Hibernate.initialize(entity.getRole());
				
			}
		});
	}
	@Override
	public UserSecretKey createSecretKey(User user, String key)
			throws PortalException {
		try{
			UserSecretKey usk = new UserSecretKey();
			usk.setName(key);
			usk.setCreated(new Date());
			usk.setUser(user);
			usk.setValue(""+new Random().nextLong());
			ht.persist(usk);
			ht.flush();
			return usk;
		}catch(Exception ex){
			ht.clear();
			ex.printStackTrace();
			throw new PortalException("Could not create UserSecretKey",ex);
		}
	}
	@Override
	public boolean checkSecretKey(User user, String key, String value)
			throws PortalException {
		try{
			Criteria crit = ht.getSessionFactory().getCurrentSession().createCriteria(UserSecretKey.class);
			crit.add(Restrictions.eq("user.id", user.getId()));
			crit.add(Restrictions.eq("name", key));
			crit.add(Restrictions.eq("value", value));
			Object o= crit.uniqueResult();
			log.info("checkSecretKey object: "+o);
			return o!=null;
		}catch(Exception ex){
			ex.printStackTrace();
			throw new PortalException("Could not check UserSecretKey",ex);
		}
	}
	@Override
	public void deleteSecretKey(UserSecretKey key) throws PortalException {
		try{
			ht.delete(key);
			ht.flush();
		}catch(Exception ex){
			ht.clear();
			ex.printStackTrace();
			throw new PortalException("Could not delete UserSecretKey",ex);
		}
		
	}
	@Override
	public void deleteSecretKeyIfExists(User user, String key)
			throws PortalException {
		try{
			Criteria crit = ht.getSessionFactory().getCurrentSession().createCriteria(UserSecretKey.class);
			crit.add(Restrictions.eq("user.id", user.getId()));
			crit.add(Restrictions.eq("name", key));
			Object usk = crit.uniqueResult();
			if(usk!=null)
				ht.delete(usk);
			ht.flush();
		}catch(Exception ex){
			ht.clear();
			ex.printStackTrace();
			throw new PortalException("Could not delete UserSecretKey",ex);
		}
		
	}
	@Override
	public User getUserById(Long userid) throws PortalException {
		try{
			User user = ht.get(User.class, userid);
			return user;
		}catch(Exception ex){
			ex.printStackTrace();
			throw new PortalException("Could not get User by id: "+userid,ex);
		}
	}
	@Override
	public User getUserByEmail(String email) throws PortalException {
		
		try{
			User user = null;
			List<User> users = (List<User>) ht.find("from User where email=?", email);
			if(users!=null && users.size()>0)
				user = users.get(0);
			return user;
		}catch(Exception ex){
			ex.printStackTrace();
			throw new PortalException("Could not get User by email: "+email,ex);
		}
	}
}
