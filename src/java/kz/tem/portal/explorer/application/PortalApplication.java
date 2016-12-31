package kz.tem.portal.explorer.application;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectInputValidation;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.OutputStream;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;

import kz.tem.portal.PortalException;
import kz.tem.portal.api.PortalEngine;
import kz.tem.portal.api.RegisterEngine;
import kz.tem.portal.explorer.page.AbstractThemePage;
import kz.tem.portal.explorer.page.AccessDeniedPage;
import kz.tem.portal.explorer.page.AuthenticatePage;
import kz.tem.portal.explorer.page.NewPasswordErrorPage;
import kz.tem.portal.explorer.page.NewPasswordPage;
import kz.tem.portal.explorer.page.NewPasswordSuccessPage;
import kz.tem.portal.explorer.page.RegistrationConfirmationErrorPage;
import kz.tem.portal.explorer.page.RegistrationConfirmationPage;
import kz.tem.portal.explorer.page.RegistrationConfirmationSuccessPage;
import kz.tem.portal.explorer.page.RememberPasswordPage;
import kz.tem.portal.explorer.page.SignOutPage;
import kz.tem.portal.explorer.page.admin.emails.EmailsPage;
import kz.tem.portal.explorer.page.admin.pages.PagesConfig;
import kz.tem.portal.explorer.page.admin.portlets.PortletsConfig;
import kz.tem.portal.explorer.page.admin.settings.SettingsPage;
import kz.tem.portal.explorer.page.admin.users.UsersPage;
import kz.tem.portal.explorer.page.login.RegistrationPage;
import kz.tem.portal.explorer.services.FileUploadService;
import kz.tem.portal.explorer.services.TestService;
import kz.tem.portal.server.model.enums.EnumPageType;
import kz.tem.portal.server.plugin.engine.JarClassLoader;
import kz.tem.portal.server.plugin.engine.ModuleEngine;

import org.apache.wicket.Application;
import org.apache.wicket.Page;
import org.apache.wicket.ThreadContext;
import org.apache.wicket.application.AbstractClassResolver;
import org.apache.wicket.application.IClassResolver;
import org.apache.wicket.authroles.authentication.AbstractAuthenticatedWebSession;
import org.apache.wicket.authroles.authentication.AuthenticatedWebApplication;
import org.apache.wicket.core.util.lang.PropertyResolver;
import org.apache.wicket.core.util.lang.PropertyResolver.IGetAndSet;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.pageStore.AbstractPageStore;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.resource.IResource;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.serialize.java.JavaSerializer;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.apache.wicket.util.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

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
		SpringComponentInjector sci = new SpringComponentInjector(this);
		getComponentInstantiationListeners().add(sci);
		
		
		getResourceSettings().setResourceStreamLocator(
				new PortalStreamLocator());

		// WebApplication.get().getMarkupSettings().getMarkupFactory().getMarkupCache().shutdown();
		// getDebugSettings().setComponentUseCheck(false);

		// **************************
		// Это нужно для того, чтобы PropertyModel в модулях работал корректно.
		// Иначе при ПОВТОРНОМ деплое модуля будет вылетать ошибка
		// java.lang.IllegalArgumentException: Can not set java.lang.String
		// field...
		// Т.е. PropertyResolver кэширует объект модели и потом уже оперирует
		// устаревшим объектом во вновь загруженном модуле.
		PropertyResolver.setClassCache(this,
				new PropertyResolver.IClassCache() {

					@Override
					public void put(Class<?> clz, Map<String, IGetAndSet> values) {
					}

					@Override
					public Map<String, IGetAndSet> get(Class<?> clz) {
						return null;
					}
				});
		// JavaSerializer.
		// **************************

		PortalEngine.getInstance().getExplorerEngine().initLayouts(this);
		PortalEngine.getInstance().getExplorerEngine().initThemes(this);

		// getPageSettings().setRecreateBookmarkablePagesAfterExpiry(true);

//		mountPage("login", AuthenticatePage.class);
		mountSystemPage("login","Аутентификация", AuthenticatePage.class);
