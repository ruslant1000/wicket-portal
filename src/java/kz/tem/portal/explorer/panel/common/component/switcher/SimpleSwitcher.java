package kz.tem.portal.explorer.panel.common.component.switcher;

import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

@SuppressWarnings("serial")
public class SimpleSwitcher extends Panel{
	
	private Label label = null;

	private Object valuex = null;
	
	public SimpleSwitcher(String id, Object value,final List<Object> variants) {
		super(id);
		this.valuex=value;
		AjaxLink<Void> link = new AjaxLink<Void>("link") {
			
			@Override
			public void onClick(AjaxRequestTarget target) {
				boolean finded =false;
				try{
					for(Object o:variants){
						if(finded){
							finded = false;
							onValue(o);
							label = new Label("title",o.toString());
							label.setOutputMarkupId(true);
							break;
						}
						if(o.toString().equals(SimpleSwitcher.this.valuex.toString())){
							finded = true;
						}
					}
					if(finded){
						onValue(variants.get(0));
						label = new Label("title",variants.get(0).toString());
						label.setOutputMarkupId(true);
					}
					SimpleSwitcher.this.get("link").get("title").replaceWith(label);
					target.add(label);
				}catch(Exception ex){
					ex.printStackTrace();
				}
			}
		};
		add(link);
		
		label = new Label("title",valuex.toString());
		label.setOutputMarkupId(true);
		
		link.add(label);
		link.setOutputMarkupId(true);
	}

	public void onValue(Object newValue)throws Exception{
		valuex = newValue;
	}
}
