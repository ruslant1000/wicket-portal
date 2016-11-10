package kz.tem.portal.explorer.panel.common.table;

import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

@SuppressWarnings("serial")
public class FirstColumnCheck<T> extends Panel{

	private CheckBox field;
	private T record;
	
	public FirstColumnCheck(String id,T record) {
		super(id);
		this.record=record;
		field = new CheckBox("field", new Model<Boolean>()){
			@Override
			protected boolean wantOnSelectionChangedNotifications() {
				return true;
			}
			@Override
			public void onSelectionChanged() {
				super.onSelectionChanged();
			}
			
			
		};
		add(field);
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
