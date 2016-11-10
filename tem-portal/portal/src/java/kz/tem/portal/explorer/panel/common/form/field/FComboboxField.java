package kz.tem.portal.explorer.panel.common.form.field;

import java.util.List;

import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

@SuppressWarnings("serial")
public class FComboboxField extends Panel{

	public FComboboxField(String id,IModel model, List choices) {
		super(id);
		DropDownChoice field = new DropDownChoice("field",model, choices);
		add(field);
	}

}
