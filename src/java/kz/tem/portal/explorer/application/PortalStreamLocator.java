package kz.tem.portal.explorer.application;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

import kz.tem.portal.server.plugin.engine.ModuleEngine;

import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.core.util.resource.UrlResourceStream;
import org.apache.wicket.core.util.resource.locator.ResourceStreamLocator;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.string.Strings;

public class PortalStreamLocator extends ResourceStreamLocator{

	@Override
	public IResourceStream locate(Class<?> arg0, String arg1, String arg2,
			String arg3, Locale arg4, String arg5, boolean arg6) {
		System.out.println("PATH: "+arg1);
		System.out.println("CLASS: "+arg0);
		System.out.println("BOOLEAN: "+arg6);
		return super.locate(arg0, arg1, arg2, arg3, arg4, arg5, arg6);
	}
	
	
	private void showResources(){
		
	}
private IResourceStream loca3(Class<?> clazz, String path){
		
		
		
		if(path.indexOf("FtpClientModule")!=-1){
			try {
				URL u = new URL("jar:file:/G:/projects/tem-portal/apache-tomcat-7.0.37/webapps/portal/modules/ftp-client-0.0.1-bundle/ftp-client/lib/ftp-client-0.0.1.jar!/kz/tem/portal/module/ftpclient/FtpClientModule.html");
				return new UrlResourceStream(u);
			} catch (MalformedURLException e) {
				System.out.println("CANNOT");
			}
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
			}
			
		}
		return null;
}
	
	private IResourceStream loca2(Class<?> clazz, String path){
		
		
		
		
		
		if(path.indexOf("FtpClientModule.html")!=-1){
			try {
				URL u = new URL("jar:file:/G:/projects/tem-portal/apache-tomcat-7.0.37/webapps/portal/modules/ftp-client-0.0.1-bundle/ftp-client/lib/ftp-client-0.0.1.jar!/kz/tem/portal/module/ftpclient/FtpClientModule.html");
				return new UrlResourceStream(u);
			} catch (MalformedURLException e) {
				System.out.println("CANNOT");
			}
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
			}
		}
		
		
		
		String location;

		String extension = path.substring(path.lastIndexOf('.') + 1);
		String simpleFileName = Strings.lastPathComponent(clazz.getName(), '.');
		location = "/pages/" + simpleFileName + "." + extension;

		URL url;
		try
		{
			
			// try to load the resource from the web context
			url = new URL("jar:file:/G:/projects/tem-portal/apache-tomcat-7.0.37/webapps/portal/modules/ftp-client-0.0.1-bundle/ftp-client/lib/ftp-client-0.0.1.jar!/kz/tem/portal/module/ftpclient/FtpClientModule.html");
//			url = WebApplication.get().getServletContext().getResource("jar:file:/G:/projects/tem-portal/apache-tomcat-7.0.37/webapps/portal/modules/ftp-client-0.0.1-bundle/ftp-client/lib/ftp-client-0.0.1.jar!/kz/tem/portal/module/ftpclient/FtpClientModule.html");

			if (url != null)
			{
				return new UrlResourceStream(url);
			}
		}
		catch (MalformedURLException e)
		{
			throw new WicketRuntimeException(e);
		}

		// resource not found; fall back on class loading
		return super.locate(clazz, path);
	}
	
	private IResourceStream moduleLocate(Class<?> arg0, String arg1){
		String moduleName = arg1.substring("kz/tem/portal/module/".length()).split("/")[0];
		System.out.println("moduleName: "+moduleName);
		IResourceStream stream =null;
		URL url = ModuleEngine.getInstance().getClassLoader(moduleName).getResource(arg1);
		
		if(url!=null){
			System.out.println("URL: "+url.toString());
			stream = new UrlResourceStream(url);
		}
		if(stream==null)
			System.out.println("   NOT FOUND");
		
		return stream;
	}

	@Override
	public IResourceStream locate(Class<?> arg0, String arg1) {
		System.out.println("PATH: "+arg1);
		System.out.println("CLASS: "+arg0);
//		try {
//			Thread.sleep(5000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
		IResourceStream stream =null;//super.locate(arg0, arg1);
		if(arg1.startsWith("kz/tem/portal/module/")){
//			stream = loca2(arg0, arg1);
			stream = moduleLocate(arg0, arg1);
		}
		if(stream!=null){
			System.out.println("stream.length(): "+stream.length());
//			try {
//				Thread.sleep(10000);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			
		}
		if(stream==null)
			stream = super.locate(arg0, arg1);
		
		
		return stream;
	}

	public static void main(String[] args) {
		
		
	}
	
}
