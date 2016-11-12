package kz.tem.portal.explorer.panel.admin.pages;

import java.util.Arrays;

import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import kz.tem.portal.PortalException;
import kz.tem.portal.api.PortalEngine;
import kz.tem.portal.api.model.LayoutInfo;
import kz.tem.portal.api.model.ThemeInfo;
import kz.tem.portal.explorer.panel.common.form.DefaultInputForm;
import kz.tem.portal.server.model.Page;
import kz.tem.portal.server.register.IPageRegister;
/**
 * 
 * @author Ruslan Temirbulatov
 *
 */
@SuppressWarnings("serial")
public class PageForm extends DefaultInputForm{
	
	@SpringBean
	private IPageRegister pageRegister;
	
	private LayoutInfo layout = null;
	private ThemeInfo theme = null;
	private String publicPage = "Публичня";
	
	private Page page;
	public PageForm(String id) {
		this(id, new Page());
	}
	
	public PageForm(String id, Page page) {
		super(id);
		this.page=page;
		if(page.getLayout()!=null && PortalEngine.getInstance().getExplorerEngine().getLayouts().containsKey(page.getLayout()))
			this.layout=PortalEngine.getInstance().getExplorerEngine().getLayouts().get(page.getLayout());
		
		

		
		try {
			addFieldString("Title", new PropertyModel<String>(page, "title"), true);
			addFieldString("URL", new PropertyModel<String>(page, "url"), true);
			addCombobox("Theme", new PropertyModel<ThemeInfo>(PageForm.this, "theme"),PortalEngine.getInstance().getExplorerEngine().getThemesList(),   true);
			addCombobox("Layout", new PropertyModel<LayoutInfo>(PageForm.this, "layout"),PortalEngine.getInstance().getExplorerEngine().getLayoutsList(),   true);
			addCombobox("Parent page", new PropertyModel<Page>(page, "parentPage"),pageRegister.pages(0, 0).records(),   false);
			addCombobox("Page visible", new PropertyModel<String>(PageForm.this, "publicPage"),Arrays.asList("Публичная","Приватная"),   true);
		} catch (PortalException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	public void onSubmit() throws Exception {
		super.onSubmit();
		page.setTheme("kz.tem.portal.explorer.theme.Theme1");
		if(layout!=null){
			page.setLayout(layout.getName());
		}else
			page.setLayout(null);
		
		if(theme!=null){
			page.setTheme(theme.getName());
		}else
			page.setTheme(null);
		
		if(publicPage!=null && publicPage.equals("Публичная"))
			page.setPublicPage(true);
		else
			page.setPublicPage(false);
		pageRegister.addNewPage(page);
		info("Готово!!!");
	}
	
	

}
