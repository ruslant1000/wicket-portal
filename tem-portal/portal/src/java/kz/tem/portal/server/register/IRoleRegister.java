package kz.tem.portal.server.register;

import java.io.Serializable;

import kz.tem.portal.PortalException;
import kz.tem.portal.server.bean.ITable;
import kz.tem.portal.server.model.Role;
/**
 * 
 * @author Ruslan Temirbulatov
 *
 */
public interface IRoleRegister extends Serializable{

	public Role addNewRole(Role role)throws PortalException;
	public void updateRole(Role role)throws PortalException;
	
	public void deleteRole(Long id)throws PortalException;
	
	public ITable<Role> table(int first,int count)throws PortalException;
	
	
}
