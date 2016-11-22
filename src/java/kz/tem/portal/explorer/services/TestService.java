package kz.tem.portal.explorer.services;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.request.resource.AbstractResource;
import org.apache.wicket.util.time.Duration;

@SuppressWarnings("serial")
public class TestService extends AbstractResource{

	@Override
	protected ResourceResponse newResourceResponse(Attributes attributes) {
		ResourceResponse resourceResponse = new ResourceResponse();
	    resourceResponse.setContentType("text/xml");
	    resourceResponse.setTextEncoding("utf-8");
	    resourceResponse.setCacheDuration(Duration.NONE);
	    
	    resourceResponse.setWriteCallback(new WriteCallback()
	    {
	      @Override
	      public void writeData(Attributes attributes) throws IOException
	      {
	    	  String b = attributes.getRequest().getRequestParameters().getParameterValue("b").toString();
	        OutputStream outputStream = attributes.getResponse().getOutputStream();
	        Writer writer = new OutputStreamWriter(outputStream);
	        writer.write("<a><b>"+b+"</b><c>ccsssscc</c></a>");
//	        SyndFeedOutput output = new SyndFeedOutput();
//	            try {
//	          output.output(getFeed(), writer);
	        writer.flush();
	        writer.close();
	      }      
	    });

	    return resourceResponse;
	}

}
