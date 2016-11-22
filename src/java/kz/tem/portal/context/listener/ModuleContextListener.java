package kz.tem.portal.context.listener;

import java.io.Serializable;
/**
 * 
 * @author Ruslan Temirbulatov
 * Интерфейс для реализации в модулях.
 */
public interface ModuleContextListener extends Serializable{

	public void initialize()throws Exception;
	public void destroy()throws Exception;
}
