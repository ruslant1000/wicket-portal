package kz.tem.portal.explorer.panel.common.ftp;

import java.io.File;
import java.io.Serializable;

public interface FileResourceReader extends Serializable{
	
	public File getFile()throws Exception;
	
	public String getFileName();
	
	public void downloaded()throws Exception;
	
	
}