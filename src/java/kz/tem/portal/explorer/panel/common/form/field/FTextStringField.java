package kz.tem.portal.explorer.panel.common.form.field;

import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
/**
 * 
 * @author Ruslan Temirbulatov
 *
 */
@SuppressWarnings("serial")
public class FTextStringField extends Panel{

	public FTextStringField(String id, IModel<String> model) {
		super(id);
		TextField<String> field = new TextField<String>("field",model);
		add(field);
	}

	
}
