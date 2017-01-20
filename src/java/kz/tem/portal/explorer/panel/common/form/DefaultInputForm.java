package kz.tem.portal.explorer.panel.common.form;

import java.util.Date;
import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.NonResettingRestartException;
import org.apache.wicket.feedback.ExactLevelFeedbackMessageFilter;
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.markup.Markup;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.flow.RedirectToUrlException;

import kz.tem.portal.explorer.panel.common.table.AColumn;
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
	
	private RepeatingView buttons=null;

	private IFormSubmitButtonListener listener = null;
	
	public DefaultInputForm(String id) {
		super(id);
		
	}
	
	public void build()throws Exception{
		DefaultInputForm.this.removeAll();
		Form<Void> form = new Form<Void>("form"){

			@Override
			protected void onSubmit() {
				super.onSubmit();
				try {
					if(fieldsBuilder.checkFields(DefaultInputForm.this)){
						DefaultInputForm.this.onSubmit();
						if(listener!=null)listener.onSubmit();
						listener=null;
					}
				} catch (Exception e) {
					if(e instanceof RedirectToUrlException)
						throw (RedirectToUrlException)e;
					if(e instanceof NonResettingRestartException)
						throw (NonResettingRestartException)e;
					e.printStackTrace();
					error("Err "+ExceptionUtils.fullError(e));
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
	@Override
	protected void onInitialize() {
		super.onInitialize();
		try {
			build();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error in DefaultInputForm.build() method",e);
		}
		
	}




	public void createButtons(){
		addSubmitButton(submitButtonName(),new IFormSubmitButtonListener() {
			@Override
			public void onSubmit() throws Exception {
				System.out.println("simple submit");
			}
		});
	}
	
	public String submitButtonName(){
		return "Сохранить";
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
					if(e instanceof RedirectToUrlException)
						throw (RedirectToUrlException)e;
					if(e instanceof NonResettingRestartException)
						throw (NonResettingRestartException)e;
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
				DefaultInputForm.this.listener = listener;
				
				
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
	public void addFieldArea(String title, IModel<String> model,
			boolean required) {
		WebMarkupContainer fld = new WebMarkupContainer(fieldSet.newChildId());
		fieldSet.add(fld);
		fld.add(new Label("label",title));
		fld.add(fieldsBuilder.area("f",title, model,required));
		
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




	@Override
	public void addLabel(String title, String text) {
		WebMarkupContainer fld = new WebMarkupContainer(fieldSet.newChildId());
		fieldSet.add(fld);
		fld.add(new Label("label",title));
		fld.add(new Label("f",text));
	}




	@Override
	public void addReadOnlyComponent(AColumn column, Object record) throws Exception {
		WebMarkupContainer fld = new WebMarkupContainer(fieldSet.newChildId());
		fieldSet.add(fld);
		fld.add(new Label("label",column.getTitle()));
		fld.add(column.cell("f", record));
		
	}

	@Override
	public void addFieldDate(String title, IModel<Date> model, String pattern,
			boolean required) {
		WebMarkupContainer fld = new WebMarkupContainer(fieldSet.newChildId());
		fieldSet.add(fld);
		fld.add(new Label("label",title));
		fld.add(fieldsBuilder.date("f",title, model,pattern,required));
		
	}




	
	

}
