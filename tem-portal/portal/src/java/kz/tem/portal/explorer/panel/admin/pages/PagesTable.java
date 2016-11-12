package kz.tem.portal.explorer.panel.admin.pages;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.spring.injection.annot.SpringBean;

import kz.tem.portal.explorer.panel.common.table.AbstractTable;
import kz.tem.portal.explorer.panel.common.table.AColumn;
import kz.tem.portal.explorer.panel.common.toolbar.IToolListener;
import kz.tem.portal.explorer.panel.common.toolbar.SimpleToolbar;
import kz.tem.portal.server.bean.ITable;
import kz.tem.portal.server.model.Page;
import kz.tem.portal.server.register.IPageRegister;
/**
 * 
 * @author Ruslan Temirbulatov
 *
 */
@SuppressWarnings("serial")
public class PagesTable extends AbstractTable<Page>{

	@SpringBean
	private IPageRegister pageRegister;
	
	public PagesTable(String id) {
		super(id,true);
	}
	

	@Override
	public Component before(String id) {
		SimpleToolbar tools = new SimpleToolbar(id);
		tools.addTool("Удалить", new IToolListener() {
			
			@Override
			public void onAction(AjaxRequestTarget target) throws Exception {
				System.out.println("delete");
				for(Page pg:getSelectedRows()){
					pageRegister.deletePage(pg.getId());
					System.out.println("  "+pg.getTitle());
				}
				
			}
		});
		return tools;
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
					Label l =new Label(id,"<a href=\""+RequestCycle.get().getRequest().getContextPath()+"/pg/"+record.getUrl()+"\">"+record.getUrl()+"</a>");
					l.setEscapeModelStrings(false);
					return l;
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
			},new AColumn<Page>("Parent page","parentPage") {

				@Override
				public Component cell(String id,final Page record) throws Exception {
					return new Label(id,record.getParentPage()!=null?record.getParentPage().getTitle():"");
				}
			},new AColumn<Page>("Page visible","publicPage") {

				@Override
				public Component cell(String id,final Page record) throws Exception {
					return new Label(id,record.getPublicPage()==true?"Публичная":"Приватная");
				}
			}
		};
		
	}

}
