package kz.tem.portal.explorer.panel.common.component;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;

@SuppressWarnings("serial")
public abstract class AjaxLabelLink extends Panel{

	public AjaxLabelLink(String id, String label) {
		super(id);
		AjaxLink<Void> link = new AjaxLink<Void>("link") {
			
			@Override
			public void onClick(AjaxRequestTarget target) {
				try {
					AjaxLabelLink.this.onClick(target);
				} catch (Exception e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}
				
			}
		};
		add(link);
		link.add(new Label("label",label));
	}

	public abstract void onClick(AjaxRequestTarget target)throws Exception;
	

}
