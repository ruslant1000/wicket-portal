package kz.tem.portal.explorer.theme;

import kz.tem.portal.explorer.layout.AbstractLayout;
import kz.tem.portal.server.page.PageInfo;

import org.apache.wicket.markup.html.panel.Panel;

@SuppressWarnings("serial")
public class AbstractTheme extends Panel{

	public AbstractTheme(String id, PageInfo info) {
		super(id);
		try {
			AbstractLayout layout = (AbstractLayout)Theme1.class.forName(info.getLayout()).getConstructor(new Class[]{String.class,PageInfo.class}).newInstance(new Object[]{"layout",info});
			add(layout);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

}
