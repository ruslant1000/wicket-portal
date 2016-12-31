package kz.tem.portal.explorer.panel.common.component.message;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;

@SuppressWarnings("serial")
public class AbstractMessage extends Panel{
	
	public static AbstractMessage info(String id, String message){
		return new AbstractMessage(id, message, "info-img");
	}
	public static AbstractMessage error(String id, String message){
		return new AbstractMessage(id, message, "error-img");
	}

	public AbstractMessage(String id, String message, String iconCss) {
		super(id);
		add(new AttributeModifier("class", "message"));
		Label png = new Label("icon");
		png.add(new AttributeModifier("class", iconCss));
		add(png);
		Label l = new Label("message",message);
		add(l);
		l.setEscapeModelStrings(false);
	}

}
