package kz.tem.portal.api;
/**
 * 
 * @author Ruslan Temirbulatov
 *
 */
public class ServerEngine {
	
	private static ServerEngine instance=null;
	
	public static ServerEngine getInstance(){
		if(instance==null)
			instance = new ServerEngine();
		return instance;
	}
	
	private ServerEngine(){}
	
	

}
