package kz.tem.portal.explorer.panel.admin.common;

import java.util.Set;

import kz.tem.portal.explorer.panel.common.component.AjaxDeletableItem;
import kz.tem.portal.server.model.Role;
import kz.tem.portal.server.model.User;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
/**
 * 
 * @author Ruslan Temirbulatov
 *
 */
@SuppressWarnings("serial")
public class RolesPanel extends Panel{

	
	public RolesPanel(String id,Set<Role> roles) {
		super(id);
		RepeatingView rep = new RepeatingView("roles");
		add(rep);
		for(final Role role:roles){
			rep.add(new AjaxDeletableItem(rep.newChildId(), role.getName()){

				@Override
				public void onDelete(AjaxRequestTarget target) throws Exception {
					super.onDelete(target);
					roleDeleted(role);
				}
				
			});
		}
		
	}
	
	public void roleDeleted(Role role){}

}
