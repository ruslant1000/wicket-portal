package kz.tem.portal.explorer.panel.common.toolbar;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
/**
 * 
 * @author Ruslan Temirbulatov
 *
 */
@SuppressWarnings("serial")
public class SimpleToolbar extends Panel{

	RepeatingView tools =null;
	
	public SimpleToolbar(String id) {
		super(id);
		tools = new RepeatingView("tools");
		add(tools);
	}
	
	public void addTool(String name, final IToolListener listener){
		
		
		
		Button btn = new Button(tools.newChildId()) {

//			@Override
//			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
//				super.onSubmit(target, form);
//				try {
//					listener.onAction(target);
//				} catch (Exception e) {
//					e.printStackTrace();
//					throw new RuntimeException(e);
//				}
//			}
			
		};
		btn.add(new AjaxEventBehavior("click"){

			@Override
			protected void onEvent(AjaxRequestTarget target) {
				try {
					listener.onAction(target);
				} catch (Exception e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}
				
			}});
		btn.add(new AttributeModifier("value", name));
		tools.add(btn);
	}
	

}
