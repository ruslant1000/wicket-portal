package kz.tem.portal.explorer.panel.common.component.link;

import kz.msystem.commons.socket.processor.java.ftp.FileRecord;
import kz.tem.portal.explorer.panel.common.ftp.DocFileResource;
import kz.tem.portal.explorer.panel.common.ftp.FtpTable;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.ResourceLink;
import org.apache.wicket.markup.html.panel.Panel;

@SuppressWarnings("serial")
public class DownloadLink extends Panel{

	private FileRecord file;
	public DownloadLink(String id, String host, String port, String user, String password, FileRecord file) {
		super(id);
		this.file=file;
		ResourceLink<Void> link = new ResourceLink<Void>("link", new DocFileResource(host, port, user, password, file));
		link.add(new AttributeModifier("class","filetype "+FtpTable.getFileExtension(file)));
		link.add(new Label("txt",getLinkLabel()));
		add(link);
		onLink(link);
	}
	
	public void onLink(ResourceLink link){
		
	}
	
	public String getLinkLabel(){
		return file.getName();
	}
	
}
