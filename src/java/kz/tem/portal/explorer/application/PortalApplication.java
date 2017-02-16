package kz.tem.portal.explorer.application;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
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
import kz.tem.portal.explorer.page.errors.ErrorPage;
import kz.tem.portal.explorer.page.login.RegistrationPage;
import kz.tem.portal.explorer.panel.admin.settings.SettingsPanel;
import kz.tem.portal.explorer.panel.common.ftp.DocFileResource;
import kz.tem.portal.explorer.services.FileUploadService;
import kz.tem.portal.explorer.services.TestService;
import kz.tem.portal.server.model.Portlet;
import kz.tem.portal.server.model.enums.EnumPageType;
import kz.tem.portal.server.plugin.engine.JarClassLoader;
import kz.tem.portal.server.plugin.engine.ModuleEngine;
import kz.tem.portal.utils.ClassUtils;

import org.apache.wicket.Application;
import org.apache.wicket.DefaultPageManagerProvider;
import org.apache.wicket.Page;
import org.apache.wicket.ThreadContext;
import org.apache.wicket.application.AbstractClassResolver;
import org.apache.wicket.application.IClassResolver;
import org.apache.wicket.authroles.authentication.AbstractAuthenticatedWebSession;
import org.apache.wicket.authroles.authentication.AuthenticatedWebApplication;
import org.apache.wicket.core.request.handler.IPageRequestHandler;
import org.apache.wicket.core.request.handler.PageProvider;
import org.apache.wicket.core.request.handler.RenderPageRequestHandler;
import org.apache.wicket.core.util.lang.PropertyResolver;
import org.apache.wicket.core.util.lang.PropertyResolver.IGetAndSet;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.page.IManageablePage;
import org.apache.wicket.page.IPageManager;
import org.apache.wicket.page.IPageManagerContext;
import org.apache.wicket.page.PageStoreManager;
import org.apache.wicket.pageStore.DefaultPageStore;
import org.apache.wicket.pageStore.DiskDataStore;
import org.apache.wicket.pageStore.IDataStore;
import org.apache.wicket.pageStore.IPageStore;
import org.apache.wicket.pageStore.memory.HttpSessionDataStore;
import org.apache.wicket.pageStore.memory.PageNumberEvictionStrategy;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.cycle.AbstractRequestCycleListener;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.resource.IResource;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.serialize.ISerializer;
import org.apache.wicket.serialize.java.JavaSerializer;
import org.apache.wicket.settings.ExceptionSettings;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.apache.wicket.util.io.IOUtils;
import org.apache.wicket.util.lang.Bytes;
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
	
	public static DiskDataStore dataStore = null;
	public static byte[] lastpage = null;
	public static int lastPageId = -1;

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

		// mountPage("login", AuthenticatePage.class);
		
		mountSystemPage("main", "Главная",EnumPageType.ADDED, AuthenticatePage.class);
		
		mountSystemPage("login", "Аутентификация",EnumPageType.SYSTEM, AuthenticatePage.class);
		// mountPage("login", LoginPage.class);
		mountSystemPage("registration", "Регистрация", EnumPageType.SYSTEM,RegistrationPage.class);

		mountSystemPage(
				RegistrationConfirmationSuccessPage.PAGE_RESPONCE_SUCCESS_URL,
				"Успешное подтверждение регистрации",EnumPageType.SYSTEM,
				RegistrationConfirmationSuccessPage.class);
		mountSystemPage(
				RegistrationConfirmationErrorPage.PAGE_RESPONCE_ERROR_URL,
				"Ошибка подтверждения регистрации",EnumPageType.SYSTEM,
				RegistrationConfirmationErrorPage.class);
		mountSystemPage(RememberPasswordPage.REMEMBER_PASSWORD_PAGE_URL,
				"Восстановление пароля", EnumPageType.SYSTEM,RememberPasswordPage.class);
		mountSystemPage(NewPasswordSuccessPage.NEW_PASSWORD_SUCCESS_PAGE_URL,
				"Восстановление пароля", EnumPageType.SYSTEM,NewPasswordSuccessPage.class);
		mountSystemPage(NewPasswordErrorPage.NEW_PASSWORD_ERROR_PAGE_URL,
				"Ошибка при восстановлении пароля", EnumPageType.SYSTEM,NewPasswordErrorPage.class);
		mountSystemPage(ErrorPage.PAGE_ERROR_URL,"Системная ошибка",EnumPageType.SYSTEM,ErrorPage.class);
				
		

		mountPage(RegistrationConfirmationPage.PAGE_REQUEST_URL,
				RegistrationConfirmationPage.class);
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
		mountResource("download","download", new DocFileResource());
		

		System.out.println("@@@  "
				+ getFrameworkSettings().getSerializer().getClass().getName());
		System.out.println("@@@  "
				+ getApplicationSettings().getClassResolver().getClass()
						.getName());

		getApplicationSettings().setClassResolver(new PortalClassResolver());
		
		
		
		
		
		getExceptionSettings().setUnexpectedExceptionDisplay(ExceptionSettings.SHOW_NO_EXCEPTION_PAGE);
//		getApplicationSettings().setInternalErrorPage(ErrorPage.class);
		
		
		
		getRequestCycleListeners().add(new AbstractRequestCycleListener() {

			@Override
			public IRequestHandler onException(RequestCycle cycle, Exception ex) {
				System.out.println("!!!!!!!!!!!!! EXCEPTION ----------");
//				Exception myE = Exceptions.findCause(ex, Exception.class);
				

//				cycle.setResponsePage(PortalSession.get().lastPage);
//		        if (ex != null) {
//		            IPageRequestHandler handler = cycle.find(IPageRequestHandler.class);
//		            if (handler != null) {
//		                if (handler.isPageInstanceCreated()) {
//		                    WebPage page = (WebPage)(handler.getPage());
//
//		                    page.error("errrrorrrrr");
////		                    page.error(page.getString(myE.getCode()));
//
//		                    return new RenderPageRequestHandler(new PageProvider(page));
//		                }
//		        }
//		        }
		        return null;
			}
			
		});
		
