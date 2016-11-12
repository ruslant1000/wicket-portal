package kz.tem.portal.explorer.panel.admin.portlets;

import java.util.LinkedList;
import java.util.List;

import kz.tem.portal.PortalException;
import kz.tem.portal.api.PortalEngine;
import kz.tem.portal.api.model.LayoutInfo;
import kz.tem.portal.explorer.panel.common.table.AColumn;
import kz.tem.portal.explorer.panel.common.table.AbstractTable;
import kz.tem.portal.server.bean.ITable;
import kz.tem.portal.server.model.Page;
import kz.tem.portal.server.model.Portlet;
import kz.tem.portal.server.plugin.ModuleMeta;
import kz.tem.portal.server.register.IPageRegister;
import kz.tem.portal.server.register.IPortletRegister;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
/**
 * 
 * @author Ruslan Temirbulatov
 *
 */
@SuppressWarnings("serial")
public class Portlets extends Panel{

	@SpringBean
	private IPageRegister pageRegister;
	@SpringBean
	private IPortletRegister portletRegister;
	
	private Page pagee;
	private String location;
	private ModuleMeta module;
	public Portlets(String id) {
		super(id);
		
		try {
			DropDownChoice<Page> pages =new DropDownChoice<Page>("pages", new PropertyModel<Page>(Portlets.this, "pagee"), pageRegister.pages(0, 0).records()){

				@Override
				protected void onSelectionChanged(Page newSelection) {
					super.onSelectionChanged(newSelection);
					
//					System.out.println(newSelection);
					System.out.println(pagee);
					if(pagee!=null){
						LayoutInfo layoutInfo =PortalEngine.getInstance().getExplorerEngine().getLayout(pagee.getLayout()); 
						if(layoutInfo!=null){
							createLocationSelector(layoutInfo.getLocations());
						}
					}
					hideForm();
				}
				

				@Override
				protected boolean wantOnSelectionChangedNotifications() {
					return true;
				}
				
			};
			pages.setOutputMarkupId(true);
			add(pages);

			createLocationSelector(new LinkedList<String>());
			hideForm();
		} catch (PortalException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	private void createLocationSelector(List<String> choices){
		if(Portlets.this.get("locations")!=null)
			Portlets.this.remove("locations");
		DropDownChoice c = new DropDownChoice<String>("locations",new PropertyModel<String>(Portlets.this, "location"),choices){

			@Override
			protected void onSelectionChanged(String newSelection) {
				super.onSelectionChanged(newSelection);
				if(location==null || location.trim().length()==0){
					hideForm();
				}else{
					showForm();
				}
			}

			@Override
			protected boolean wantOnSelectionChangedNotifications() {
				return true;
			}
			
		};
		c.setOutputMarkupId(true);
		add(c);
	}
	
	private void hideForm(){
		if(Portlets.this.get("form")==null)
			add(new WebMarkupContainer("form").setOutputMarkupId(true));
		Portlets.this.get("form").setVisible(false);
		hideTable();
	}
	private void showForm(){
		if(Portlets.this.get("form")!=null)
			Portlets.this.remove("form");
		Form form = new Form<Void>("form"){

			@Override
			protected void onSubmit() {
				super.onSubmit();
				if(module!=null){
					Portlet p = new Portlet();
					p.setModuleName(module.getModuleName());
					p.setPosition(location);
					p.setPage(pagee);
					try {
						portletRegister.addPortlet(p);
					} catch (PortalException e) {
						e.printStackTrace();
						throw new RuntimeException(e);
					}
				}
			}
			
		};
		add(form);
		form.setOutputMarkupId(true);
		
		DropDownChoice<ModuleMeta> c = new DropDownChoice<ModuleMeta>("module",new PropertyModel<ModuleMeta>(Portlets.this, "module"),PortalEngine.getInstance().getModuleEngine().getModulesList());
		form.add(c);
		form.setVisible(true);
		showTable();
	}

	private void hideTable(){
		if(Portlets.this.get("table")==null)
			add(new WebMarkupContainer("table").setOutputMarkupId(true));
		
		Portlets.this.get("table").setVisible(false);
	}
	private void showTable(){
		if(Portlets.this.get("table")!=null)
			Portlets.this.remove("table");
		
		AbstractTable<Portlet> table = new AbstractTable<Portlet>("table",false) {
			
			@Override
			public ITable<Portlet> data(int first, int count) throws Exception {
				return portletRegister.table(first, count, pagee.getId(),location);
			}
			
			@Override
			public AColumn[] columns() throws Exception {
				return new AColumn[]{
						new AColumn<Portlet>("Module","moduleName") {

							@Override
							public Component cell(String id, Portlet record)
									throws Exception {
								return new Label(id,record.getModuleName());
							}
						},new AColumn<Portlet>("1") {

							@Override
							public Component cell(String id,final Portlet record)
									throws Exception {
								Link<Void> del = new Link<Void>(id) {
									
									@Override
									public void onClick() {
										try {
											portletRegister.deletePortlet(record.getId());
											build();
										} catch (Exception e) {
											e.printStackTrace();
											throw new RuntimeException(e);
										}
										
									}
									
									
								};
								del.setBody(new Model<String>("del"));
								return del;
							}
						}
				};
			}
		};
		add(table);
		table.setVisible(true);
	}
}
