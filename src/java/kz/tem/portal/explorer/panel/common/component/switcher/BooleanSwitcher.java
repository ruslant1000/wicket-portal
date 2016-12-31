package kz.tem.portal.explorer.panel.common.component.switcher;

import java.util.LinkedList;
import java.util.List;

@SuppressWarnings("serial")
public class BooleanSwitcher extends SimpleSwitcher{

	public BooleanSwitcher(String id, Boolean value) {
		super(id,value, objs());
	}
	
	public static List<Object> objs(){
		List<Object> list = new LinkedList<Object>();
		list.add(Boolean.TRUE);
		list.add(Boolean.FALSE);
		return list;
	}

	@Override
	public void onValue(Object newValue) throws Exception{
		super.onValue(newValue);
		onValue((Boolean)newValue);
	}
	
	public void onValue(Boolean value)throws Exception{
		
	}
	 
	
	
}
 	