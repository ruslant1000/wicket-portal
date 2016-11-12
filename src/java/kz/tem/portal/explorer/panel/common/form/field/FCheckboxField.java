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

	public FCheckboxField(String id,IModel<Boolean> model) {
		super(id);
		CheckBox field = new CheckBox("field",model);
		add(field);
	}
	
	public void onCheckBox(CheckBox field){
		
	}

}
