package kz.tem.portal.server.plugin.engine;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kz.tem.portal.utils.UnZipUtils;
/**
 * 
 * @author Ruslan Temirbulatov
 *
 */
public class ModuleFinder {
 
	private static Logger log = LoggerFactory.getLogger(ModuleFinder.class);
	
	public boolean findNewModules(String path){
		Set<String> set = new HashSet<String>();
		File dir = new File(path);
		File[] list = dir.listFiles();
		if(list!=null && list.length>0){
			for(File f:list){
				if(f.isDirectory()){
					set.add(f.getName());
				}else if(f.isFile() && f.getName().toLowerCase().endsWith(".zip")){
					String foldName = f.getName().substring(0,f.getName().length()-4);
					if(!set.contains(foldName)){
						unpack(f.getAbsolutePath(), f.getAbsolutePath().substring(0,f.getAbsolutePath().length()-4));
					}
				}
			}
		}
		return false;
	}
	public void unpack(String zipFileName, String outputFolder){
		try {
			UnZipUtils.unzip(zipFileName, outputFolder);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		new ModuleFinder().findNewModules("G:\\projects\\tem-portal\\apache-tomcat-7.0.37\\webapps\\portal\\modules");
	}
}
