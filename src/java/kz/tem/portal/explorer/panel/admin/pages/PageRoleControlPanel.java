package kz.tem.portal.explorer.panel.admin.pages;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import kz.tem.portal.PortalException;
import kz.tem.portal.explorer.panel.common.form.DefaultInputForm;
import kz.tem.portal.server.model.Page;
import kz.tem.portal.server.model.Role;
import kz.tem.portal.server.register.IPageRegister;
import kz.tem.portal.server.register.IRoleRegister;
/**
 * 
 * @author Ruslan Temirbulatov
 *
 */
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

			@Override
			public void build() throws Exception {
				super.build();
				addCombobox("Page", new PropertyModel<Page>(PageRoleControlPanel.this, "page"), pageRegister.pages(0, 0).records(), true);
				addCombobox("Role", new PropertyModel<Role>(PageRoleControlPanel.this, "role"), roleRegister.table(0, 0).records(), true);
			}
			
			
			
		};
		add(form);

	}
	
	

}
