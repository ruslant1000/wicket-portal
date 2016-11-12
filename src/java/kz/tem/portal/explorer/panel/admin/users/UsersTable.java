package kz.tem.portal.explorer.panel.admin.users;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.spring.injection.annot.SpringBean;

import kz.tem.portal.explorer.panel.common.table.AColumn;
import kz.tem.portal.explorer.panel.common.table.AbstractTable;
import kz.tem.portal.server.bean.ITable;
import kz.tem.portal.server.model.User;
import kz.tem.portal.server.register.IUserRegister;
/**
 * 
 * @author Ruslan Temirbulatov
 *
 */
@SuppressWarnings("serial")
public class UsersTable extends AbstractTable<User>{

	@SpringBean
	private IUserRegister userRegister;
	public UsersTable(String id) {
		super(id, true);
	}
	
	

	@Override
	public Component before(String id) {
		return new UsersControlPanel(id);
	}



	@Override
	public ITable<User> data(int first, int count) throws Exception {
		return userRegister.table(first, count);
	}

	@Override
	public AColumn[] columns() throws Exception {
		return new AColumn[]{
				new AColumn<User>("Login","login"){

					@Override
					public Component cell(String id, User record)
							throws Exception {
						return new Label(id,record.getLogin());
				}},
				new AColumn<User>("Email","email"){

					@Override
					public Component cell(String id, User record)
							throws Exception {
						return new Label(id,record.getEmail());
				}}
		};
	}

}
