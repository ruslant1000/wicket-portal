package kz.tem.portal.explorer.panel.common.form;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;

@SuppressWarnings("serial")
public class DefaultInputForm extends Panel{
	
	private String name;

	public DefaultInputForm(String id) {
		super(id);
		Form<Void> form = new Form<Void>("form");
		form.add(new TextField<String>("name", new PropertyModel<String>(DefaultInputForm.this, "name")));
		form.add(new AjaxSubmitLink("submit") {

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				super.onSubmit(target, form);
				System.out.println(name);
			}
			
		});
		
		add(form);
	}

}
