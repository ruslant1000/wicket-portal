package kz.tem.portal.explorer.panel.common.dialog;

import kz.tem.portal.explorer.page.AbstractThemePage;
import kz.tem.portal.explorer.page.IComponentCreator;
import kz.tem.portal.explorer.panel.common.toolbar.IToolListener;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.panel.Panel;

@SuppressWarnings("serial")
public class ConfirmationDialog extends Panel{

	public ConfirmationDialog(String id, String text) {
		super(id);
		add(new Label("text",text).setEscapeModelStrings(false));
		Button yes = new Button("yes");
		yes.add(new AjaxEventBehavior("click"){

			@Override
			protected void onEvent(AjaxRequestTarget target) {
				try {
					onYes(target);
				} catch (Exception e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}
				
			}});
		add(yes);
		
		Button no = new Button("no");
		no.add(new AjaxEventBehavior("click"){

			@Override
			protected void onEvent(AjaxRequestTarget target) {
				try {
					onNo(target);
				} catch (Exception e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}
				
			}});
		add(no);
		
	}
	
	public void onYes(AjaxRequestTarget target)throws Exception{}
	public void onNo(AjaxRequestTarget target)throws Exception{}
	
	public static void show(final Page page,AjaxRequestTarget target,  String title,final String text, final IToolListener yes, final IToolListener no){
		((AbstractThemePage)page).showModal(title, target, new IComponentCreator() {
			
			@Override
			public Component create(String id) throws Exception {
				ConfirmationDialog d = new ConfirmationDialog(id, text){

					@Override
					public void onYes(AjaxRequestTarget target)throws Exception {
						super.onYes(target);
						((AbstractThemePage)page).closeModal(target);
						if(yes!=null)
							yes.onAction(target);
					}

					@Override
					public void onNo(AjaxRequestTarget target) throws Exception{
						super.onNo(target);
						((AbstractThemePage)page).closeModal(target);
						if(no!=null)
							no.onAction(target);
					}
					
				};
				
				return d;
			}
		});
	}

}
