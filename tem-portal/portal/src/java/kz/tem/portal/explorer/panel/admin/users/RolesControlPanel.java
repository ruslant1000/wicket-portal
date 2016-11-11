package kz.tem.portal.explorer.panel.admin.users;

import kz.tem.portal.explorer.panel.common.form.DefaultInputForm;
import kz.tem.portal.server.model.Role;
import kz.tem.portal.server.register.IRoleRegister;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

@SuppressWarnings("serial")
public class RolesControlPanel extends Panel{

	private String roleName;
	
	@SpringBean
	private IRoleRegister roleRegister;
	
	public RolesControlPanel(String id) {
		super(id);
		DefaultInputForm form = new DefaultInputForm("form"){

			@Override
			public void onSubmit() throws Exception {
				super.onSubmit();
				Role role = new Role();
				role.setName(roleName);
				roleRegister.addNewRole(role);
			}
			
		};
		add(form);
		form.addFieldString("Role name", new PropertyModel<String>(RolesControlPanel.this, "roleName"), true);
	}

}
