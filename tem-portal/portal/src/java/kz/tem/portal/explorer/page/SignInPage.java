package kz.tem.portal.explorer.page;

import kz.tem.portal.server.register.IUserRegister;

import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.flow.RedirectToUrlException;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.Strings;

@SuppressWarnings("serial")
public class SignInPage extends WebPage {

//	
//	@SpringBean
//	private IUserRegister userRegister;  
	
	
	private String username;
	private String password;

	
	public SignInPage(){
		if(AuthenticatedWebSession.get().isSignedIn())
			throw new RedirectToUrlException("/"+RequestCycle.get().getRequest().getContextPath());
	}
	@Override
	protected void onInitialize() {
		super.onInitialize();
		
		StatelessForm form = new StatelessForm("form") {
			@Override
			protected void onSubmit() {
				if (Strings.isEmpty(username))
					return;

				boolean authResult = AuthenticatedWebSession.get().signIn(
						username, password);
				if (authResult)
					continueToOriginalDestination();
				else
					error("�������� ����� ��� ������");
			}
		};

		form.setDefaultModel(new CompoundPropertyModel(this));

		form.add(new TextField("username"));
		form.add(new PasswordTextField("password"));

		add(form);
		add(new FeedbackPanel("feeds"));
	}
}