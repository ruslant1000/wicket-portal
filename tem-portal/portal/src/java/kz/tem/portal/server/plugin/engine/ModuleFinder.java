package kz.tem.portal.server.plugin.engine;

import java.io.File;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import kz.tem.portal.utils.UnZipUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ModuleFinder {
 
	private static Logger log = LoggerFactory.getLogger(ModuleFinder.class);
	
	public boolean findNewModules(String path){
		log.info("ѕоиск новых пакетов модулей...");
		Set<String> set = new HashSet<String>();
		File dir = new File(path);
		File[] list = dir.listFiles();
		if(list!=null && list.length>0){
			for(File f:list){
				if(f.isDirectory()){
					log.info("\t...найден существующий пакет "+f.getName());
					set.add(f.getName());
				}else if(f.isFile() && f.getName().toLowerCase().endsWith(".zip")){
					String foldName = f.getName().substring(0,f.getName().length()-4);
					if(!set.contains(foldName)){
						log.info("\t...найден новый пакет "+foldName);
						unpack(f.getAbsolutePath(), f.getAbsolutePath().substring(0,f.getAbsolutePath().length()-4));
					}
				}
			}
		}
		log.info("ѕоиск новых пакетов модулей завершен");
		return false;
	}
	public void unpack(String zipFileName, String outputFolder){
		try {
			UnZipUtils.unzip(zipFileName, outputFolder);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Ќе удалось распаковать пакет модулей "+zipFileName,e);
		}
	}
	
	public static void main(String[] args) {
		new ModuleFinder().findNewModules("G:\\projects\\tem-portal\\apache-tomcat-7.0.37\\webapps\\portal\\modules");
	}
}
