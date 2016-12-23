package kz.tem.portal.explorer.panel.common.component.repeater;

import java.util.LinkedList;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.DequeueContext;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.DequeueContext.Bookmark;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.IMarkupFragment;
import org.apache.wicket.markup.IMarkupResourceStreamProvider;
import org.apache.wicket.markup.Markup;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.response.ByteArrayResponse;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.resource.StringResourceStream;

@SuppressWarnings("serial")
public class StatelessRepeater extends WebMarkupContainer{

	private List<Component> components = new LinkedList<Component>();
	
	private String itemTag;
	private String itemClass;
	private String itemStyle;
	
	private Integer number = 1;
	
	public StatelessRepeater(String id, String itemTag, String itemClass, String itemStyle) {
		super(id);
		this.itemTag=itemTag;
		this.itemClass=itemClass;
		this.itemStyle=itemStyle;
	}
	
	public String newChildId(){
		return getId()+"_"+(number++);
	}
	
	public void add(Component component){
		components.add(component);
//		super.add(component);
	}
	
	

	
	
	@Override
	public void onComponentTagBody(MarkupStream markupStream,
			ComponentTag openTag) {
		super.onComponentTagBody(markupStream, openTag);
	}
	
	

	@Override
	protected void onComponentTag(ComponentTag tag) {
		super.onComponentTag(tag);
		
	}

	@Override
	public IMarkupFragment getMarkup(Component child) {
		IMarkupFragment m =super.getMarkup(child);
		System.out.println("UUUUUUUUUUUUUu");
		System.out.println(m.toString());
		
		System.out.println(getMarkup().toString());
		System.out.println(getRegionMarkup().toString(true));
		System.out.println("UUUUUUUUUUUUUu");
		
		return m;
	}
	

	
	

	@Override
	public void dequeue(DequeueContext dequeue) {
		if (size() > 0)
		{
			// essentially what we do is for every child replace the repeater with the child in
			// dequeue container stack and run the dequeue on the child. we also take care to reset
			// the state of the dequeue context after we process every child.

			Bookmark bookmark = dequeue.save();

			for (Component child : this)
			{
				if (child instanceof MarkupContainer)
				{
					dequeue.popContainer(); // pop the repeater
					MarkupContainer container = (MarkupContainer) child;
					dequeue.pushContainer(container);
					container.dequeue(dequeue);
					dequeue.restore(bookmark);
				}
			}
		}

		dequeue.skipToCloseTag();
	}

//	@Override
//	public IResourceStream getMarkupResourceStream(MarkupContainer container,
//			Class<?> containerClass) {
//		
//		String html = "<wicket:panel>";
//		
//		for(int i=1;i<number;i++){
//			html=html+"<"+itemTag+" class=\""+itemClass+"\" style=\""+itemStyle+"\" wicket:id=\""+getId()+"_"+i+"\"/>";
//		}
//		
//		html = html+"</wicket:panel>";
//		
//		StringResourceStream srs = new StringResourceStream(html); 
//		return srs;
//	}

	
	
	

}
