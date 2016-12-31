package kz.tem.portal.explorer.panel.admin.pages;

import kz.tem.portal.api.RegisterEngine;
import kz.tem.portal.explorer.page.AbstractThemePage;
import kz.tem.portal.explorer.page.AdminPage;
import kz.tem.portal.explorer.page.IComponentCreator;
import kz.tem.portal.explorer.panel.common.component.switcher.BooleanSwitcher;
import kz.tem.portal.explorer.panel.common.table.AColumn;
import kz.tem.portal.explorer.panel.common.table.AbstractTable;
import kz.tem.portal.explorer.panel.common.toolbar.IToolListener;
import kz.tem.portal.explorer.panel.common.toolbar.SimpleToolbar;
import kz.tem.portal.server.bean.ITable;
import kz.tem.portal.server.model.Page;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.request.cycle.RequestCycle;
/**
 * 
 * @author Ruslan Temirbulatov
 *
 */
@SuppressWarnings("serial")
public class PagesTable extends AbstractTable<Page>{

	
	public PagesTable(String id) {
		super(id,true);
	}
	

	public void editor(AjaxRequestTarget target, final Page page){
		((AdminPage) getWebPage()).showModal("Новая страница", target, new IComponentCreator() {
			
			@Override
			public Component create(String id) throws Exception {
				PageForm form = new PageForm(id,page){

					@Override
					public void onSubmit() throws Exception {
						super.onSubmit();
						((AdminPage) getWebPage()).closeModal();
					}
				};
				return form;
			}
		});
	}
	
	@Override
	public Component before(String id) {
		SimpleToolbar tools = new SimpleToolbar(id);
		tools.addTool("Новая страница", new IToolListener() {
			
			@Override
			public void onAction(AjaxRequestTarget target) throws Exception {
				editor(target, new Page());
				
			}
		});
		tools.addTool("Удалить", new IToolListener() {
			
			@Override
			public void onAction(AjaxRequestTarget target) throws Exception {
				System.out.println("delete");
				for(Page pg:getSelectedRows()){
					RegisterEngine.getInstance().getPageRegister().deletePage(pg.getId());
					System.out.println("  "+pg.getTitle());
				}
				
			}
		});
		return tools;
	}



	@Override
	public ITable<Page> data(int first, int count) throws Exception {
		return RegisterEngine.getInstance().getPageRegister().pages(first, count);
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
					return new Label(id,record.getPublicPage()==true? PageForm.PAGE_PUBLIC:PageForm.PAGE_PRIVATE);
				}
			},new AColumn<Page>("Page type","pageType") {

				@Override
				public Component cell(String id,final Page record) throws Exception {
					return new Label(id,record.getPageType());
				}
			},new AColumn<Page>("Menu","menu") {

				@Override
				public Component cell(String id,final Page record) throws Exception {
					BooleanSwitcher sw = new BooleanSwitcher(id, record.getMenu()){

						@Override
						public void onValue(Boolean value) throws Exception{
							super.onValue(value);
							record.setMenu(value);
							RegisterEngine.getInstance().getPageRegister().savePage(record);
						}
						
					};
					return sw;
				}
			},new AColumn<Page>("") {

				@Override
				public Component cell(String id,final Page record) throws Exception {
					
					return tools(id, record);
				}
			}
		};
		
	}
	
	public SimpleToolbar tools(String id, final Page page){
		SimpleToolbar t = new SimpleToolbar(id);
		t.addTool("edit", new IToolListener() {
			
			@Override
			public void onAction(AjaxRequestTarget target) throws Exception {
				editor(target, page);
				
			}
		});
		t.addTool("delete", new IToolListener() {
			
			@Override
			public void onAction(AjaxRequestTarget target) throws Exception {
				RegisterEngine.getInstance().getPageRegister().deletePage(page.getId());
				
			}
		});
		return t;
		
	}

}
