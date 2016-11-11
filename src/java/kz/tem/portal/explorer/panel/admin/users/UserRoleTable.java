package kz.tem.portal.explorer.panel.admin.users;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.spring.injection.annot.SpringBean;

import kz.tem.portal.PortalException;
import kz.tem.portal.explorer.panel.admin.common.RolesPanel;
import kz.tem.portal.explorer.panel.common.component.AjaxDeletableItem;
import kz.tem.portal.explorer.panel.common.table.AColumn;
import kz.tem.portal.explorer.panel.common.table.AbstractTable;
import kz.tem.portal.server.bean.ITable;
import kz.tem.portal.server.model.Role;
import kz.tem.portal.server.model.User;
import kz.tem.portal.server.register.IUserRegister;

@SuppressWarnings("serial")
public class UserRoleTable extends AbstractTable<User>{

	@SpringBean
	private IUserRegister userRegister;
	
	public UserRoleTable(String id) {
		super(id, true);
	}

	
	@Override
	public Component before(String id) {
		return new UserRoleControlPanel(id);
	}


	@Override
	public ITable<User> data(int first, int count) throws Exception {
		return userRegister.table(first, count);
	}

	@Override
	public AColumn[] columns() throws Exception {
		return new AColumn[]{
				new AColumn<User>("User"){

					@Override
					public Component cell(String id, User record)
							throws Exception {
						return new Label(id,record.getLogin());
				}},
				new AColumn<User>("Roles"){

					@Override
					public Component cell(String id, final User record)
							throws Exception {
						return new RolesPanel(id, record.getRole()){

							@Override
							public void roleDeleted(Role role) {
								super.roleDeleted(role);
								record.getRole().remove(role);
								try {
									userRegister.updateUser(record);
								} catch (PortalException e) {
									e.printStackTrace();
									throw new RuntimeException(e);
								}
							}
							
						};
				}}
					
		};
	}

}
