package kz.tem.portal.utils;

import java.util.LinkedList;
import java.util.List;
/**
 * 
 * @author Ruslan Temirbulatov
 *
 */
public class ExceptionUtils {

	public static List<String> errors(Throwable cause){
		List<String> list = new LinkedList<String>();
		if(cause!=null){
			System.out.println("---> "+cause.getMessage());
			list.add(cause.getMessage());
			list.addAll(errors(cause.getCause()));
		}
		return list;
	}
	
	public static String fullError(Throwable cause){
		String txt = null;
		List<String> list = errors(cause);
		for(String s:list)
			txt=(txt==null?"":txt+". ")+s;
		return txt;
	}
	
}
