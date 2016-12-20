package kz.tem.portal.explorer.panel.common.form;

import java.io.Serializable;
import java.util.List;

import org.apache.wicket.model.IModel;
/**
 * 
 * @author Ruslan Temirbulatov
 *
 */
public interface IForm extends Serializable{

	public void addFieldString(String title, IModel<String> model, boolean required);
	
	public void addFieldPassword(String title, IModel<String> model, boolean required);
	
	public void addCombobox(String title, IModel<?> model,List<?> choices, boolean required);
	
}