//		getStoreSettings().setMaxSizePerSession(Bytes.kilobytes(1024));
//		setPageManagerProvider(new PageManagerProvider());
				
		// setPageManagerProvider(new DefaultPageManagerProvider(this){
		//
		// @Override
		// protected IDataStore newDataStore() {
		// return new HttpSessionDataStore(getPageManagerContext(), new
		// PageNumberEvictionStrategy(20));
		// }
		//
		// });
		// getStoreSettings().setMaxSizePerSession(Bytes.kilobytes(1024));
		// getStoreSettings().setInmemoryCacheSize(0);

		// *************************************
		/**
		 * 
		 * Этот кусок кода полностью отменяет версионность страниц.
		 * 
		 */
		// setPageManagerProvider(new VoidPageManagerProvider(this));
		// *************************************

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
		// getFrameworkSettings().setSerializer(new MJS(getApplicationKey()));
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

		
		SettingsPanel.setDefaultSettings(RegisterEngine.getInstance().getSettingsRegister());
	}

	@Override
	public Class<? extends Page> getHomePage() {
		return AbstractThemePage.class;
	}
	
	

	private class PortalClassResolver implements IClassResolver {

		private JarClassLoader jcl = null;

		private AbstractClassResolver res = new AbstractClassResolver() {

			@Override
			public ClassLoader getClassLoader() {
				ClassLoader loader = Thread.currentThread()
						.getContextClassLoader();

				return loader;
			}
		};

		/**
		 * Тут кроется такая засада. Каждый модуль тянет свой набор классов из
		 * своего JarClassLoader'а. Так же может случится ситуация, когда из
		 * двух разных модулей, примененных на одной общей странице,
		 * используется класс с одим названием, но разными версиями. Но так как
		 * механизм десериализации Java исключает повтовную загрузку уже
		 * загруженного класса, то будет загружен класс из первого модуля, в то
		 * время как второй получит неверную версию класса.
		 */
		@Override
		public Class<?> resolveClass(String classname)
				throws ClassNotFoundException {

			try {
				return res.resolveClass(classname);

			} catch (ClassNotFoundException ex) {
				Class c = null;
				System.out.println("!!!!! search " + classname + "  " + jcl);
				if (classname.startsWith("kz.tem.portal")) {

					jcl = ModuleEngine.getInstance().getClassLoader(
							classname.split("\\.")[3]);
					if (jcl == null) {
						throw new ClassNotFoundException(
								"Not found JarClassLoader for module id:"
										+ classname.split("\\.")[3]);
					}

					c = jcl.findClass(classname);
					System.out.println("!!! " + (c == null ? "NOT" : "")
							+ " found " + classname);
				} else if (jcl != null) {

					c = ClassUtils.resolveClassName(classname, jcl);

					// c=jcl.findClass(classname);
					System.out.println("finded C " + c);
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

	public void mountSystemPage(String path, String title, EnumPageType ept, Class pageClass) {
		mountPage(path, pageClass);
		try {
			RegisterEngine.getInstance().getPageRegister().getPage(path);
		} catch (PortalException ex) {
			if (ex.getKey() != null
					&& ex.getKey().equals(PortalException.NOT_FOUND)) {
				kz.tem.portal.server.model.Page page = new kz.tem.portal.server.model.Page();
				page.setPageType(ept);
				page.setPublicPage(true);
				page.setMenu(ept.equals(EnumPageType.ADDED));
				page.setUrl(path);
				page.setTitle(title);
				page.setLayout("DefaultLayout.html");
				page.setTheme("DefaultTheme.html");
				try {
					page = RegisterEngine.getInstance().getPageRegister()
							.addNewPage(page);
					
					if(path.equals("login")){
						Portlet p = new Portlet();
						p.setPosition("portlet1");
						p.setModuleName("user-login");
						p.setPage(page);
						page.getPortlets().add(p);
					}
					RegisterEngine.getInstance().getPageRegister().savePage(page);
					
					
				} catch (PortalException e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}
			} else
				throw new RuntimeException(ex);

		}

	}
	
	 

}

class MJS extends JavaSerializer {

	public MJS(String applicationKey) {
		super(applicationKey);

	}

	@Override
	public byte[] serialize(Object object) {
		return super.serialize(object);
	}

	@Override
	public Object deserialize(byte[] data) {
		ThreadContext old = ThreadContext.get(false);
		final ByteArrayInputStream in = new ByteArrayInputStream(data);
		ObjectInputStream ois = null;
		try {
			Application oldApplication = ThreadContext.getApplication();
			try {
				System.out.println("\tTRY deserialize");
				ois = newObjectInputStream(in);

				String applicationName = (String) ois.readObject();
				if (applicationName != null) {
					Application app = Application.get(applicationName);
					if (app != null) {
						ThreadContext.setApplication(app);
					}
				}
				return ois.readObject();
			} finally {
				try {
					ThreadContext.setApplication(oldApplication);
					IOUtils.close(ois);
				} finally {
					in.close();
				}
			}
		} catch (ClassNotFoundException | IOException cnfx) {
			throw new RuntimeException(
					"Could not deserialize object from byte[]", cnfx);
		} finally {
			ThreadContext.restore(old);
		}
	}

}
