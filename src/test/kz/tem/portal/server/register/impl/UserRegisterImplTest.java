package kz.tem.portal.server.register.impl;

import static org.junit.Assert.*;

import java.net.URL;
import java.util.List;
import java.util.Map;

import kz.tem.portal.PortalException;
import kz.tem.portal.server.model.Page;
import kz.tem.portal.server.model.User;
import kz.tem.portal.server.register.IPageRegister;
import kz.tem.portal.server.register.IUserRegister;
import kz.tem.portal.utils.ExceptionUtils;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
/**
 * 
 * @author Ruslan Temirbulatov
 *
 */
public class UserRegisterImplTest {

	private static IUserRegister register;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("/kz/tem/portal/server/context/main.xml");
		register = ctx.getBean(UserRegisterImpl.class);
	}
 
	@Test
	public void test() {
		
		register.defaults();
		User user = UserRegisterImpl.instance.authenticateByLogin("admin", "admin");
//		User user = register.authenticateByLogin("admin", "admin");
		System.out.println(user);
//		User user = new User();
//		String id = ""+System.currentTimeMillis();
//		user.setEmail("portal"+id+"@example.com");
//		user.setLogin("admin"+id);
//		user.setPassword("admin");
//		try {
//			register.addNewUser(user);
//		} catch (PortalException e) {
//			e.printStackTrace();
//			fail(e.getMessage());
//		}
		
//		((UserRegisterImpl)register).init();
	}
}