//		mountPage("login", LoginPage.class);
		mountSystemPage("registration","Регистрация", RegistrationPage.class);
		
		mountSystemPage(RegistrationConfirmationSuccessPage.PAGE_RESPONCE_SUCCESS_URL,"Успешное подтверждение регистрации", RegistrationConfirmationSuccessPage.class);
		mountSystemPage(RegistrationConfirmationErrorPage.PAGE_RESPONCE_ERROR_URL, "Ошибка подтверждения регистрации",RegistrationConfirmationErrorPage.class);
		mountSystemPage(RememberPasswordPage.REMEMBER_PASSWORD_PAGE_URL, "Восстановление пароля",RememberPasswordPage.class);
		mountSystemPage(NewPasswordSuccessPage.NEW_PASSWORD_SUCCESS_PAGE_URL, "Восстановление пароля",NewPasswordSuccessPage.class);
		mountSystemPage(NewPasswordErrorPage.NEW_PASSWORD_ERROR_PAGE_URL, "Ошибка при восстановлении пароля",NewPasswordErrorPage.class);
		
		mountPage(RegistrationConfirmationPage.PAGE_REQUEST_URL, RegistrationConfirmationPage.class);
		mountPage(NewPasswordPage.NEW_PASSWORD_PAGE_URL, NewPasswordPage.class);

		mountPage("logout", SignOutPage.class);
		mountPage("accessdenied", AccessDeniedPage.class);

		// mount(new MountedMapperWithoutPageComponentInfo("pg/${seg1}",
		// AbstractThemePage.class));
		mountPage("pg/${seg1}", AbstractThemePage.class);

		mountPage("admin/pages", PagesConfig.class);
		mountPage("admin/portlets", PortletsConfig.class);
		mountPage("admin/settings", SettingsPage.class);
		mountPage("admin/users", UsersPage.class);
		mountPage("admin/emails", EmailsPage.class);

		// TODO надо будет отключить при вводе в эксплуатацию, потому что это
		// нарушает систему безопасности. Либо добавить сервисам авторизацию
		mountResource("test", "services/test", new TestService());
		mountResource("upload", "services/upload", new FileUploadService());

		System.out.println("@@@  "
				+ getFrameworkSettings().getSerializer().getClass().getName());
		System.out.println("@@@  "
				+ getApplicationSettings().getClassResolver().getClass()
						.getName());

		getApplicationSettings().setClassResolver(new PortalClassResolver());

		

		// getExceptionSettings().setAjaxErrorHandlingStrategy(errorHandlingStrategyDuringAjaxRequests)

		// getFrameworkSettings().setSerializer(new
		// JavaSerializer(getApplicationKey()){
		//
		//
		// @Override
		// public byte[] serialize(Object object) {
		// if(object.getClass().getName().indexOf("XmlFile")!=-1){
		// System.out.println("------------");
		// System.out.println("SERIALIZE: "+object.getClass().getName());
		// System.out.println("    XmlFile: "+object);
		// }
		//
		// return super.serialize(object);
		// }
		//
		// @Override
		// public Object deserialize(byte[] data) {
		// System.out.println("TRY deserialize---------");
		// try{
		// return super.deserialize(data);
		// }catch(Exception ex){
		// ex.printStackTrace();
		// }
		// return null;
		// }
		//
		// });
		getFrameworkSettings().setSerializer(new MJS(getApplicationKey()));
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
				
				return loader;
			}
		};

		@Override
		public Class<?> resolveClass(String classname)
				throws ClassNotFoundException {
			
			
			try {
				return res.resolveClass(classname);
				
			} catch (ClassNotFoundException ex) {
				Class c = null;
				
				if (classname.startsWith("kz.tem.portal")) {
					System.out.println("!!!!! search " + classname);
					JarClassLoader jcl = ModuleEngine.getInstance()
							.getClassLoader(classname.split("\\.")[3]);
					if(jcl==null){
						throw new ClassNotFoundException("Not found JarClassLoader for module id:"+classname.split("\\.")[3]);
					}
						
					c=jcl.findClass(classname);
					System.out.println("!!! " + (c == null ? "NOT" : "")
							+ " found " + classname);
				}
				if (c == null) {
					System.out.println("$$$$: [" + classname + "]");
					throw new ClassNotFoundException(classname);
				}
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

	public void mountResource(String key, String path, final IResource resource) {
		ResourceReference ref = new ResourceReference(key) {
			private static final long serialVersionUID = 1L;

			@Override
			public IResource getResource() {
				return resource;
			}
		};
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

	public void mountSystemPage(String path, String title, Class pageClass){
		mountPage(path, pageClass);
		try{
			RegisterEngine.getInstance().getPageRegister().getPage(path);
		}catch(PortalException ex){
			if(ex.getKey()!=null && ex.getKey().equals(PortalException.NOT_FOUND)){
				kz.tem.portal.server.model.Page page = new kz.tem.portal.server.model.Page();
				page.setPageType(EnumPageType.SYSTEM);
				page.setPublicPage(true);
				page.setUrl(path);
				page.setTitle(title);
				page.setLayout("DefaultLayout.html");
				page.setTheme("DefaultTheme.html");
				try {
					RegisterEngine.getInstance().getPageRegister().addNewPage(page);
				} catch (PortalException e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}
			}else
				throw new RuntimeException(ex);
				
		}
		
	}
	
}

class MJS extends JavaSerializer{

	public MJS(String applicationKey) {
		super(applicationKey);
	}



	@Override
	public Object deserialize(byte[] data) {
		ThreadContext old = ThreadContext.get(false);
		final ByteArrayInputStream in = new ByteArrayInputStream(data);
		ObjectInputStream ois = null;
		try
		{
			Application oldApplication = ThreadContext.getApplication();
			try
			{
				System.out.println("\tTRY deserialize");
				ois = newObjectInputStream(in);
				
				
				String applicationName = (String)ois.readObject();
				if (applicationName != null)
				{
					Application app = Application.get(applicationName);
					if (app != null)
					{
						ThreadContext.setApplication(app);
					}
				}
				return ois.readObject();
			}
			finally
			{
				try
				{
					ThreadContext.setApplication(oldApplication);
					IOUtils.close(ois);
				}
				finally
				{
					in.close();
				}
			}
		}
		catch (ClassNotFoundException | IOException cnfx)
		{
			throw new RuntimeException("Could not deserialize object from byte[]", cnfx);
		}
		finally
		{
			ThreadContext.restore(old);
		}
	}

	
	
	
	
}
