package kz.tem.portal.explorer.page;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.cycle.RequestCycle;
/**
 * 
 * @author Ruslan Temirbulatov
 *
 */
@SuppressWarnings("serial")
public abstract class AdminPage extends WebPage{

	public AdminPage(){
		//**********************
				// модальное окно
				WebMarkupContainer modal = new WebMarkupContainer("modal");
				add(modal);
				modal.setOutputMarkupId(true);
				modal.add(new WebMarkupContainer("content").setOutputMarkupId(true));
				//**********************
	}
	
	public void closeModal(){
		WebMarkupContainer modal = new WebMarkupContainer("modal");
		modal.setOutputMarkupId(true);
		AdminPage.this.get("modal").replaceWith(modal);
		modal.setVisible(false);
		
		
	}
	public void closeModal(AjaxRequestTarget target){
		closeModal();
		target.add(AdminPage.this.get("modal"));
		
		
	}
	
	public void showModal(String title,AjaxRequestTarget target, IComponentCreator creator){
		
		try {
			AdminPage.this.get("modal").add(new AttributeModifier("style", "display:block"));
			AdminPage.this.get("modal").get("content").replaceWith(creator.create("content").setOutputMarkupId(true));
			
			target.add(AdminPage.this.get("modal"));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
	}
}
