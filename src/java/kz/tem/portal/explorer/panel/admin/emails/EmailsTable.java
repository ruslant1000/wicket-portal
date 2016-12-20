package kz.tem.portal.explorer.panel.admin.emails;

import java.text.SimpleDateFormat;

import kz.tem.portal.explorer.panel.common.table.AColumn;
import kz.tem.portal.explorer.panel.common.table.AbstractTable;
import kz.tem.portal.server.bean.ITable;
import kz.tem.portal.server.model.Email;
import kz.tem.portal.server.register.IEmailRegister;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.spring.injection.annot.SpringBean;

@SuppressWarnings("serial")
public class EmailsTable extends AbstractTable<Email>{

	@SpringBean
	private IEmailRegister emailRegister;
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm");
	public EmailsTable(String id) {
		super(id, true);
	}
	
	
	@Override
	public ITable<Email> data(int first, int count) throws Exception {
//		return new ITable<Email>() {
//			
//			@Override
//			public Long total() {
//				// TODO Auto-generated method stub
//				return 0L;
//			}
//			
//			@Override
//			public List<Email> records() {
//				// TODO Auto-generated method stub
//				retu	rn new LinkedList<Email>();
//			}
//		};
		
//		return RegisterEngine.getInstance().getEmailRegister().tableEmails(first, count, "created", true);
		return emailRegister.tableEmails(first, count, "created", true);
	}

	@Override
	public AColumn[] columns() throws Exception {
		return new AColumn[]{
				new AColumn<Email>("Тема письма","subject") {

					@Override
					public Component cell(String id, Email record)
							throws Exception {
						return new Label(id,record.getSubject());
					}
				},new AColumn<Email>("Адресат","recipient") {

					@Override
					public Component cell(String id, Email record)
							throws Exception {
						return new Label(id,record.getRecipient());
					}
				},new AColumn<Email>("Статус","status") {

					@Override
					public Component cell(String id, Email record)
							throws Exception {
						return new Label(id,record.getStatus());
					}
				},new AColumn<Email>("Создано","created") {

					@Override
					public Component cell(String id, Email record)
							throws Exception {
						return new Label(id,sdf.format(record.getCreated()));
					}
				},new AColumn<Email>("Отправлено","sended") {

					@Override
					public Component cell(String id, Email record)
							throws Exception {
						return new Label(id,record.getSended()==null?"":sdf.format(record.getSended()));
					}
				}
		};
	}

}
