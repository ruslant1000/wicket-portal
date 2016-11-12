package kz.tem.portal.explorer.panel.admin.users;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.spring.injection.annot.SpringBean;

import kz.tem.portal.explorer.panel.common.table.AColumn;
import kz.tem.portal.explorer.panel.common.table.AbstractTable;
import kz.tem.portal.server.bean.ITable;
import kz.tem.portal.server.model.Role;
import kz.tem.portal.server.register.IRoleRegister;
/**
 * 
 * @author Ruslan Temirbulatov
 *
 */
@SuppressWarnings("serial")
public class RolesTable extends AbstractTable<Role>{

	@SpringBean
	private IRoleRegister roleRegister;
	
	public RolesTable(String id) {
		super(id, true);
		// TODO Auto-generated constructor stub
	}

	
	@Override
	public Component before(String id) {
		return new RolesControlPanel(id);
	}


	@Override
	public ITable<Role> data(int first, int count) throws Exception {
		return roleRegister.table(first, count);
	}

	@Override
	public AColumn[] columns() throws Exception {
		return new AColumn[]{
				new AColumn<Role>("Role name","name") {

					@Override
					public Component cell(String id, Role record)
							throws Exception {
						return new Label(id,record.getName());
					}
				}
		};
	}

}
