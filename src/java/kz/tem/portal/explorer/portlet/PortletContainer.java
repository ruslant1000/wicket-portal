package kz.tem.portal.explorer.portlet;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;

import kz.tem.portal.server.model.Portlet;
import kz.tem.portal.server.plugin.Module;
import kz.tem.portal.server.plugin.ModuleConfig;
import kz.tem.portal.server.plugin.ModuleMeta;
import kz.tem.portal.server.plugin.engine.ModuleEngine;

/**
 * 
 * @author Ruslan Temirbulatov
 *
 */
@SuppressWarnings("serial")
public class PortletContainer extends Panel {

	public PortletContainer(String id, String moduleName, ModuleConfig config, Portlet portlet) {
		super(id);
		ModuleMeta meta = ModuleEngine.getInstance().getModuleMap()
				.get(moduleName);

		Module module;
		try {
			module = ModuleEngine.getInstance().create("module", meta, config);

			add(module);

			final ModalWindow modal = new ModalWindow("mmm");
			add(modal);
			modal.setContent(new PortletSettingsPanel(modal.getContentId(),
					module, portlet));
			modal.setTitle("Modal window\n'panel\" content.");
			modal.setCookieName("modal-2");

			modal.setCloseButtonCallback(new ModalWindow.CloseButtonCallback() {
				@Override
				public boolean onCloseButtonClicked(AjaxRequestTarget target) {
					return true;
				}
			});

			add(new AjaxLink<Void>("config") {
				@Override
				public void onClick(AjaxRequestTarget target) {
					modal.show(target);
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
			add(new Label("module", "Модуль недоступен"));
		}

	}

}
