package kz.tem.portal.explorer.panel.common.form;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;

import kz.tem.portal.explorer.panel.common.form.field.FComboboxField;
import kz.tem.portal.explorer.panel.common.form.field.FTextStringField;
/**
 * 
 * @author Ruslan Temirbulatov
 * 
 * Конструктор полей ввода для форм ввода.
 *
 */
@SuppressWarnings("serial")
public class FormFieldsBuilder implements Serializable{
	

	private Map<String, IModel> requireds = new HashMap<String, IModel>();
	/**
	 * Поле для ввода строк.
	 * @param id
	 * @param model
	 * @return
	 */
	public FTextStringField string(String id, String title, IModel<String> model, boolean required){
		
		FTextStringField field = new FTextStringField(id, model);
		if(required)
			requireds.put(title, model);
		return field;
	} 
	public FComboboxField combobox(String id, String title, IModel model,List choices, boolean required){
		FComboboxField field = new FComboboxField(id, model, choices);
		if(required)
			requireds.put(title, model);
		return field;
	}
	
	public boolean checkFields(Component parent){
		boolean valid = true;
		for(String f:requireds.keySet()){
			Object value = requireds.get(f).getObject();
			if(value==null){
				parent.error("Не заполнено обязательное поле '"+f+"'");
				valid = false;
			}else{ 
				if(value instanceof String && value.toString().trim().length()==0){
					parent.error("Не заполнено обязательное поле '"+f+"'");
					valid = false;
				}
			}
		}
		return valid;
	}
}
