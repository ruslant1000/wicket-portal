package kz.tem.portal.server.plugin;

import org.apache.wicket.markup.html.panel.Panel;
import org.hibernate.cfg.NotYetImplementedException;
/**
 * 
 * @author Ruslan Temirbulatov
 *
 */
@SuppressWarnings("serial")
public class Module extends Panel {

	private ModuleConfig config = new ModuleConfig();
	
	public Module(String id, ModuleConfig config) {
		super(id);
		try {
			initDefaultConfigs(Module.this.config);
			if(config!=null){
				for(String key:config.getValues().keySet()){
					Module.this.config.getValues().put(key, config.getValues().get(key));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Инициализация модуля не удалась",e);
		}
	}
	
	public void create()throws Exception{
		throw new NotYetImplementedException("Не реализован обязательный метод create");
	}
	
	public ModuleConfig getModuleConfig(){
		return config;
	}
	
	public void initDefaultConfigs(ModuleConfig config)throws Exception{
		
	}
	
	

	
	
	

}
 