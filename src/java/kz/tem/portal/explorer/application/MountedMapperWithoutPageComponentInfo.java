package kz.tem.portal.explorer.application;

import org.apache.wicket.core.request.handler.ListenerInterfaceRequestHandler;
import org.apache.wicket.core.request.mapper.MountedMapper;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.component.IRequestablePage;
import org.apache.wicket.request.mapper.info.PageComponentInfo;
import org.apache.wicket.request.mapper.parameter.PageParametersEncoder;

public class MountedMapperWithoutPageComponentInfo extends MountedMapper{

	public MountedMapperWithoutPageComponentInfo(String mountPath,
			Class<? extends IRequestablePage> pageClass) {
		super(mountPath, pageClass, new PageParametersEncoder());
		// TODO Auto-generated constructor stub
	}
	
	@Override
	  protected void encodePageComponentInfo(Url url, PageComponentInfo info) {
//		  System.out.println("cc "+info.getPageInfo().getPageId());
//		  url.concatSegments(Arrays.asList(".html"));
	  }

	@Override
	public Url mapHandler(IRequestHandler requestHandler) {
		 if (requestHandler instanceof ListenerInterfaceRequestHandler) { 
	          return null; 
	      } else { 
	           return super.mapHandler(requestHandler); 
	      } 
	}
	
	

}
