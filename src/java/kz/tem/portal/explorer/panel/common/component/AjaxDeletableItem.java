package kz.tem.portal.explorer.panel.common.component;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;

@SuppressWarnings("serial")
public class AjaxDeletableItem extends Panel{

	public AjaxDeletableItem(String id, String label) {
		super(id);
		setOutputMarkupId(true);
		Label l = new Label("label",label);
		add(l);
		AjaxLink<Void> link = new AjaxLink<Void>("link") {
			
			@Override
			public void onClick(AjaxRequestTarget target) {
				try {
					onDelete(target);
					AjaxDeletableItem.this.setVisible(false);
					target.add(AjaxDeletableItem.this);
				} catch (Exception e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}
			}
		};
		add(link);
	}
	
	public void onDelete(AjaxRequestTarget target)throws Exception{}

}
