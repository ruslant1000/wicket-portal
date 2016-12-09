package kz.tem.portal.explorer.panel.common.toolbar;

import java.io.Serializable;

import org.apache.wicket.ajax.AjaxRequestTarget;

public interface IToolListener2 extends Serializable{

	public void onAction(Object arg, AjaxRequestTarget target)throws Exception;
}
