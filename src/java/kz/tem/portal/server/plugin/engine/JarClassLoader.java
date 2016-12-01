package kz.tem.portal.server.plugin.engine;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Hashtable;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.wicket.util.resource.IResourceStream;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.InputStreamResource;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.util.ClassUtils;
import org.xml.sax.InputSource;

import sun.misc.Resource;
import kz.tem.portal.context.listener.ModuleContextListener;
import kz.tem.portal.utils.FileUtils;
/**
 * 
 * @author Ruslan Temirbulatov
 *
 */ 
public class JarClassLoader extends ClassLoader {

	/**
	 * Тут указывается ClassLoader для поиска классов бинов, загруженных через Spring модуля
	 * XmlBeanDefinitionReader.getBeanClassLoader()
	 */
	private ClassLoader springClassLoader = null;
	
	public ClassLoader getSpringClassLoader() {
		return springClassLoader;
	}

	public void setSpringClassLoader(ClassLoader springClassLoader) {
		this.springClassLoader = springClassLoader;
	}

	private String path;
	// private String[] jarFiles = new
	// String[]{"E:/projects/xxx/xxx.jar","E:/projects/xxx/zzz.jar"}; //Path to
	// the jar file
	private Hashtable classes = new Hashtable(); // used to cache already
													// defined classes

	public JarClassLoader(String path) {
		super(JarClassLoader.class.getClassLoader()); // calls the parent class
														// loader's constructor
		this.path = path;
	}

	public Class loadClass(String className) throws ClassNotFoundException {
//		System.out.println("load " + className);
		return findClass(className);
	}

	public Class findClass(String className) {
		
		
		if(className.indexOf("CategoryRegisterImpl")!=-1)
			System.out.println("\t jar: "+className);
		
		
//		System.out.println("find " + className);
		byte classByte[];
		Class result = null;

		result = (Class) classes.get(className); // checks in cached classes
		if (result != null) {
			return result;
		}

		try {
			return JarClassLoader.class.getClassLoader().loadClass(className);
			// return super.findClass(className);
		} catch (Exception e) {
		}
		try {
			return findSystemClass(className);
		} catch (Exception e) {
		}
		
		
		InputStream is = null;
		JarFile jar = null;
		ByteArrayOutputStream byteStream = null;
		
		
//		if(className.contains("$$EnhancerBySpringCGLIB$$"))
//			className = className.split("\\$\\$EnhancerBySpringCGLIB\\$\\$")[0];
//		
//		result = (Class) classes.get(className); // checks in cached classes
//		if (result != null) {
//			return result;
//		}
		
		try {
//			System.out.println("find in jar...");
			
			File pathDir = new File(path);

			for (String jarFile : pathDir.list()) {
//				System.out.println(jarFile);
				
					jar = new JarFile(new File(path, jarFile));
//					System.out.println(className.replaceAll("\\.", "/"));
					JarEntry entry = jar.getJarEntry(className.replaceAll("\\.",
							"/") + ".class");
					if (entry == null){
						try {jar.close();} catch (IOException e) {}
						continue;
					}
					is = jar.getInputStream(entry);
					
			}

			byteStream = new ByteArrayOutputStream();
			int nextValue = is.read();
			while (-1 != nextValue) {
				byteStream.write(nextValue);
				nextValue = is.read();
			}

			classByte = byteStream.toByteArray();
			result = defineClass(className, classByte, 0, classByte.length,
					null);
			classes.put(className, result);
			return result;
		} catch (Exception e) {
			
		}finally{
			
			try{byteStream.close();}catch(Exception ex){}
			try{is.close();}catch(Exception ex){}
			try {jar.close();} catch (IOException e) {}
		}
		if(springClassLoader!=null){
			try {
				result = springClassLoader.loadClass(className);
				return result;
			} catch (ClassNotFoundException e) {
				
			}
		}
		
		return null;
	}


	public void clear() {
		classes.clear();
	}
	
	public void destroy(){
		clear();
	}

	@Override
	protected URL findResource(String name) {
		try {
			File pathDir = new File(path);
			for (File jarFile : pathDir.listFiles()) {
				JarFile jar = null;
				try{
					jar = new JarFile(jarFile);
					JarEntry entry = jar.getJarEntry(name);
					if(entry==null)
						continue;
					String url = jarFile.toURL().toString();
					
					return new URL("jar:" + url + "!/" + name);
				}finally{
					jar.close();
				}
			}
		} catch (Exception e) {

		}
		return null;
	}

	public static void main(String[] args) throws Exception {
		JarClassLoader j = new JarClassLoader(
				"G:\\projects\\tem-portal\\apache-tomcat-7.0.37\\webapps\\portal\\modules\\msystext-0.0.1-bundle\\lib");
		
		
		
//		InputStream ins = j.getResourceAsStream("kz/tem/portal/msystext/server/context/main.xml");
//		System.out.println(ins.available());
////		
////		
//		
//		GenericApplicationContext context = new GenericApplicationContext(); 
////
//		  XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(context);
//		  reader.setValidationMode(XmlBeanDefinitionReader.VALIDATION_XSD);
//		  
////		  //get the classloader
////		  
////		  
////		  ClassLoader loader = Thread.currentThread().getContextClassLoader();
////		  //set the loader
//		  reader.setBeanClassLoader(j);
//		  reader.setResourceLoader(new DefaultResourceLoader(j));
////		  //get the bean definitions
////		  j.getResource("kz/tem/portal/msystext/server/context/main.xml")
//		  reader.loadBeanDefinitions(new InputStreamResource(ins));
////		  //init
//		  context.refresh();
//		
//		  
//		  
//		  
//		  context.start();
//		  
//		  
//		  SessionFactory sess = (SessionFactory)context.getBean("mySessionFactory");
		
//		ApplicationContext context = 
//	             new ClassPathXmlApplicationContext("kz/tem/portal/msystext/server/context/main.xml");
		
		
//		ModuleEngine.initializeSpringContext(j, "kz/tem/portal/msystext/server/context/main.xml");
//		Class s = j.loadClass("kz.tem.portal.msystext.server.context.ContextListener");
//		s.getConstructor(new Class[]{String.class}).newInstance("classpath:main.xml");
//		ModuleContextListener mcl = (ModuleContextListener)s.newInstance();
//		mcl.initialize();
//		URL url = j
//				.getResource("kz/tem/portal/module/ftpclient/FtpClientModule.html");
//		System.out.println(url);
//		Class s = j.loadClass("kz.tem.portal.module.ftpclient.BeanX");
//		Object o = s.getConstructor(new Class[]{String.class}).newInstance(new Object[]{"s"});
		
//		j.destroy();
//		j=null;
		
//		File pathDir = new File("G:\\projects\\tem-portal\\apache-tomcat-7.0.37\\webapps\\portal\\modules\\ftp-client-0.0.1-bundle\\lib\\ftp-client-0.0.1.jar");
//		JarFile jar = new JarFile(pathDir);
//		JarEntry entry = jar.getJarEntry("kz/tem/portal/module/ftpclient/BeanX.class");
//		
//		InputStream is = jar.getInputStream(entry);
//		is.close();
//		jar.close();
		
//		FileUtils.deleteDirectory("G:\\projects\\tem-portal\\apache-tomcat-7.0.37\\webapps\\portal\\modules\\ftp-client-0.0.1-bundle");
//		File f = new File("G:\\projects\\tem-portal\\apache-tomcat-7.0.37\\webapps\\portal\\modules\\ftp-client-0.0.1-bundle");
//		boolean b = f.delete();
//		System.out.println(b);
	}

}