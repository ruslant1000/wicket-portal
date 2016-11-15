package kz.tem.portal.explorer.panel.common.table;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
/**
 * 
 * @author Ruslan Temirbulatov
 *
 * @param <T>
 */
@SuppressWarnings("serial")
public class FirstColumnCheck<T> extends Panel{

	private CheckBox field;
	private T record;
	
	public FirstColumnCheck(String id,T record) {
		super(id);
		setOutputMarkupId(true);
		this.record=record;
		field = new CheckBox("field", new Model<Boolean>());
		add(field);
		field.add(new OnChangeAjaxBehavior() {
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				System.out.println(field.getModelObject());
				FirstColumnCheck.this.onChange(target);
			}
		});
	}
	
	public void onChange(AjaxRequestTarget target){
		
	}
	public void setSelected(Boolean value){
		field.setModelObject(value);
	}
	public boolean isSelected(){
		if(field.getModelObject()==null)
			return false;
		return field.getModelObject().booleanValue();
	} 
	public T getRecord(){
		return record;
	}

}
