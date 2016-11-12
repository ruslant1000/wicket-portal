package kz.tem.portal.explorer.panel.admin.users;

import kz.tem.portal.PortalException;
import kz.tem.portal.explorer.panel.common.form.DefaultInputForm;
import kz.tem.portal.server.model.Role;
import kz.tem.portal.server.model.User;
import kz.tem.portal.server.register.IRoleRegister;
import kz.tem.portal.server.register.IUserRegister;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
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
			
		};
		add(form);
		try {
			form.addCombobox("User", new PropertyModel<User>(UserRoleControlPanel.this, "user"), userRegister.table(0, 0).records(), true);
			form.addCombobox("Role", new PropertyModel<Role>(UserRoleControlPanel.this, "role"), roleRegister.table(0, 0).records(), true);
		} catch (PortalException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

}
