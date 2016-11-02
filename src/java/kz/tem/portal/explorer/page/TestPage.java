package kz.tem.portal.explorer.page;

import java.net.URL;

import kz.tem.portal.server.plugin.Module;
import kz.tem.portal.server.plugin.ModuleMeta;
import kz.tem.portal.server.plugin.engine.ModuleEngine;

import org.apache.wicket.core.util.resource.UrlResourceStream;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;

@SuppressWarnings("serial")
public class TestPage extends WebPage {

	public void test(String url) {
		try {
			new UrlResourceStream(new URL(url));
			System.out.println("SUCCESS "+url);
		} catch (Exception ex) {
			System.out.println("ERROR "+url);
		}
	}

	public TestPage() {
		ModuleMeta meta = ModuleEngine.getInstance().getModuleMap().values()
				.iterator().next();

		
//			test("jar:file:/G:/projects/tem-portal/apache-tomcat-7.0.37/webapps/portal/modules/ftp-client-0.0.1-bundle/ftp-client/lib/ftp-client-0.0.1.jar!/kz/tem/portal/module/ftpclient/FtpClientModule.html");
		try {
			

//			 Module module = ModuleEngine.getInstance().create("xx",meta);
//			 add(module);
			add(new TestPanel("xx"));
//			add(new Label("xx", "ddd"));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

	}
}
