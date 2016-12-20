package kz.tem.portal.server.register.impl;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import kz.tem.portal.PortalException;
import kz.tem.portal.explorer.application.PortalSession;
import kz.tem.portal.server.bean.ITable;
import kz.tem.portal.server.model.Email;
import kz.tem.portal.server.model.enums.EnumEmailStatus;
import kz.tem.portal.server.register.IEmailRegister;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQuery;
import org.springframework.transaction.annotation.Transactional;

@SuppressWarnings("serial")
@Transactional
public class EmailRegisterImpl extends AbstractRegister implements IEmailRegister{

	
	@Override
	public Email createEmail(Email email) throws PortalException {
		System.out.println("\n   CREATE EMAIL\n"
				+ "   "+PortalSession.get().getUser());
		email.setStatus(EnumEmailStatus.CREATED);
		email.setCreated(new Date());
		ht.persist(email);
		return email;
	}

	@Override
	public void updateEmail(Email email) throws PortalException {
		ht.merge(email);
		
	}
	@Override
	public void showRevisions(Long id) throws PortalException {
		AuditQuery aq = AuditReaderFactory.get(ht.getSessionFactory().getCurrentSession()).createQuery().forRevisionsOfEntity(Email.class, false, false);
		aq.add(AuditEntity.id().eq(id));
		List<Object[]> list = aq.getResultList();
		if(list!=null)
			for(Object[] e:list){
				Email em = (Email)e[0];
				System.out.println(em.getSubject());
				System.out.println(e[1]);
				System.out.println(e[2]);
//				System.out.println(e[1]);
			}
//		AuditQuery aq = 
		
	}

	

	@Override
	public ITable<Email> tableEmails(int first, int count, String sortField,
			boolean asc, EnumEmailStatus status) throws PortalException {
		List<Criterion> crits = null;
		if(status!=null){
			crits = new LinkedList<Criterion>();
			crits.add(Restrictions.eq("status", status));
		}
		return getTable(Email.class, first, count, crits, sortField, asc);
	}

	@Override
	public ITable<Email> tableEmails(int first, int count, String sortField,
			boolean asc) throws PortalException {
		return tableEmails(first, count, sortField, asc,null);
	}

	@Override
	public void setEmailStatus(Email email, EnumEmailStatus status)
			throws PortalException {
		email.setStatus(status);
		ht.merge(email);
		
	}

	@Override
	public void emailSended(Email email) throws PortalException {
		email.setStatus(EnumEmailStatus.SENDED);
		email.setSended(new Date());
		ht.merge(email);
	}

	@Override
	public void emailError(Email email, String message, Exception ex)
			throws PortalException {
		email.setStatus(EnumEmailStatus.SEND_ERROR);
		email.setSendErrorMessage(message);
		if(ex!=null){
			StringWriter errors = new StringWriter();
			ex.printStackTrace(new PrintWriter(errors));
			email.setSendErrorTrace(errors.toString().getBytes());
		}
		ht.merge(email);
	}

	@Override
	public Email getEmail(Email email) throws PortalException {
		email = ht.get(Email.class, email.getId());
		email.getMessage();
		email.getSendErrorTrace();
		return email;
	}


}
