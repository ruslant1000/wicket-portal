package kz.tem.portal.server.register.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.net.URL;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import kz.tem.portal.PortalException;
import kz.tem.portal.server.model.Page;
import kz.tem.portal.server.register.IPageRegister;
import kz.tem.portal.utils.ExceptionUtils;
/**
 * 
 * @author Ruslan Temirbulatov
 *
 */
public class PageRegisterImplTest {

	private static IPageRegister register;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("main.xml");
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
			fail("������ ���� ������ Duplicate entry...");
		} catch (PortalException e) {
			System.out.println(ExceptionUtils.fullError(e));
//			fail(ExceptionUtils.fullError(e));
		}
		try {
			page = new Page();
			page.setUrl(url);
			page.setTitle("title");
			page = register.addNewPage(page);
			fail("������ ���� ������ Duplicate entry...");
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
	@Test
	public void tt() throws PortalException{
		Page page = new Page();
		String key = "-"+System.currentTimeMillis();
		page.setTheme("theme"+key);
		page.setLayout("layout"+key);
		page.setUrl("url"+key);
		page.setTitle("title"+key);
		page = register.addNewPage(page);
		System.out.println(page.getId());
		Long id = page.getId();
		Page page1 = register.getPage(id);
		Page page2 = register.getPage(id);
		
		System.out.println(page1.getId());
		System.out.println(page2.getId());
		
		
		Page p0 = register.pages(0, 0).records().get(0);
		Page p1 = register.pages(0, 0).records().get(0);
		System.out.println(p0.getId());
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
