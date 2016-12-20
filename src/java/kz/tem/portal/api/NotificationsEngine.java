package kz.tem.portal.api;

import kz.tem.portal.explorer.application.PortalSession;
import kz.tem.portal.server.jobs.NotificationsJob;
import kz.tem.portal.server.model.Email;
import kz.tem.portal.server.model.User;

public class NotificationsEngine {

	public static NotificationsEngine instance = null;
	
	public static NotificationsEngine get(){
		if(instance==null)
			instance = new NotificationsEngine();
		return instance;
	}
	
	private NotificationsEngine(){}
	
	/**
	 * Email-уведомление пользователя о регистрации на портале и необходимости подтвердить свою регистрацию
	 * @param user
	 * @throws Exception
	 */
	public static void notifyUserRegistered(User user)throws Exception{
		
		System.out.println("USER SESSION "+PortalSession.get().getUser());
		
		if(user==null || user.getEmail()==null)
			throw new Exception("Не определен email адресата");
		Email email = new Email();
		email.setRecipient(user.getEmail());
		email.setSubject("Регистрация на портале");
		email.setMessage("Поздравляем!<br/>Вы зарегистрированы на нашем портале!!!<br/>С Уважением, Администрация портала".getBytes());
//		email = RegisterEngine.getInstance().getEmailRegister().createEmail(email);
//		new Thread(new EmailSendRunnable(email)).start();
	}
	
	
}
class EmailSendRunnable implements Runnable{
	private Email email;
	public EmailSendRunnable(Email email){
		this.email=email;
	}

	@Override
	public void run() {
		NotificationsJob.sendEmail(email);
	}
	
}