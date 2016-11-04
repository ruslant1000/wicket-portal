package kz.tem.portal.api;

public class ServerEngine {
	
	private static ServerEngine instance=null;
	
	public static ServerEngine getInstance(){
		if(instance==null)
			instance = new ServerEngine();
		return instance;
	}
	
	private ServerEngine(){}
	
	

}
