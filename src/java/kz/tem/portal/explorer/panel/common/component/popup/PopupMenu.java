package kz.tem.portal.explorer.panel.common.component.popup;

import kz.tem.portal.explorer.panel.common.component.AjaxLabelLink;
import kz.tem.portal.explorer.panel.common.toolbar.IToolListener;
import kz.tem.portal.explorer.panel.common.toolbar.IToolListener2;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;

@SuppressWarnings("serial")
public class PopupMenu extends Panel{
	
	private RepeatingView menu = null;
	private WebMarkupContainer custommenu=null;
	public PopupMenu(String id) {
		super(id);
		custommenu=new WebMarkupContainer("custom-menu");
		add(custommenu);
		custommenu.setOutputMarkupId(true);
		menu = new RepeatingView("menu");
		custommenu.add(menu);
	}
	/**
	 * Добавление меню, которое оперирует с выделенным на странице текстом.
	 * @param title
	 * @param listener
	 */
	public void addAjaxSelectedTextMenu(String title,final IToolListener2 listener){
		WebMarkupContainer li = new WebMarkupContainer(menu.newChildId());
		
		InPopupForm form = new InPopupForm("link", title, listener);
		
		li.add(form);
		menu.add(li);
	}
	
	public void addAjaxMenu(String title,final IToolListener listener){
		WebMarkupContainer li = new WebMarkupContainer(menu.newChildId());
		AjaxLabelLink link = new AjaxLabelLink("link",title) {
			
			@Override
			public void onClick(AjaxRequestTarget target) throws Exception {
				try {
					listener.onAction(target);
				} catch (Exception e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}
				
			}
		};
		li.add(link);
		menu.add(li);
	}

	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
		String js = ""+
		"$(\"#"+custommenu.getMarkupId()+"\").parent().parent().bind(\"contextmenu\", function (event) { "+
		"	 event.preventDefault();"+
		"	 $(\"#"+custommenu.getMarkupId()+"\").finish().toggle(100)."+
		"	 css({"+
		"	     top: event.clientY + \"px\","+
		"	     left: event.clientX + \"px\""+
		"	 });"+
		"	});"+
		"$(document).bind(\"mousedown\", function (e) { "+
		"	 if (!$(e.target).parents(\"#"+custommenu.getMarkupId()+"\").length > 0) {"+
		"	     $(\"#"+custommenu.getMarkupId()+"\").hide(100);"+
		"	 }"+
		"	});"+
		"$(\"#"+custommenu.getMarkupId()+" li\").click(function(){"+
		"	 $(\"#"+custommenu.getMarkupId()+"\").hide(100);"+
		"	});";
	
		response.render(new OnDomReadyHeaderItem().forScript(js));
	}
	
	
	 

}
