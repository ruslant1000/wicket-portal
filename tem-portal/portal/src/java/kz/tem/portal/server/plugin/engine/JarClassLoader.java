package kz.tem.portal.server.plugin.engine;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class JarClassLoader extends ClassLoader {
	private String path;
//    private String[] jarFiles = new String[]{"E:/projects/xxx/xxx.jar","E:/projects/xxx/zzz.jar"}; //Path to the jar file
    private Hashtable classes = new Hashtable(); //used to cache already defined classes

    public JarClassLoader(String path) {
        super(JarClassLoader.class.getClassLoader()); //calls the parent class loader's constructor
        this.path=path;
    }

    public Class loadClass(String className) throws ClassNotFoundException {
    	System.out.println("load "+className);
        return findClass(className);
    }

    public Class findClass(String className) {
    	System.out.println("find "+className);
        byte classByte[];
        Class result = null;

        result = (Class) classes.get(className); //checks in cached classes
        if (result != null) {
            return result;
        }

        try {
            return findSystemClass(className);
        } catch (Exception e) {
        }

        try {	
        	System.out.println("find in jar...");
        	InputStream is = null;
        	File pathDir = new File(path);
        	
        	for(String jarFile:pathDir.list()){
        		System.out.println(jarFile);
        		JarFile jar = new JarFile(new File(path,jarFile));
        		System.out.println(className.replaceAll("\\.", "/"));
        		JarEntry entry = jar.getJarEntry(className.replaceAll("\\.", "/") + ".class");
        		if(entry==null)continue;
                is = jar.getInputStream(entry);
        	}
        	
            
           
            
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            int nextValue = is.read();
            while (-1 != nextValue) {
                byteStream.write(nextValue);
                nextValue = is.read();
            }

            classByte = byteStream.toByteArray();
            result = defineClass(className, classByte, 0, classByte.length, null);
            classes.put(className, result);
            return result;
        } catch (Exception e) {
            return null;
        }
    }
    
    public void clear(){
    	classes.clear();
    }

}