package kz.tem.portal.explorer.panel.admin.users;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import kz.tem.portal.api.NotificationsEngine;
import kz.tem.portal.explorer.panel.common.form.DefaultInputForm;
import kz.tem.portal.server.model.User;
import kz.tem.portal.server.register.IUserRegister;
/**
 * 
 * @author Ruslan Temirbulatov
 *
 */
@SuppressWarnings("serial")
public class UsersControlPanel extends Panel{

	private User user;
	@SpringBean
	private IUserRegister userRegister;
	public UsersControlPanel(String id) {
		super(id);
		user = new User();
		
		DefaultInputForm form = new DefaultInputForm("form"){

			@Override
			public void onSubmit() throws Exception {
				super.onSubmit();
				user = userRegister.addNewUser(user);
				NotificationsEngine.notifyUserRegistered(user);
			}
			
		};
		add(form);
		form.addFieldString("Login", new PropertyModel<String>(user, "login"), true);
		form.addFieldString("Email", new PropertyModel<String>(user, "email"), true);
		form.addFieldString("Password", new PropertyModel<String>(user, "password"), true);
	}

}
