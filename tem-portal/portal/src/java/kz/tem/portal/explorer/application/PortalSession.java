package kz.tem.portal.explorer.application;

import kz.tem.portal.api.RegisterEngine;
import kz.tem.portal.server.model.User;
import kz.tem.portal.server.register.IUserRegister;
import kz.tem.portal.server.register.impl.UserRegisterImpl;

import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.request.Request;
import org.apache.wicket.spring.injection.annot.SpringBean;

@SuppressWarnings("serial")
public class PortalSession extends AuthenticatedWebSession{

	private User user = null;
	
	public PortalSession(Request request) {
		super(request);
	}

	@Override
	protected boolean authenticate(String username, String password) {
		user = RegisterEngine.getInstance().getUserRegister().authenticateByLogin(username, password);
//		user = UserRegisterImpl.instance.authenticateByLogin(username, password);
		return user!=null;
//		return username.equals(password);
	}

	@Override
	public Roles getRoles() {
		// TODO Auto-generated method stub
		return null;
	}

}
