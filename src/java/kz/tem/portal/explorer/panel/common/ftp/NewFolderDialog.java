package kz.tem.portal.explorer.panel.common.ftp;

import kz.tem.portal.explorer.page.AbstractThemePage;
import kz.tem.portal.explorer.page.IComponentCreator;
import kz.tem.portal.explorer.panel.common.form.DefaultInputForm;
import kz.tem.portal.explorer.panel.common.toolbar.IToolListener2;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;

@SuppressWarnings("serial")
public class NewFolderDialog extends Panel{

	private String name;
	
	public NewFolderDialog(String id) {
		super(id);
		
		DefaultInputForm form = new DefaultInputForm("form"){

			@Override
			public void onSubmit() throws Exception {
				super.onSubmit();
				onNewFolder(name);
			}

			@Override
			public String submitButtonName() {
				return "Создать";
			}
			
			
			
			
		};
		form.addFieldString("Название папки", new PropertyModel<String>(NewFolderDialog.this, "name"), true);
		
		
		
		
		add(form);
		
	}
	
	public void onNewFolder(String name)throws Exception{}
	
	
	
	public static void show(final Page page,AjaxRequestTarget target,  String title, final IToolListener2 onsubmit){
		((AbstractThemePage)page).showModal(title, target, new IComponentCreator() {
			
			@Override
			public Component create(String id) throws Exception {
				NewFolderDialog d = new NewFolderDialog(id){

					@Override
					public void onNewFolder(String name) throws Exception {
						super.onNewFolder(name);
						onsubmit.onAction(name,null);
						((AbstractThemePage)page).closeModal();
					}
					
					
					
				};
				
				return d;
			}
		});
	}

}
