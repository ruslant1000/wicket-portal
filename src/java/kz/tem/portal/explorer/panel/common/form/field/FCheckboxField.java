package kz.tem.portal.explorer.panel.common.form.field;

import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
/**
 * 
 * @author Ruslan Temirbulatov
 *
 */
@SuppressWarnings("serial")
public class FCheckboxField extends Panel{

	private CheckBox field=null;
	public FCheckboxField(String id,IModel<Boolean> model) {
		super(id);
		field = new CheckBox("field",model);
		add(field);
		onCheckBox(field);
	}
	
	public Boolean getValue(){
		return field.getModelObject();
	}
	
	public void onCheckBox(CheckBox field){
		
	}

}
