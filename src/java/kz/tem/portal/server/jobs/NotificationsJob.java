package kz.tem.portal.server.jobs;


import kz.tem.portal.PortalException;
import kz.tem.portal.api.ExplorerEngine;
import kz.tem.portal.api.RegisterEngine;
import kz.tem.portal.server.bean.ITable;
import kz.tem.portal.server.model.Email;
import kz.tem.portal.server.model.enums.EnumEmailStatus;
import kz.tem.utils.smtp.SmtpUtils;

public class NotificationsJob {

	public static synchronized void sendEmail(Email email){
		try{
			email = RegisterEngine.getInstance().getEmailRegister().getEmail(email);
			if(email.getStatus()!=EnumEmailStatus.CREATED)
				return;
			String smtpHost = ExplorerEngine.getInstance().getSettingsValue(ExplorerEngine.SETTINGS_SMTP_HOST);
			String smtpPort = ExplorerEngine.getInstance().getSettingsValue(ExplorerEngine.SETTINGS_SMTP_PORT);
			String senderEmail = ExplorerEngine.getInstance().getSettingsValue(ExplorerEngine.SETTINGS_SMTP_USER);
			String senderPassword = ExplorerEngine.getInstance().getSettingsValue(ExplorerEngine.SETTINGS_SMTP_PASSWORD);
			SmtpUtils.sendMailHtml(smtpHost, smtpPort, senderEmail, senderPassword, email.getRecipient(), email.getSubject(), new String(email.getMessage()));
			RegisterEngine.getInstance().getEmailRegister().emailSended(email);
		}catch(Exception ex){
			try {
				RegisterEngine.getInstance().getEmailRegister().emailError(email, "Ошибка при отправке сообщения", ex);
			} catch (PortalException e) {
				e.printStackTrace();
			}
		}
	} 
	
	public static void sendEmails()throws Exception{
		System.out.println("send emails...");
		
		ITable<Email> emails = RegisterEngine.getInstance().getEmailRegister().tableEmails(0, 10, "created", true, EnumEmailStatus.CREATED);
		for(Email email:emails.records()){
			sendEmail(email);
			Thread.sleep(1000);
		}
	}
	
	public static synchronized void test(String x) throws InterruptedException{
		System.out.println("aa "+x);
		Thread.sleep(1000);
	}
	public static void main(String[] args) throws InterruptedException {
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				for(int x=0;x<5;x++)
					try {
						test("11");
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				
			}
		}).start();
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				for(int x=0;x<5;x++)
					try {
						test("22");
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				
			}
		}).start();
		
	}
}
