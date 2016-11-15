package kz.tem.portal.explorer.panel.login;

import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.util.string.Strings;

@SuppressWarnings("serial")
public class LoginPanel extends Panel{

	private String username;
	private String password;
	
	public LoginPanel(String id) {
		super(id);
		StatelessForm form = new StatelessForm("form") {
			@Override
			protected void onSubmit() {
				if (Strings.isEmpty(username))
					return;

				boolean authResult = AuthenticatedWebSession.get().signIn(
						username, password);
				if (authResult)
					onLogin();
//					continueToOriginalDestination();
				else
					error("Неверный логин или пароль");
			}
		};

		form.setDefaultModel(new CompoundPropertyModel(this));

		form.add(new TextField("username"));
		form.add(new PasswordTextField("password"));

		add(form);
		add(new FeedbackPanel("feeds"));
	}

	public void onLogin(){
		
	}

}
