package kz.tem.portal.server.plugin;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.hibernate.cfg.NotYetImplementedException;
import org.springframework.context.support.GenericApplicationContext;
/**
 * 
 * @author Ruslan Temirbulatov
 *
 */
@SuppressWarnings("serial")
public class Module extends Panel {
	
	private static GenericApplicationContext springContext;

	private ModuleConfig config = new ModuleConfig();
	
	public Module(String id, ModuleConfig config) {
		super(id);
		try {
			initDefaultConfigs(Module.this.config);
			if(config!=null){
				Module.this.config.copyFrom(config);
				
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

	public GenericApplicationContext getSpringContext() {
		return springContext;
	}

	public void setSpringContext(GenericApplicationContext springContext) {
		this.springContext = springContext;
	}
	
	

	
	/**
	 * To get Module object of the given child component
	 * @param component
	 * @return
	 */
	public static Module get(MarkupContainer component){
		if(component==null)
			return null;
		if(component instanceof Module)
			return (Module)component;
		return get(component.getParent());
	}
	

}
 