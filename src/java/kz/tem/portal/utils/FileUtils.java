package kz.tem.portal.utils;

import java.io.File;
/**
 * 
 * @author Ruslan Temirbulatov
 *
 */
public class FileUtils {

	public static void deleteFile(String name)throws Exception{
		File f = new File(name);
		if(!f.delete()){
			throw new Exception("Ошибка удаления "+name);
		}
	}
	public static void deleteDirectory(String directory)throws Exception{
		File dir = new File(directory);
		if(dir.exists() && dir.isFile()){
			deleteFile(directory);
			return;
		}
		File[] ff = dir.listFiles();
		if(ff==null || ff.length==0){
			if(!dir.delete())
				throw new Exception("Ошибка удаления "+directory);
			return;
		}
		for(File f:ff){
			deleteDirectory(f.getAbsolutePath());
		}
		if(!dir.delete())
			throw new Exception("Ошибка удаления "+directory);
	}
}
