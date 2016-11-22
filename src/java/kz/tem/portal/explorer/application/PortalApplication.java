package kz.tem.portal.explorer.application;

import java.net.URL;
import java.util.Iterator;
import java.util.Map;

import org.apache.wicket.Page;
import org.apache.wicket.application.AbstractClassResolver;
import org.apache.wicket.application.IClassResolver;
import org.apache.wicket.authroles.authentication.AbstractAuthenticatedWebSession;
import org.apache.wicket.authroles.authentication.AuthenticatedWebApplication;
import org.apache.wicket.core.util.lang.PropertyResolver;
import org.apache.wicket.core.util.lang.PropertyResolver.IGetAndSet;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.resource.IResource;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.apache.wicket.util.time.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import kz.tem.portal.api.PortalEngine;
import kz.tem.portal.explorer.page.AbstractThemePage;
import kz.tem.portal.explorer.page.AccessDeniedPage;
import kz.tem.portal.explorer.page.AuthenticatePage;
import kz.tem.portal.explorer.page.LoginPage;
import kz.tem.portal.explorer.page.SignOutPage;
import kz.tem.portal.explorer.page.admin.pages.PagesConfig;
import kz.tem.portal.explorer.page.admin.portlets.PortletsConfig;
import kz.tem.portal.explorer.page.admin.settings.SettingsPage;
import kz.tem.portal.explorer.page.admin.users.UsersPage;
import kz.tem.portal.explorer.services.FileUploadService;
import kz.tem.portal.explorer.services.TestService;
import kz.tem.portal.server.plugin.engine.ModuleEngine;
/**
 * 
 * @author Ruslan Temirbulatov
 *
 */
public class PortalApplication extends AuthenticatedWebApplication {

	@Autowired
	private ApplicationContext context;
	
	@Override
	protected void init() {
		super.init();
		getComponentInstantiationListeners().add(
				new SpringComponentInjector(this));
		getResourceSettings().setResourceStreamLocator(
				new PortalStreamLocator());

		// WebApplication.get().getMarkupSettings().getMarkupFactory().getMarkupCache().shutdown();
		// getDebugSettings().setComponentUseCheck(false);

		//**************************
		// Это нужно для того, чтобы PropertyModel в модулях работал корректно.
		// Иначе при ПОВТОРНОМ деплое модуля будет вылетать ошибка 
		//    java.lang.IllegalArgumentException: Can not set java.lang.String field...
		// Т.е. PropertyResolver кэширует объект модели и потом уже оперирует устаревшим объектом во вновь загруженном модуле.
		PropertyResolver.setClassCache(this, new PropertyResolver.IClassCache() {
			
			@Override
			public void put(Class<?> clz, Map<String, IGetAndSet> values) {
			}
			
			@Override
			public Map<String, IGetAndSet> get(Class<?> clz) {
				return null;
			}
		});
		//**************************
		
		
		PortalEngine.getInstance().getExplorerEngine().initLayouts(this);
		PortalEngine.getInstance().getExplorerEngine().initThemes(this);

		// getPageSettings().setRecreateBookmarkablePagesAfterExpiry(true);

		mountPage("authenticate", AuthenticatePage.class);
		mountPage("login", LoginPage.class);
		
		mountPage("logout", SignOutPage.class);
		mountPage("accessdenied", AccessDeniedPage.class);
		
		mountPage("pg/${seg1}", AbstractThemePage.class);

		mountPage("admin/pages", PagesConfig.class);
		mountPage("admin/portlets", PortletsConfig.class);
		mountPage("admin/settings", SettingsPage.class);
		mountPage("admin/users", UsersPage.class);
		
		
	
		//TODO  надо будет отключить при вводе в эксплуатацию, потому что это нарушает систему безопасности. Либо добавить сервисам авторизацию
		mountResource("test","services/test", new TestService());
		mountResource("upload","services/upload", new FileUploadService());
		

		System.out.println("@@@  "
				+ getFrameworkSettings().getSerializer().getClass().getName());
		System.out.println("@@@  "
				+ getApplicationSettings().getClassResolver().getClass()
						.getName());

//		getApplicationSettings().setClassResolver(new PortalClassResolver());
		
		
		

		// getFrameworkSettings().setSerializer(new JavaSerializer(
		// getApplicationKey() ){
		//
		//
		// @Override
		// public byte[] serialize(Object object) {
		// System.out.println("    serialize "+object.getClass().getName());
		// return super.serialize(object);
		// }
		//
		// @Override
		// public Object deserialize(byte[] data) {
		// try{
		// return super.deserialize(data);
		// }catch(Exception ex){
		// ex.printStackTrace();
		// System.out.println("!!!!!!!!!!!!!!!!!!!!! error");
		// return null;
		// }
		// }
		//
		//
		// });
		//
		//
		// getFrameworkSettings().setSerializer(new ISerializer() {
		//
		// @Override
		// public byte[] serialize(Object object) {
		// // TODO Auto-generated method stub
		// return null;
		// }
		//
		// @Override
		// public Object deserialize(byte[] data) {
		// // TODO Auto-generated method stub
		// return null;
		// }
		// });
		// getResourceSettings().setResourceStreamLocator(new
		// CustomResourceStreamLocator());
		
		
		
	}

	@Override
	public Class<? extends Page> getHomePage() {
		return AbstractThemePage.class;
	}

	private class PortalClassResolver implements IClassResolver {

		private AbstractClassResolver res = new AbstractClassResolver() {

			@Override
			public ClassLoader getClassLoader() {
				ClassLoader loader = Thread.currentThread()
						.getContextClassLoader();
				if (loader == null) {
					loader = ModuleEngine.getInstance().getClassLoader(
							"ftpclient");
					// loader = DefaultClassResolver.class.getClassLoader();
				}
				return loader;
			}
		};

		@Override
		public Class<?> resolveClass(String classname)
				throws ClassNotFoundException {

			try {
				return res.resolveClass(classname);
			} catch (ClassNotFoundException ex) {
				Class c = ModuleEngine.getInstance()
						.getClassLoader("ftpclient").findClass(classname);
				if (c == null)
					throw new ClassNotFoundException(classname);
				return c;
				// throw ex;
			}
		}

		@Override
		public Iterator<URL> getResources(String name) {
			return res.getResources(name);
		}

		@Override
		public ClassLoader getClassLoader() {
			return res.getClassLoader();
		}

	}

	
	public void mountResource(String key,String path,  final IResource resource){
		ResourceReference ref= new ResourceReference(key){
			private static final long serialVersionUID = 1L;

			@Override
			public IResource getResource() {
				return resource;
			}};
		mountResource(path, ref);
	}
	
	public static PortalApplication get() {
		return (PortalApplication) WebApplication.get();
	}

	/**
	 * � ������ Page �������� ������ ������������� URL. �������� 'suburl1'. �
	 * ��� ���� ����� �������� ������ �������� URL ����� �������� ��� � ����
	 * '/<portal-context-path>/pg/suburl1'. �������� '/portal/pg/suburl1'
	 * 
	 * @param page
	 * @return
	 */
	public static String toPortalUrl(kz.tem.portal.server.model.Page page) {
		return RequestCycle.get().getRequest().getContextPath() + "/pg/"
				+ page.getUrl();
	}

	@Override
	protected Class<? extends WebPage> getSignInPageClass() {
		return AuthenticatePage.class;
	}

	@Override
	protected Class<? extends AbstractAuthenticatedWebSession> getWebSessionClass() {
		return PortalSession.class;
	}
}
