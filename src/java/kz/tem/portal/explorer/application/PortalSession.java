package kz.tem.portal.explorer.application;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.request.Request;

import kz.tem.portal.api.RegisterEngine;
import kz.tem.portal.server.model.Role;
import kz.tem.portal.server.model.User;
/**
 * 
 * @author Ruslan Temirbulatov
 *
 */
@SuppressWarnings("serial")
public class PortalSession extends AuthenticatedWebSession{

	private User user = null;
	private Roles roles = null;
	
	private Map<String, Object> objects = new HashMap<String, Object>();
	
	
	public Map<String, Object> getObjects() {
		return objects;
	}

	public void setObjects(Map<String, Object> objects) {
		this.objects = objects;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public PortalSession(Request request) {
		super(request);
	}
	
	public static PortalSession get(){
		return (PortalSession)AuthenticatedWebSession.get();
	}

	public static boolean isAdmin(){
		return get().getUser()!=null && get().getUser().getLogin().equals("admin");
	}
	@Override
	protected boolean authenticate(String username, String password) {
		user = RegisterEngine.getInstance().getUserRegister().authenticateByLogin(username, password);
		if(user!=null){
			roles = new Roles();
			for(Role r:user.getRole())
				roles.add(r.getName());
		} 
		return user!=null;
		
	}

	@Override
	public Roles getRoles() {
		return roles;
	}
	
	public boolean access(Set<Role> role){
		if(roles!=null && role!=null && role.size()>0){
			for(Role r:role){
				if (roles.hasRole(r.getName()))
					return true;
			}
		}
		return false;
	}

}
