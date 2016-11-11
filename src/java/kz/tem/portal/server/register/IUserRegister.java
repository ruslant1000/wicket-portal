package kz.tem.portal.server.register;

import java.io.Serializable;

import kz.tem.portal.PortalException;
import kz.tem.portal.server.bean.ITable;
import kz.tem.portal.server.model.User;

public interface IUserRegister extends Serializable{
	
	public void defaults();
	
	public User addNewUser(User user)throws PortalException;
	public void updateUser(User user)throws PortalException;
	public void updateUserPassword(Long userId, String oldPassword,String newPassword)throws PortalException;
	public void deleteUser(Long userId)throws PortalException;
	
	public User authenticateByLogin(String login, String password);
	
	public ITable<User> table(int first,int count)throws PortalException;
	
	
	
	
	

}
