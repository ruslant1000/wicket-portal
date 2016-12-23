package kz.tem.portal.explorer.panel.login;

import kz.tem.portal.api.RegisterEngine;
import kz.tem.portal.explorer.panel.common.form.DefaultInputStatelesForm;
import kz.tem.portal.explorer.panel.common.form.IFormSubmitButtonListener;
import kz.tem.portal.server.model.User;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;

@SuppressWarnings("serial")
public class RegistrationPanel extends Panel{

	private User user;
	private String password2;
	
	public RegistrationPanel(String id) {
		super(id);
		user = new User();
		
		
		
		DefaultInputStatelesForm form = new DefaultInputStatelesForm("form"){

			@Override
			public void onSubmit() throws Exception {
				super.onSubmit();
			}

			@Override
			public void createButtons() {
				addSubmitButton("Зарегистрировать", new IFormSubmitButtonListener() {
					
					@Override
					public void onSubmit() throws Exception {
						System.out.println("Зарегистрировать");
						if(password2!=null && user.getPassword()!=null && !password2.equals(user.getPassword()))
							throw new Exception("Пароли не совпадают");
						
						RegisterEngine.getInstance().getUserRegister().addNewUser(user);
						
					}
				});
			}
			
			
			
		};
		form.addFieldString("e-mail", new PropertyModel<String>(user, "email"), true);
		form.addFieldString("login", new PropertyModel<String>(user, "login"), true);
		form.addFieldPassword("Пароль", new PropertyModel<String>(user, "password"), true);
		form.addFieldPassword("Еще раз пароль", new PropertyModel<String>(RegistrationPanel.this, "password2"), true);
		add(form);
		
	}

}
