package kz.tem.portal.explorer.page.admin.portlets;

import kz.tem.portal.explorer.page.AdminPage;
import kz.tem.portal.explorer.panel.admin.portlets.Portlets;
/**
 * 
 * @author Ruslan Temirbulatov
 *
 */
@SuppressWarnings("serial")
public class PortletsConfig extends AdminPage{

	public PortletsConfig(){
		Portlets portlets = new Portlets("portlets");
		add(portlets);
	}
}
