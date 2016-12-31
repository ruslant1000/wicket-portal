package kz.tem.portal.server.register.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.net.URL;
import java.util.List;

import junit.framework.Assert;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import kz.tem.portal.PortalException;
import kz.tem.portal.server.model.Page;
import kz.tem.portal.server.model.Portlet;
import kz.tem.portal.server.model.enums.EnumPageType;
import kz.tem.portal.server.plugin.ModuleConfig;
import kz.tem.portal.server.register.IPageRegister;
import kz.tem.portal.server.register.IPortletRegister;
import kz.tem.portal.utils.ExceptionUtils;
/**
 * 
 * @author Ruslan Temirbulatov
 *
 */
public class PortletRegisterImplTest {

	private static IPageRegister pageRegister;
	private static IPortletRegister portletRegister;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("test.xml");
		pageRegister = ctx.getBean(PageRegisterImpl.class);
		portletRegister = ctx.getBean(PortletRegisterImpl.class);
	}
 
	@Test
	public void test() {
		
		Page page = new Page();
		Portlet p = new Portlet();
		
		try{
			String id = ""+System.currentTimeMillis();
			
			
			page.setLayout("layout");
			page.setPageType(EnumPageType.ADDED);
			page.setTheme("theme");
			page.setTitle("title"+id);
			page.setUrl("url"+id);
			page = pageRegister.addNewPage(page);
			
			ModuleConfig conf  =new ModuleConfig();
			conf.addDefaultConfig("name", "valueФ");
			
			
			p.setPage(page);
			p.setModuleName("moduleName");
			p.setPosition("position");
			p.setSettings(conf.toXML().getBytes());
			p = portletRegister.addPortlet(p);
		
			ModuleConfig conf2 = portletRegister.getPortletSettings(p.getId());
			
			Assert.assertEquals(conf2.toXML(), conf.toXML());
			
		}catch(Exception ex){
			ex.printStackTrace();
			fail("errro");
		}finally{
			try {
				portletRegister.deletePortlet(p.getId());
				pageRegister.deletePage(page.getId());
			} catch (PortalException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
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
