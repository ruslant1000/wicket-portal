package kz.tem.portal.api;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kz.tem.portal.server.register.IUserRegister;

@SuppressWarnings("serial")
public class RegisterEngine implements Serializable{
	
	private static Logger log = LoggerFactory.getLogger(RegisterEngine.class);

	private static RegisterEngine instance = null;
	
	private IUserRegister userRegister;
	
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
	
	
}
