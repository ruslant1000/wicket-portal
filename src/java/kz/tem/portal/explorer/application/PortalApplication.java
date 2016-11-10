package kz.tem.portal.explorer.application;

import java.net.URL;
import java.util.Iterator;

import kz.tem.portal.api.PortalEngine;
import kz.tem.portal.explorer.page.AbstractThemePage;
import kz.tem.portal.explorer.page.SignInPage;
import kz.tem.portal.explorer.page.admin.pages.PagesConfig;
import kz.tem.portal.explorer.page.admin.portlets.PortletsConfig;
import kz.tem.portal.server.plugin.engine.ModuleEngine;
import kz.tem.portal.server.register.IUserRegister;
import kz.tem.portal.server.register.impl.UserRegisterImpl;

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.Page;
import org.apache.wicket.application.AbstractClassResolver;
import org.apache.wicket.application.DefaultClassResolver;
import org.apache.wicket.application.IClassResolver;
import org.apache.wicket.authroles.authentication.AbstractAuthenticatedWebSession;
import org.apache.wicket.authroles.authentication.AuthenticatedWebApplication;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.resolver.IComponentResolver;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.serialize.ISerializer;
import org.apache.wicket.serialize.java.JavaSerializer;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.apache.wicket.util.file.IResourceFinder;
import org.apache.wicket.util.resource.IResourceStream;

public class PortalApplication extends AuthenticatedWebApplication {

	
	
	@Override
	protected void init() {
		super.init();
		getComponentInstantiationListeners().add(
				new SpringComponentInjector(this));
		getResourceSettings().setResourceStreamLocator(
				new PortalStreamLocator());

		// WebApplication.get().getMarkupSettings().getMarkupFactory().getMarkupCache().shutdown();
		// getDebugSettings().setComponentUseCheck(false);

		PortalEngine.getInstance().getExplorerEngine().initLayouts(this);
		PortalEngine.getInstance().getExplorerEngine().initThemes(this);

		// getPageSettings().setRecreateBookmarkablePagesAfterExpiry(true);

		mountPage("login", SignInPage.class);
		
		mountPage("pg/${seg1}", AbstractThemePage.class);

		mountPage("admin/pages", PagesConfig.class);
		mountPage("admin/portlets", PortletsConfig.class);

		System.out.println("@@@  "
				+ getFrameworkSettings().getSerializer().getClass().getName());
		System.out.println("@@@  "
				+ getApplicationSettings().getClassResolver().getClass()
						.getName());

		getApplicationSettings().setClassResolver(new PortalClassResolver());
		
		
		

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

	public static PortalApplication get() {
		return (PortalApplication) WebApplication.get();
	}

	/**
	 * В объкте Page хранится только относительный URL. Например 'suburl1'. И
	 * для того чтобы получить полный валидный URL нужно привести его к виду
	 * '/<portal-context-path>/pg/suburl1'. Например '/portal/pg/suburl1'
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
		return SignInPage.class;
	}

	@Override
	protected Class<? extends AbstractAuthenticatedWebSession> getWebSessionClass() {
		return PortalSession.class;
	}
}
