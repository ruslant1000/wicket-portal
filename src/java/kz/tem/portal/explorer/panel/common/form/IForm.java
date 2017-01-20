package kz.tem.portal.explorer.panel.common.form;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import kz.tem.portal.explorer.panel.common.table.AColumn;

import org.apache.wicket.model.IModel;
/**
 * 
 * @author Ruslan Temirbulatov
 *
 */
public interface IForm extends Serializable{

	public void addFieldString(String title, IModel<String> model, boolean required);
	
	public void addFieldArea(String title, IModel<String> model, boolean required);
	
	public void addFieldPassword(String title, IModel<String> model, boolean required);
	
	public void addCombobox(String title, IModel<?> model,List<?> choices, boolean required);
	
	public void addFieldDate(String title, IModel<Date> model,String pattern, boolean required);
	
	public void addLabel(String title, String text);
	
	public void addReadOnlyComponent(AColumn column, Object record)throws Exception;
	
}
