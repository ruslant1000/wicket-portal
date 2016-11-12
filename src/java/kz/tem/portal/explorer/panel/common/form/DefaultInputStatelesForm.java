package kz.tem.portal.explorer.panel.common.form;

import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;
/**
 * 
 * @author Ruslan Temirbulatov
 *
 */
@SuppressWarnings("serial")
public class DefaultInputStatelesForm extends Panel{

	private String name;
	
	public DefaultInputStatelesForm(String id) {
		super(id);
		StatelessForm<Void> form = new StatelessForm<Void>("form"){

			@Override
			protected void onSubmit() {
				super.onSubmit();
				try {
					DefaultInputStatelesForm.this.onSubmit();
					info("отлично");
				} catch (Exception e) {
					e.printStackTrace();
					error("ошибка");
				}
			}
			
		};
		form.add(new TextField<String>("name", new PropertyModel<String>(DefaultInputStatelesForm.this, "name")));
		add(form);
		add(new FeedbackPanel("feedbackPanel"));
	}

	public void onSubmit()throws Exception{
		System.out.println("DefaultInputStatelesForm onSubmit");
	}
}
