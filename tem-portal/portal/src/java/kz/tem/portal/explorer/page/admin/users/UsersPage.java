package kz.tem.portal.explorer.page.admin.users;

import kz.tem.portal.explorer.page.AdminPage;
import kz.tem.portal.explorer.panel.admin.users.RolesTable;
import kz.tem.portal.explorer.panel.admin.users.UserRoleTable;
import kz.tem.portal.explorer.panel.admin.users.UsersTable;
/**
 * 
 * @author Ruslan Temirbulatov
 *
 */
@SuppressWarnings("serial")
public class UsersPage extends AdminPage{
	
	public UsersPage(){
		add(new RolesTable("roles"));
		add(new UsersTable("users"));
		add(new UserRoleTable("user-role"));
	}

}
