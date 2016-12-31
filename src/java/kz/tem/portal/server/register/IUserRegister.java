package kz.tem.portal.server.register;

import java.io.Serializable;

import kz.tem.portal.PortalException;
import kz.tem.portal.server.bean.ITable;
import kz.tem.portal.server.model.User;
import kz.tem.portal.server.model.UserSecretKey;
/**
 * 
 * @author Ruslan Temirbulatov
 *
 */
public interface IUserRegister extends Serializable{
	
	public void defaults();
	
	public User addNewUser(User user)throws PortalException;
	public User getUserById(Long userid)throws PortalException;
	public User getUserByEmail(String email)throws PortalException;
	
	public void updateUser(User user)throws PortalException;
	public void updateUserPassword(Long userId, String oldPassword,String newPassword)throws PortalException;
	public void deleteUser(Long userId)throws PortalException;
	
	public User authenticateByLogin(String login, String password);
	
	public ITable<User> table(int first,int count)throws PortalException;
	
	
	
	/**
	 * Создать секретный ключ
	 * @param user
	 * @param key
	 * @return
	 * @throws PortalException
	 */
	public UserSecretKey createSecretKey(User user, String key)throws PortalException;
	
	public boolean checkSecretKey(User user, String key, String value)throws PortalException;
	
	public void deleteSecretKey(UserSecretKey key)throws PortalException;
	
	public void deleteSecretKeyIfExists(User user, String key)throws PortalException;
	
	
	

}
