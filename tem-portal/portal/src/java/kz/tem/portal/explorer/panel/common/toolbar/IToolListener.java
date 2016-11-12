package kz.tem.portal.explorer.panel.common.toolbar;

import java.io.Serializable;

import org.apache.wicket.ajax.AjaxRequestTarget;
/**
 * 
 * @author Ruslan Temirbulatov
 *
 */
public interface IToolListener extends Serializable{
	
	public void onAction(AjaxRequestTarget target)throws Exception;

}
