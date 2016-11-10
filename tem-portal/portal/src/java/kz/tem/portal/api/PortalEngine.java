package kz.tem.portal.api;

import kz.tem.portal.server.plugin.engine.ModuleEngine;

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
	
	public ModuleEngine getModuleEngine(){
		return ModuleEngine.getInstance();
	} 
	public RegisterEngine getRegisterEngine(){
		return RegisterEngine.getInstance();
	}
}
