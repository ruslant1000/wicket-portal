package kz.tem.portal.explorer.panel.common.ftp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.activation.MimetypesFileTypeMap;

import kz.msystem.commons.socket.processor.java.ftp.FileRecord;
import kz.msystem.commons.socket.processor.java.ftp.FtpMethods;
import kz.tem.portal.utils.FileUtils;

import org.apache.commons.io.IOUtils;
import org.apache.wicket.request.resource.AbstractResource;

@SuppressWarnings("serial")
public class DocFileResource extends AbstractResource{

	private String host;
	private String port;
	private String user;
	private String password;
	private FileRecord file=null;
	
	public DocFileResource(String host, String port, String user, String password, FileRecord file){
		this.file=file;
		this.host=host;
		this.port=port;
		this.user=user;
		this.password=password;
	}

	@Override
	protected ResourceResponse newResourceResponse(Attributes attributes) {
		ResourceResponse resourceResponse = new ResourceResponse();
//	    resourceResponse.setContentType("application/msword");
	    resourceResponse.setContentType(MimetypesFileTypeMap.getDefaultFileTypeMap().getContentType(file.getName()));
	    System.out.println("////////////////");
	    System.out.println(MimetypesFileTypeMap.getDefaultFileTypeMap().getContentType(file.getName()));
	    
	    resourceResponse.setFileName(file.getName());
	    resourceResponse.setTextEncoding("utf-8");
	    

	    resourceResponse.setWriteCallback(new WriteCallback()
	    {
	      @Override
	      public void writeData(Attributes attributes) throws IOException
	      {
	        OutputStream outputStream = attributes.getResponse().getOutputStream();
	        try {
	        	
				ftpFileToOutputStream(outputStream);
			} catch (Exception e) {
				e.printStackTrace();
				throw new IOException(e);
			}
//	        outputStream.write("<a><b>ddd</b><b>gggg</b></a>".getBytes());
	        
	      }      
	    });

	    return resourceResponse;
	}

	public void ftpFileToOutputStream(OutputStream out)throws Exception{
		FtpMethods ftp = new FtpMethods();
		
		if(!ftp.fileExists(host, port, user, password, file.getPath(), file.getName()).equals("true"))
			throw new Exception("DOC Файл не найден: "+file.getPath()+"/"+ file.getName());
		String localFileName  =null;
		try{
			localFileName = ftp.downloadFtpFile(host, port, user, password, (file.getPath()+"/"+ file.getName()).replaceAll("//", "/"), FileUtils.getTempDir());
			if(!new File(localFileName).exists())
				throw new Exception("Не найден файл: "+localFileName);
			IOUtils.copy(new FileInputStream(localFileName), out);
		}finally{
			try{new File(localFileName).delete();}catch(Exception ex){}
		}
		
	}
	
	public static void main(String[] args) {
		String s = "aaaaa.doc.xml";
		System.out.println(s.substring(0,s.length()-4));
	}
}
