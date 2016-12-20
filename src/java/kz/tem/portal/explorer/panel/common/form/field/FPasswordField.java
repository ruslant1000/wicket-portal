package kz.tem.portal.explorer.panel.common.form.field;

import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
/**
 * 
 * @author Ruslan Temirbulatov
 *
 */
@SuppressWarnings("serial")
public class FPasswordField extends Panel{

	public FPasswordField(String id, IModel<String> model) {
		super(id);
		
		PasswordTextField field = new PasswordTextField("field",model);
		
//		TextField<String> field = new TextField<String>("field",model);
		add(field);
	}

	
}
