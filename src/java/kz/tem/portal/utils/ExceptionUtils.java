package kz.tem.portal.utils;

import java.util.LinkedList;
import java.util.List;

import org.hibernate.StaleObjectStateException;
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
			if(cause instanceof StaleObjectStateException)
				list.add("Объект только что был изменен другим пользователем.");
			else
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
