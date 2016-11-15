package kz.tem.portal.explorer.panel.common.form.field;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.model.IModel;
/**
 * 
 * @author Ruslan Temirbulatov
 *
 */
@SuppressWarnings("serial")
public class FAjaxCheckboxField extends FCheckboxField{

	public FAjaxCheckboxField(String id,IModel<Boolean> model) {
		super(id,model);
		
	}
	
	public void onCheckBox(CheckBox field){
		field.add(new OnChangeAjaxBehavior() {
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				try {
					FAjaxCheckboxField.this.onChangeValue(target);
				} catch (Exception e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}
			}
		});
	}
	
	public void onChangeValue(AjaxRequestTarget target) throws Exception{
		
	}

}
