package kz.tem.portal.server.register.impl;

import java.io.Serializable;

import kz.tem.portal.server.model.RevisionEntity;

public interface InSessionAuditAction<T> extends Serializable{
	
	public void action(T entity, RevisionEntity revisionEntity)throws Exception;

}
