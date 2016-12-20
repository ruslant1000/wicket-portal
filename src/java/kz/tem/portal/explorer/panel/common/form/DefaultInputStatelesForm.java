package kz.tem.portal.explorer.panel.common.form;

import java.util.List;

import kz.tem.portal.utils.ExceptionUtils;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.feedback.ExactLevelFeedbackMessageFilter;
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
/**
 * 
 * @author Ruslan Temirbulatov
 *
 */
@SuppressWarnings("serial")
public class DefaultInputStatelesForm extends Panel implements IForm{

	private FormFieldsBuilder fieldsBuilder;
	private RepeatingView fieldSet = null;
	
	private RepeatingView buttons=null;
	
	
	public DefaultInputStatelesForm(String id) {
		super(id);
		StatelessForm<Void> form = new StatelessForm<Void>("form"){

			@Override
			protected void onSubmit() {
				super.onSubmit();
				try {
					if(fieldsBuilder.checkFields(DefaultInputStatelesForm.this))
						DefaultInputStatelesForm.this.onSubmit();
				} catch (Exception e) {
					e.printStackTrace();
					error(ExceptionUtils.fullError(e));
				}
			}
			
		};
		buttons =  new RepeatingView("buttons");
		form.add(buttons);
		createButtons();
		
//		SubmitLink submit = new SubmitLink("submit");
//		form.add(submit);
		
		fieldsBuilder = new FormFieldsBuilder();
		fieldSet = new RepeatingView("fields");
		form.add(fieldSet);
		add(form);
		add(new FeedbackPanel("feedbackPanel", new ExactLevelFeedbackMessageFilter(FeedbackMessage.ERROR)));
		add(new FeedbackPanel("success", new ExactLevelFeedbackMessageFilter(FeedbackMessage.SUCCESS)));
		
	}

	public void createButtons(){
		addSubmitButton("Сохранить ",new IFormSubmitButtonListener() {
			@Override
			public void onSubmit() throws Exception {
				System.out.println("simple submit");
			}
		});
	}
	public FormFieldsBuilder fields(){
		return fieldsBuilder;
	}
	
	public void onSubmit()throws Exception{
		System.out.println("DefaultInputForm onSubmit");
	}
	
	public void addNonSubmitButton(String name, final IFormSubmitButtonListener listener){
		Button button = new Button(buttons.newChildId()){

			@Override
			public void onSubmit() {
				super.onSubmit();
				try {
					listener.onSubmit();
				} catch (Exception e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}
			}
			
		};
		button.add(new Label("value",name));
		button.setDefaultFormProcessing(false);
		buttons.add(button);
	}
	
	public void addSubmitButton(final String name, final IFormSubmitButtonListener listener){
		SubmitLink submit = new SubmitLink(buttons.newChildId()){

			@Override
			public void onSubmit() {
				super.onSubmit();
				try {
					listener.onSubmit();
				} catch (Exception e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}
			}
			
		};
		submit.add(new AttributeModifier("value", name));
		submit.add(new Label("value",name));
		buttons.add(submit);
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


	@Override
	public void addFieldPassword(String title, IModel<String> model,
			boolean required) {
		WebMarkupContainer fld = new WebMarkupContainer(fieldSet.newChildId());
		fieldSet.add(fld);
		fld.add(new Label("label",title));
		fld.add(fieldsBuilder.password("f",title, model,required));
		
	}
}
