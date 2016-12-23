package kz.tem.portal.explorer.theme.menu;

import java.util.List;

@SuppressWarnings("serial")
public class MenuRenderer1 implements IMenuRenderer{

	@Override
	public String render(List<MenuItem> menu) {
		if(menu==null || menu.size()==0)
			return "";
		
		String str ="<ul class=\"dropdown\">";
		
		for(final MenuItem item:menu){
			
			str=str+"<li class=\"dropdown-top\">";
			str=str+"<nobr><a class=\"dropdown-top"+(item.isSelected()?" selected-item":"")+"\" href=\""+item.url+"\">"+item.title+"</a></nobr>";
			if(item.getChilds().size()>0){
				str=str+"<div class=\"dropdown-inside\">";
				str=str+"<ul>";
				for(MenuItem item2:item.childs){
					str=str+"<li><nobr><a href=\""+item2.url+"\">"+item2.title+"</a></nobr></li>";	
				}
				str=str+"</ul>";
				str=str+"</div>";
			}
			str=str+"</li>";
			
			
//		<li class=\"dropdown-top\">
//			<nobr><a wicket:id="menu-link" class="dropdown-top"/></nobr>
//			<div class="dropdown-inside">
//				<ul>
//					<li wicket:id="sub-menu"><nobr><a wicket:id="sub-menu-link"/></nobr></li>
//				</ul>
//			</div>
//		</li>
		}
		str=str+"</ul>";
		return str;
	}

}
