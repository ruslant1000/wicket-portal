package kz.tem.portal.explorer.panel.common.component.link;

import java.io.File;

import kz.msystem.commons.socket.processor.java.ftp.FileRecord;
import kz.msystem.commons.socket.processor.java.ftp.FtpMethods;
import kz.tem.portal.explorer.panel.common.ftp.DocFileResource;
import kz.tem.portal.explorer.panel.common.ftp.FileResourceReader;
import kz.tem.portal.explorer.panel.common.ftp.FtpTable;
import kz.tem.portal.utils.FileUtils;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.ResourceLink;
import org.apache.wicket.markup.html.panel.Panel;

@SuppressWarnings("serial")
public class DownloadLink extends Panel{

	private FileRecord file;
	public DownloadLink(String id, final String host, final String port, final String user, final String password, final FileRecord file) throws Exception {
		super(id);
		this.file=file;
		
		
		String href = DocFileResource.urlToResource(new FileResourceReader() {
			
			@Override
			public String getFileName() {
				return file.getName();
			}
			
			@Override
			public File getFile() throws Exception {
				FtpMethods ftp = new FtpMethods();
				String fn = ftp.downloadFtpFile(host, port, user, password, (file.getPath()+"/"+file.getName()).replaceAll("//", "/"), FileUtils.getTempDir());
				return new File(fn);
			}
			
			@Override
			public void downloaded() throws Exception {
				getFile().delete();
				
			}
		});
		
		WebMarkupContainer link = new WebMarkupContainer("link");
		
//		ResourceLink<Void> link = new ResourceLink<Void>("link", new DocFileResource(host, port, user, password, file));
		link.add(new AttributeModifier("href",href));
		link.add(new AttributeModifier("class","filetype "+FtpTable.getFileExtension(file)));
		link.add(new Label("txt",getLinkLabel()));
		add(link);
		onLink(link);
	}
	
	public void onLink(WebMarkupContainer link){
		
	}
	
	public String getLinkLabel(){
		return file.getName();
	}
	
}
