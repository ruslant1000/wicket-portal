package kz.tem.portal.explorer.page.admin.emails;

import kz.tem.portal.explorer.page.AdminPage;
import kz.tem.portal.explorer.panel.admin.emails.EmailsTable;

@SuppressWarnings("serial")
public class EmailsPage extends AdminPage{

	public EmailsPage(){
		EmailsTable table =  new EmailsTable("table");
		add(table);
	}
	
}
