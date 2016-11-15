package kz.tem.portal.explorer.page;

import java.io.Serializable;

import org.apache.wicket.Component;

public interface IComponentCreator extends Serializable{

	public Component create(String id)throws Exception;
}
