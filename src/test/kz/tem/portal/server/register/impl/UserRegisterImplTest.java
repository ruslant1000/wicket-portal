package kz.tem.portal.server.register.impl;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import kz.tem.portal.server.model.User;
import kz.tem.portal.server.register.IUserRegister;
/**
 * 
 * @author Ruslan Temirbulatov
 *
 */
public class UserRegisterImplTest {

	private static IUserRegister register;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("/kz/tem/portal/server/context/test.xml");
		register = ctx.getBean(UserRegisterImpl.class);
	}
 
	@Test
	public void test() {
		
		register.defaults();
		User user = register.authenticateByLogin("admin", "admin");
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
