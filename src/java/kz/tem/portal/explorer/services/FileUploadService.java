package kz.tem.portal.explorer.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import javax.servlet.http.HttpServletRequest;

import kz.tem.portal.explorer.application.PortalApplication;
import kz.tem.portal.server.plugin.engine.ModuleEngine;

import org.apache.wicket.request.resource.AbstractResource;
import org.apache.wicket.util.time.Duration;

@SuppressWarnings("serial")
public class FileUploadService extends AbstractResource {

	@Override
	protected ResourceResponse newResourceResponse(Attributes attributes) {
		ResourceResponse resourceResponse = new ResourceResponse();
		resourceResponse.setContentType("text/xml");
		resourceResponse.setTextEncoding("utf-8");
		resourceResponse.setCacheDuration(Duration.NONE);

		resourceResponse.setWriteCallback(new WriteCallback() {
			@Override
			public void writeData(Attributes attributes) throws IOException {
				HttpServletRequest request = (HttpServletRequest) attributes
						.getRequest().getContainerRequest();

				OutputStream outputStream = attributes.getResponse()
						.getOutputStream();
				Writer writer = new OutputStreamWriter(outputStream);
				
				String filename = request.getHeader("filename");
				String moduleName = request.getHeader("modulename");
				
				System.out.println("filename "+filename);
				
				FileOutputStream fos = null;
				InputStream ins = null;
				try {
					
					ModuleEngine.getInstance().undeploy(moduleName);
					
					System.out.println("!!!! module undeployed");
					Thread.sleep(5000);
					
					ins = request.getInputStream();

					fos = new FileOutputStream(new File(
							PortalApplication.get().getServletContext()
									.getRealPath("modules"), filename));
					int read = 0;
					byte[] bytes = new byte[1024];

					while ((read = ins.read(bytes)) != -1) {
						fos.write(bytes, 0, read);
					}

					System.out.println("Done!");
					writer.write("<responce>OK</responce>");
					writer.flush();
					
					ModuleEngine.getInstance().loadNewModules();
				} catch (Exception e) {
					e.printStackTrace();
					writer.write("<responce>ERROR</responce>");
				} finally {
					if (ins != null) {
						try {
							ins.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					if (fos != null) {
						try {
							// outputStream.flush();
							fos.close();
						} catch (IOException e) {
							e.printStackTrace();
						}

					}
					try{
						writer.close();
					}catch(Exception ex){}
				}

			
				// try {
				// output.output(getFeed(), writer);
				
			}
		});

		return resourceResponse;
	}

}
