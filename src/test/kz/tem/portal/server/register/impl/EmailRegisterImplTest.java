package kz.tem.portal.server.register.impl;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import kz.tem.portal.PortalException;
import kz.tem.portal.server.bean.ITable;
import kz.tem.portal.server.model.Email;
import kz.tem.portal.server.register.IEmailRegister;
import kz.tem.portal.server.register.IUserRegister;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.sun.xml.internal.ws.policy.PolicyException;

public class EmailRegisterImplTest {

private static IEmailRegister register;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("/kz/tem/portal/server/context/test.xml");
		register = ctx.getBean(EmailRegisterImpl.class);
	}
	@Test
	public void test() {
		
		Email e = new Email();
		e.setSubject("test subject");
		e.setRecipient("aa@aa.kz");
		e.setMessage("message".getBytes());
		try {
			register.createEmail(e);
		} catch (Exception e1) {
			e1.printStackTrace();
			fail("ошибка");
		}
	}
	@Test
	public void test2() {
		Email e = new Email();
		e.setSubject("test subject 2");
		e.setRecipient("aa@aa.kz");
		e.setMessage("message".getBytes());
		try {
			e = register.createEmail(e);
			
			e.setSubject("test subject 22");
			register.updateEmail(e);
			
			e.setSubject("test subject 223");
			register.updateEmail(e);
			
			register.showRevisions(e.getId());
		} catch (Exception e1) {
			e1.printStackTrace();
			fail("ошибка");
		}
	}
	@Test
	public void test3() {
		try {
			ITable<Email> table = register.tableEmails(0, 10, "subject", false);
			System.out.println(table.total());
			for(Email e:table.records())
				System.out.println(e.getSubject());
		} catch (PortalException e) {
			e.printStackTrace();
			fail("ошибка");
		}
	}
}
