package kz.tem.portal.explorer.panel.common.component.switcher.calendar;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;

@SuppressWarnings("serial")
public class CalendarSwitcherPanel extends Panel{

	private Date selected = null;
	private Date today = new Date();
	
	public CalendarSwitcherPanel(String id, Date date) {
		super(id);
		this.selected=date;
		create();
		setOutputMarkupId(true);
	}
	
	private void create(){
		CalendarSwitcherPanel.this.removeAll();
		RepeatingView view = new RepeatingView("dates");
		add(view);
		
		addButton(view, today);
		addButton(view, new Date(today.getTime()-1*1000*60*60*24L));
		addButton(view, new Date(today.getTime()-2*1000*60*60*24L));
		addButton(view, new Date(today.getTime()-3*1000*60*60*24L));
		
	}
	private static SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
	public void addButton(RepeatingView view,final Date date){
		Button b = new Button(view.newChildId());
		b.add(new AjaxEventBehavior("click"){

			@Override
			protected void onEvent(AjaxRequestTarget target) {
				try {
					onDateClick(target,date);
				} catch (Exception e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}
				
			}});
		b.add(new AttributeModifier("value", sdf.format(date)));
		if(sdf.format(date).equals(sdf.format(selected)))
			b.add(new AttributeModifier("style", "color:red;font-weight:bold;"));
		view.add(b);
	}
	
	public void onDateClick(AjaxRequestTarget target, Date date)throws Exception{
		selected = date;
		create();
		target.add(CalendarSwitcherPanel.this);
	}

}
