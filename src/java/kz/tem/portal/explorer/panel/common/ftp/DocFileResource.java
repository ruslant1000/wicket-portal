package kz.tem.portal.explorer.panel.common.ftp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.activation.MimetypesFileTypeMap;

import org.apache.commons.io.IOUtils;
import org.apache.wicket.request.flow.RedirectToUrlException;
import org.apache.wicket.request.resource.AbstractResource;
import org.apache.wicket.request.resource.ContentDisposition;
import org.apache.wicket.util.string.StringValue;

@SuppressWarnings("serial")
public class DocFileResource extends AbstractResource{

	private static final String URL_ARG = "resource";
	
	public static Map<String, FileResourceReader> resourceMap = new HashMap<String, FileResourceReader>();

	private static int number = 1;
	
//	private String host;
//	private String port;
//	private String user;
//	private String password;
//	private FileRecord file=null;
	
	public static String urlToResource(FileResourceReader resource)throws Exception{
		return "/download/"+resource.getFileName()+"?"+URL_ARG+"="+newResourceReader(resource);
	}
	
	public static String newResourceReader(FileResourceReader resource)throws Exception{
		String key = "frr-"+number+(resource.getFileName()+"fh3478frh734fhidf9df").hashCode();
		resourceMap.put(key, resource);
		number++;
		return key;
	}
	public DocFileResource(){}
//	public DocFileResource(String host, String port, String user, String password, FileRecord file){
//		this.file=file;
//		this.host=host;
//		this.port=port;
//		this.user=user;
//		this.password=password;
//	}

	@Override
	protected ResourceResponse newResourceResponse(Attributes attributes) {
		
		
		StringValue sv = attributes.getParameters().get(URL_ARG);
		
		if(sv==null || sv.isEmpty() || sv.toString()==null || sv.toString().trim().length()==0 || !resourceMap.containsKey(sv.toString()))
			throw new RedirectToUrlException("/404");
		
		final FileResourceReader reader = resourceMap.get(sv.toString()) ;
		
		ResourceResponse resourceResponse = new ResourceResponse();
//	    resourceResponse.setContentType("application/msword");
	    resourceResponse.setContentType(MimetypesFileTypeMap.getDefaultFileTypeMap().getContentType(reader.getFileName()));
	    
	    resourceResponse.setContentDisposition(ContentDisposition.ATTACHMENT);
	    resourceResponse.setFileName(reader.getFileName());
	    resourceResponse.setTextEncoding("utf-8");
	    
	    
	    try {
			storeFile(reader, resourceResponse);
			resourceResponse.setWriteCallback(new WriteCallback()
		    {
		      @Override
		      public void writeData(Attributes attributes) throws IOException
		      {
		    	  System.out.println("!!!!!!!!!!!!! writeData");
		        OutputStream outputStream = attributes.getResponse().getOutputStream();
		        
		        
                
		        try {
		        	
					ftpFileToOutputStream(outputStream,reader.getFile());
					
				} catch (Exception e) {
					System.out.println("error---");
					
//					e.printStackTrace();
//					throw new IOException(e);
				}finally{
					try{reader.downloaded();}catch(Exception ex){}
				}
//		        outputStream.write("<a><b>ddd</b><b>gggg</b></a>".getBytes());
		        
		      }      
		    });
		} catch (Exception e1) {
			System.out.println("error2----");
//			e1.printStackTrace();
		}
	    

	    

	    return resourceResponse;
	}

	public void storeFile(FileResourceReader reader, ResourceResponse resourceResponse)throws Exception{

		FileInputStream fis = null;
		try{
			resourceResponse.setContentLength((fis = new FileInputStream(reader.getFile())).available());
//			IOUtils.copy(new FileInputStream(localFileName), out);
		}finally{
			try{fis.close();}catch(Exception ex){}
		}
	}
	
	public void ftpFileToOutputStream(OutputStream out, File file)throws Exception{
//		FtpMethods ftp = new FtpMethods();
//		
//		if(!ftp.fileExists(host, port, user, password, file.getPath(), file.getName()).equals("true"))
//			throw new Exception("DOC Файл не найден: "+file.getPath()+"/"+ file.getName());
//		String localFileName  =null;
		try{
//			System.out.println("downloading ...");
//			localFileName = ftp.downloadFtpFile(host, port, user, password, (file.getPath()+"/"+ file.getName()).replaceAll("//", "/"), FileUtils.getTempDir());
//			System.out.println("downloaded");
//			if(!new File(localFileName).exists())
//				throw new Exception("Не найден файл: "+localFileName);
			IOUtils.copy(new FileInputStream(file), out);
		}finally{
			
		}
		
	}
	
	public static void main(String[] args) {
		String s = "aaaaa.doc.xml";
		System.out.println(s.substring(0,s.length()-4));
	}
}


