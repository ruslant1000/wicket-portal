package kz.tem.portal.server.register.impl;

import java.io.Serializable;

public interface InSessionAction<T> extends Serializable{
	
	public void action(T entity)throws Exception;

}
