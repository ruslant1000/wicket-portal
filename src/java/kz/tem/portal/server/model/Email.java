package kz.tem.portal.server.model;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import kz.tem.portal.server.model.enums.EnumEmailStatus;

import org.hibernate.envers.Audited;


@SuppressWarnings("serial")
@Entity
@Table(name="PT_EMAIL")
@Audited
public class Email extends IdEntity{

	private String subject;
	
	private byte[] message;
	
	private String sendErrorMessage;
	private byte[] sendErrorTrace;
	
	private String recipient;
	
	private EnumEmailStatus status;
	
	private Date created;
	
	private Date sended;

	@Column(nullable=false)
	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}
	@Lob
	@Basic(fetch=FetchType.LAZY)
	public byte[] getMessage() {
		return message;
	}

	public void setMessage(byte[] message) {
		this.message = message;
	}
	@Column(nullable=false)
	public String getRecipient() {
		return recipient;
	}

	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}

	@Enumerated(EnumType.STRING)
	@Column(nullable=false)
	public EnumEmailStatus getStatus() {
		return status;
	}

	public void setStatus(EnumEmailStatus status) {
		this.status = status;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable=false)
	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}
	@Temporal(TemporalType.TIMESTAMP)
	public Date getSended() {
		return sended;
	}

	public void setSended(Date sended) {
		this.sended = sended;
	}

	public String getSendErrorMessage() {
		return sendErrorMessage;
	}

	public void setSendErrorMessage(String sendErrorMessage) {
		this.sendErrorMessage = sendErrorMessage;
	}
	@Lob
	@Basic(fetch=FetchType.LAZY)
	public byte[] getSendErrorTrace() {
		return sendErrorTrace;
	}

	public void setSendErrorTrace(byte[] sendErrorTrace) {
		this.sendErrorTrace = sendErrorTrace;
	}
	
	
	
	
	
	
	
	
	
}
