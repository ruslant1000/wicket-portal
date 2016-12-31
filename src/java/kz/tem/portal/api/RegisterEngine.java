package kz.tem.portal.api;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kz.tem.portal.server.register.IEmailRegister;
import kz.tem.portal.server.register.IPageRegister;
import kz.tem.portal.server.register.ISettingsRegister;
import kz.tem.portal.server.register.IUserRegister;
/**
 * 
 * @author Ruslan Temirbulatov
 *
 */
@SuppressWarnings("serial")
public class RegisterEngine implements Serializable{
	
	private static Logger log = LoggerFactory.getLogger(RegisterEngine.class);

	private static RegisterEngine instance = null;
	
	private IUserRegister userRegister;
	private ISettingsRegister settingsRegister;
	private IEmailRegister emailRegister;
	private IPageRegister pageRegister;
	
	private RegisterEngine(){
		instance = this;
		log.debug("Create RegisterEngine");
	}
	
	public static RegisterEngine getInstance(){
		if(instance==null)
			new RegisterEngine();
		return instance;
	}

	public IUserRegister getUserRegister() {
		return userRegister;
	}

	public void setUserRegister(IUserRegister userRegister) {
		this.userRegister = userRegister;
	}

	public ISettingsRegister getSettingsRegister() {
		return settingsRegister;
	}

	public void setSettingsRegister(ISettingsRegister settingsRegister) {
		this.settingsRegister = settingsRegister;
	}

	public IEmailRegister getEmailRegister() {
		return emailRegister;
	}

	public void setEmailRegister(IEmailRegister emailRegister) {
		this.emailRegister = emailRegister;
	}

	public IPageRegister getPageRegister() {
		return pageRegister;
	}

	public void setPageRegister(IPageRegister pageRegister) {
		this.pageRegister = pageRegister;
	}
	
	
}
