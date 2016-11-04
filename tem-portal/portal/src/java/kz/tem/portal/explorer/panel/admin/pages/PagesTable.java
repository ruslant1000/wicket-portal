package kz.tem.portal.explorer.panel.admin.pages;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.spring.injection.annot.SpringBean;

import kz.tem.portal.explorer.panel.common.table.AbstractTable;
import kz.tem.portal.explorer.panel.common.table.AColumn;
import kz.tem.portal.server.bean.ITable;
import kz.tem.portal.server.model.Page;
import kz.tem.portal.server.register.IPageRegister;

@SuppressWarnings("serial")
public class PagesTable extends AbstractTable<Page>{

	@SpringBean
	private IPageRegister pageRegister;
	
	public PagesTable(String id) {
		super(id);
	}

	@Override
	public ITable<Page> data(int first, int count) throws Exception {
		return pageRegister.pages(first, count);
	}

	@Override
	public AColumn[] columns() throws Exception {
		return new AColumn[]{
			new AColumn<Page>("Title","title") {

				@Override
				public Component cell(String id,final Page record) throws Exception {
					return new Label(id,record.getTitle());
				}
			},new AColumn<Page>("URL","url") {

				@Override
				public Component cell(String id,final Page record) throws Exception {
					return new Label(id,record.getUrl());
				}
			},new AColumn<Page>("Theme","theme") {

				@Override
				public Component cell(String id,final Page record) throws Exception {
					return new Label(id,record.getTheme());
				}
			},new AColumn<Page>("Layout","layout") {

				@Override
				public Component cell(String id,final Page record) throws Exception {
					return new Label(id,record.getLayout());
				}
			}
		};
		
	}

}
