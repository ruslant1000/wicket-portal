package kz.tem.portal.api;

public class PortalEngine {

	
	public static PortalEngine instance = null;
	
	public static PortalEngine getInstance(){
		if(instance==null)
			instance = new PortalEngine();
		return instance;
	}
	
	private PortalEngine(){}
	
	
	public ServerEngine getServerEngine(){
		return ServerEngine.getInstance();
	} 
	
	public ExplorerEngine getExplorerEngine(){
		return ExplorerEngine.getInstance();
	}
}
