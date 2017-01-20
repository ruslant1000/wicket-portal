package kz.tem.portal.explorer.panel.common.form.field;

import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

@SuppressWarnings("serial")
public class FAreaStringField extends Panel{

	public FAreaStringField(String id, IModel<String> model) {
		super(id);
		TextArea<String> field = new TextArea<String>("field", model);
		add(field);
	}

}
