package kz.tem.portal.explorer.panel.admin.pages;

import kz.tem.portal.PortalException;
import kz.tem.portal.explorer.panel.common.form.DefaultInputForm;
import kz.tem.portal.server.model.Page;
import kz.tem.portal.server.model.Role;
import kz.tem.portal.server.register.IPageRegister;
import kz.tem.portal.server.register.IRoleRegister;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

@SuppressWarnings("serial")
public class PageRoleControlPanel extends Panel{

	@SpringBean
	private IPageRegister pageRegister;
	@SpringBean
	private IRoleRegister roleRegister;
	
	private Page page;
	private Role role;
	
	public PageRoleControlPanel(String id) {
		super(id);
		DefaultInputForm form = new DefaultInputForm("form"){

			@Override
			public void onSubmit() throws Exception {
				super.onSubmit();
				page.getRole().add(role);
				pageRegister.savePage(page);
			}
			
		};
		add(form);
		try {
			form.addCombobox("Page", new PropertyModel<Page>(PageRoleControlPanel.this, "page"), pageRegister.pages(0, 0).records(), true);
			form.addCombobox("Role", new PropertyModel<Role>(PageRoleControlPanel.this, "role"), roleRegister.table(0, 0).records(), true);
		} catch (PortalException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	

}
