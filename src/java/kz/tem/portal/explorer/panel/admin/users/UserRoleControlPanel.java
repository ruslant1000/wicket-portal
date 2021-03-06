package kz.tem.portal.explorer.panel.admin.users;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import kz.tem.portal.PortalException;
import kz.tem.portal.explorer.panel.common.form.DefaultInputForm;
import kz.tem.portal.server.model.Role;
import kz.tem.portal.server.model.User;
import kz.tem.portal.server.register.IRoleRegister;
import kz.tem.portal.server.register.IUserRegister;
/**
 * 
 * @author Ruslan Temirbulatov
 *
 */
@SuppressWarnings("serial")
public class UserRoleControlPanel extends Panel{

	@SpringBean
	private IUserRegister userRegister;
	@SpringBean
	private IRoleRegister roleRegister;
	
	private User user;
	private Role role;
	
	public UserRoleControlPanel(String id) {
		super(id);
		DefaultInputForm form = new DefaultInputForm("form"){

			@Override
			public void onSubmit() throws Exception {
				super.onSubmit();
				user.getRole().add(role);
				userRegister.updateUser(user);
			}

			@Override
			public void build() throws Exception {
				super.build();
				addCombobox("User", new PropertyModel<User>(UserRoleControlPanel.this, "user"), userRegister.table(0, 0).records(), true);
				addCombobox("Role", new PropertyModel<Role>(UserRoleControlPanel.this, "role"), roleRegister.table(0, 0).records(), true);
			}
			
			
		};
		add(form);

	}

}
