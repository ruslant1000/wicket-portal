package kz.tem.portal.explorer.theme.menu;

import java.io.Serializable;
import java.util.List;

public interface IMenuRenderer extends Serializable{
	
	public String render(List<MenuItem> menu);

}
