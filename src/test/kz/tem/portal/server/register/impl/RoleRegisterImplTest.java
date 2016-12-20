package kz.tem.portal.server.register.impl;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import kz.tem.portal.PortalException;
import kz.tem.portal.server.model.Email;
import kz.tem.portal.server.model.Role;
import kz.tem.portal.server.register.IEmailRegister;
import kz.tem.portal.server.register.IRoleRegister;
import kz.tem.portal.server.register.IUserRegister;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.sun.xml.internal.ws.policy.PolicyException;

public class RoleRegisterImplTest {

private static IRoleRegister register;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("/kz/tem/portal/server/context/test.xml");
		register = ctx.getBean(RoleRegisterImpl.class);
	}
	@Test
	public void test() {
		
		Role role = new Role();
		role.setName("role-"+System.currentTimeMillis());
		try {
			register.addNewRole(role);
		} catch (PortalException e) {
			e.printStackTrace();
			fail("ошибка");
		}
	}
}
