package kz.tem.portal.explorer.panel.admin.pages;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.spring.injection.annot.SpringBean;

import kz.tem.portal.PortalException;
import kz.tem.portal.api.RegisterEngine;
import kz.tem.portal.explorer.panel.admin.common.RolesPanel;
import kz.tem.portal.explorer.panel.common.table.AColumn;
import kz.tem.portal.explorer.panel.common.table.AbstractTable;
import kz.tem.portal.server.bean.ITable;
import kz.tem.portal.server.model.Page;
import kz.tem.portal.server.model.Role;
import kz.tem.portal.server.register.IPageRegister;
/**
 * 
 * @author Ruslan Temirbulatov
 *
 */
@SuppressWarnings("serial")
public class PageRoleTable extends AbstractTable<Page>{

	
	public PageRoleTable(String id) {
		super(id, true);
	}

	
	@Override
	public Component before(String id) {
		return new PageRoleControlPanel(id);
	}


	@Override
	public ITable<Page> data(int first, int count) throws Exception {
		return RegisterEngine.getInstance().getPageRegister().pages(first, count);
	}

	@Override
	public AColumn[] columns() throws Exception {
		return new AColumn[]{
				new AColumn<Page>("Page") {

					@Override
					public Component cell(String id, Page record)
							throws Exception {
						return new Label(id,record.getTitle());
					}
				},new AColumn<Page>("Roles") {

					@Override
					public Component cell(String id, final Page record)
							throws Exception {
						return new RolesPanel(id, record.getRole()){

							@Override
							public void roleDeleted(Role role) {
								super.roleDeleted(role);
								record.getRole().remove(role);
								try {
									RegisterEngine.getInstance().getPageRegister().savePage(record);
								} catch (PortalException e) {
									e.printStackTrace();
									throw new RuntimeException(e);
								}
							}
							
						};
					}
				}
		};
	}

}
