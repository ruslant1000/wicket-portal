package kz.tem.portal.explorer.page.admin.pages;

import kz.tem.portal.explorer.page.AdminPage;
import kz.tem.portal.explorer.panel.admin.pages.PageForm;
import kz.tem.portal.explorer.panel.admin.pages.PagesTable;

@SuppressWarnings("serial")
public class PagesConfig extends AdminPage{

	public PagesConfig(){
		PagesTable table =new PagesTable("table");
		add(table);
		PageForm form = new PageForm("form"){

			@Override
			public void onSubmit() throws Exception {
				super.onSubmit();
				setResponsePage(getPage());
			}
			
		};
		add(form);
	}
	
}
