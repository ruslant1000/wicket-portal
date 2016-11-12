package kz.tem.portal.server.register.impl;

import static org.junit.Assert.*;

import java.net.URL;
import java.util.List;
import java.util.Map;

import kz.tem.portal.PortalException;
import kz.tem.portal.server.model.Page;
import kz.tem.portal.server.register.IPageRegister;
import kz.tem.portal.utils.ExceptionUtils;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
/**
 * 
 * @author Ruslan Temirbulatov
 *
 */
public class PageRegisterImplTest {

	private static IPageRegister register;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("/kz/tem/portal/server/context/main.xml");
		register = ctx.getBean(PageRegisterImpl.class);
	}
 
	@Test
	public void test() {
		String url = "url"+System.currentTimeMillis();
		Page page = new Page();
		page.setUrl(url);
		page.setTitle("title");
		try {
			page = register.addNewPage(page);
		} catch (PortalException e) {
			fail(ExceptionUtils.fullError(e));
		}
		
		try {
			page.setTitle("ttt");
			register.savePage(page);
		} catch (PortalException e) {
			fail(ExceptionUtils.fullError(e));
		}
		try {
			Page page2 = register.getPage(page.getId());
			System.out.println(page2.getTitle()+" = "+ page.getTitle());
			assertEquals(page2.getTitle(), page.getTitle());
		} catch (PortalException e) {
			fail(ExceptionUtils.fullError(e));
		}
		try {
			page = new Page();
			page.setUrl(url);
			page.setTitle("title");
			page = register.addNewPage(page);
			fail("Должна быть ошибка Duplicate entry...");
		} catch (PortalException e) {
			System.out.println(ExceptionUtils.fullError(e));
//			fail(ExceptionUtils.fullError(e));
		}
		try {
			page = new Page();
			page.setUrl(url);
			page.setTitle("title");
			page = register.addNewPage(page);
			fail("Должна быть ошибка Duplicate entry...");
		} catch (PortalException e) {
			System.out.println(ExceptionUtils.fullError(e));
//			fail(ExceptionUtils.fullError(e));
		}
//		fail("Not yet implemented");
	}

	@Test
	public void test2() {
		String url = "2url"+System.currentTimeMillis();
		Page page = new Page();
		page.setUrl(url);
		page.setTitle("title");
		try {
			page = register.addNewPage(page);
			register.deletePage(page.getId());
			page=register.getPage(page.getId());
			assertNull(page);
			
		} catch (PortalException e) {
			e.printStackTrace();
			fail(ExceptionUtils.fullError(e));
		}
		
	}
	
	@Test
	public void testTree() {
		try {
			List<Page> map = register.pagesTree();
			for(Page p1:map){
				System.out.println(p1.getTitle());
				for(Page p2:p1.getChilds())
					System.out.println("   "+p2.getTitle());
			}
		} catch (PortalException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	public static void main(String[] args) {
		URL url = Thread.currentThread().getContextClassLoader()
			    .getResource(
			    "org/hibernate/cache/infinispan/StrategyRegistrationProviderImpl.class");
			System.out.println(url);
			
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("/kz/tem/portal/server/context/main.xml");
//		register = ctx.getBean(PageRegisterImpl.class);
	}
}
