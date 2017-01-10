package kz.tem.portal.utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
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
	
	public static String getTempDir()throws Exception{
		File f = new File(System.getProperty("java.io.tmpdir"),new SimpleDateFormat("yyyy-MM/dd/HH/mm").format(new Date()));
		if(!f.exists()){
			if(!f.mkdirs()){
				throw new Exception("Cannot create temp path: "+f.getAbsolutePath());
			}
		}
		return f.getAbsolutePath();
	}
	
	public static String getTempFileFullName(String name)throws Exception{
		File f = new File(getTempDir(),name);
		return f.getAbsolutePath();
	}
}
