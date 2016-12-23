package kz.tem.portal.explorer.panel.common.form;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.BiConsumer;

import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;

import kz.tem.portal.explorer.panel.common.form.field.FComboboxField;
import kz.tem.portal.explorer.panel.common.form.field.FPasswordField;
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
	
	private List<String> requiredFieldNames = new LinkedList<String>();
	private Map<String, IModel> requireds = new TreeMap<String, IModel>();
	/**
	 * Поле для ввода строк.
	 * @param id
	 * @param model
	 * @return
	 */
	public FTextStringField string(String id, String title, IModel<String> model, boolean required){
		
		FTextStringField field = new FTextStringField(id, model);
		if(required){
			requireds.put(title, model);
			requiredFieldNames.add(title);
		}
		return field;
	} 
	/**
	 * Поле для ввода пароля
	 * @param id
	 * @param title
	 * @param model
	 * @param required
	 * @return
	 */
	public FPasswordField password(String id, String title, IModel<String> model, boolean required){
		
		FPasswordField field = new FPasswordField(id, model);
		if(required){
			requireds.put(title, model);
			requiredFieldNames.add(title);
		}
		return field;
	} 
	
	public FComboboxField combobox(String id, String title, IModel model,List choices, boolean required){
		FComboboxField field = new FComboboxField(id, model, choices);
		if(required){
			requireds.put(title, model);
			requiredFieldNames.add(title);
		}
		return field;
	}
	
	public boolean checkFields(final Component parent){
		
	
		
		boolean valid = true;
		for(String f:requiredFieldNames){
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
	
	public static void main(String[] args) {
		Map<String, String> map = new TreeMap<String, String>();
		map.put("login", "a");
		map.put("e-mail", "a");
		map.put("Пароль", "a");
		map.put("Еще раз пароль", "a");
		
		
		for(String s:map.keySet()){
			System.out.println(s);
		}
	}
}
