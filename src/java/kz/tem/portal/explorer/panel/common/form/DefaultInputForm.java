package kz.tem.portal.explorer.panel.common.form;

import java.util.List;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.IModel;

import kz.tem.portal.utils.ExceptionUtils;
/**
 * 
 * @author Ruslan Temirbulatov
 *
 */
@SuppressWarnings("serial")
public class DefaultInputForm extends Panel implements IForm{
	
	private FormFieldsBuilder fieldsBuilder;
	private RepeatingView fieldSet = null;

	public DefaultInputForm(String id) {
		super(id);
		Form<Void> form = new Form<Void>("form"){

			@Override
			protected void onSubmit() {
				super.onSubmit();
				try {
					if(fieldsBuilder.checkFields(DefaultInputForm.this))
						DefaultInputForm.this.onSubmit();
				} catch (Exception e) {
					e.printStackTrace();
					error(ExceptionUtils.fullError(e));
//					throw new RuntimeException();
				}
				
			}
			
		};
		fieldsBuilder = new FormFieldsBuilder();
		fieldSet = new RepeatingView("fields");
		form.add(fieldSet);
		add(form);
		add(new FeedbackPanel("feedbackPanel"));
		
	}
	
	
	public FormFieldsBuilder fields(){
		return fieldsBuilder;
	}
	
	public void onSubmit()throws Exception{
		System.out.println("DefaultInputForm onSubmit");
	}

	@Override
	public void addFieldString(String title, IModel<String> model,
			boolean required)  {
		WebMarkupContainer fld = new WebMarkupContainer(fieldSet.newChildId());
		fieldSet.add(fld);
		fld.add(new Label("label",title));
		fld.add(fieldsBuilder.string("f",title, model,required));
	}

	@Override
	public void addCombobox(String title, IModel model, List choices,
			boolean required) {
		WebMarkupContainer fld = new WebMarkupContainer(fieldSet.newChildId());
		fieldSet.add(fld);
		fld.add(new Label("label",title));
		fld.add(fieldsBuilder.combobox("f",title, model,choices,required));
		
	}
	
	

}
