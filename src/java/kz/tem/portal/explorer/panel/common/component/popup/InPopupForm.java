package kz.tem.portal.explorer.panel.common.component.popup;

import kz.tem.portal.explorer.panel.common.toolbar.IToolListener2;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;

@SuppressWarnings("serial")
public class InPopupForm extends Panel{

	private String text;
	private TextField txt;
	public InPopupForm(String id, String title, final IToolListener2 listener) {
		super(id);
		setOutputMarkupId(true);
		Form<Void> form = new Form<Void>("form");
		add(form);
		form.add((txt = new TextField<String>("txt",new PropertyModel<String>(InPopupForm.this, "text"))).setOutputMarkupId(true));
		
		AjaxSubmitLink link = new AjaxSubmitLink("submit") {

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				super.onSubmit(target, form);
				try {
					listener.onAction(text, target);
				} catch (Exception e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}
			}
			
		};
		link.add(new Label("label",title));
		form.add(link);
	}

	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
		String js = ""+
				"$('#"+InPopupForm.this.getMarkupId()+"').parent().parent().parent().mouseup(function (e){ "+
				"    text = window.getSelection().toString(); "+
				"   $('#"+txt.getMarkupId()+"').val(text); "+
				"  }); ";
		response.render(new OnDomReadyHeaderItem().forScript(js));
	}
	
}
