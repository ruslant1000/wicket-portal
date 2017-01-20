package kz.tem.portal.explorer.panel.common.form.field;

import java.util.Date;

import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.extensions.yui.calendar.DatePicker;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

@SuppressWarnings("serial")
public class FDatePickerField extends Panel{

	public FDatePickerField(String id, IModel<Date> model, String pattern) {
		super(id);
		
		DateTextField f = new DateTextField("field", model, "yyyy.MM.dd");
		add(f);
		
		DatePicker datePicker = new DatePicker();
		datePicker.setShowOnFieldClick(true);
		datePicker.setAutoHide(true);
		f.add(datePicker);
		
	}

}
