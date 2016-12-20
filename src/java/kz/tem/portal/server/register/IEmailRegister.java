package kz.tem.portal.server.register;

import java.io.Serializable;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.transaction.annotation.Transactional;

import kz.tem.portal.PortalException;
import kz.tem.portal.server.bean.ITable;
import kz.tem.portal.server.model.Email;
import kz.tem.portal.server.model.enums.EnumEmailStatus;

import com.sun.xml.internal.ws.policy.PolicyException;
public interface IEmailRegister extends Serializable{

	public Email createEmail(Email email)throws PortalException;
	
	public ITable<Email> tableEmails(int first,int count, String sortField, boolean asc, EnumEmailStatus status)throws PortalException;
	public ITable<Email> tableEmails(int first,int count, String sortField, boolean asc)throws PortalException;
	
	public void emailSended(Email email)throws PortalException;
	public void emailError(Email email, String message,Exception ex)throws PortalException;
	
	public Email getEmail(Email email)throws PortalException;
	
	
	
	public void setEmailStatus(Email email, EnumEmailStatus status)throws PortalException;
	public void showRevisions(Long id)throws PortalException;
	public void updateEmail(Email email) throws PortalException; 
}
