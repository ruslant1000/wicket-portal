package kz.tem.portal.explorer.panel.common.ftp;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import kz.msystem.commons.socket.processor.java.ftp.FtpMethods;
import kz.tem.portal.utils.FileUtils;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.MultiFileUploadField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.file.Files;
import org.apache.wicket.util.lang.Bytes;

@SuppressWarnings("serial")
public class FtpMultiFileUploader extends Panel {

	private final Collection<FileUpload> uploads = new ArrayList<>();

	public FtpMultiFileUploader(String id,final String host, final String port, final String user, final String password, final String path) {
		super(id);
		Form<Void> form = new Form<Void>("form") {

			@Override
			protected void onSubmit() {
				super.onSubmit();
				String folder;
				try {
					folder = getUploadFolder();
					List<String> fnames = new LinkedList<String>();
					boolean hasError = false;
					for (FileUpload upload : uploads) {
						// Create a new file
						File newFile = new File(folder,
								upload.getClientFileName());

						// Check new file, delete if it already existed
						checkFileExists(newFile);
						try {
							// Save to new file
							newFile.createNewFile();
							upload.writeTo(newFile);

							FtpMethods ftp = new FtpMethods();
							String reslt = ftp.uploadServerFileToFtp(host, port, user, password, (path+"/"+upload.getClientFileName()).replaceAll("//", "/"), newFile.getAbsolutePath());
							if(reslt.toLowerCase().equals("ok")){
								FtpMultiFileUploader.this.info("saved file: "
										+ upload.getClientFileName());
								fnames.add(upload.getClientFileName());
							}else{
								hasError = true;
								FtpMultiFileUploader.this.info("not saved file: "
										+ upload.getClientFileName());
							}
							
							
						} catch (Exception e) {
							throw new IllegalStateException("Unable to write file");
						}finally{
							try{newFile.delete();}catch(Exception ex){}
						}
					}
					
					onUploaded(fnames);
				} catch (Exception e1) {
					e1.printStackTrace();
					FtpMultiFileUploader.this.error(e1.getMessage());
				}
				
			}

		};
		add(form);
		form.setMultiPart(true);

		form.add(new MultiFileUploadField("file",
				new PropertyModel<Collection<FileUpload>>(this, "uploads"), 5,
				true));

		// Set maximum size to 100K for demo purposes
		form.setMaxSize(Bytes.megabytes(100));

		// Set maximum size per file to 90K for demo purposes
		form.setFileMaxSize(Bytes.megabytes(100));
		
		final FeedbackPanel uploadFeedback = new FeedbackPanel("feeds");

        // Add uploadFeedback to the page itself
        add(uploadFeedback);
	}
	
	public String getUploadFolder() throws Exception{
		String tmpdir = FileUtils.getTempDir();
		return tmpdir;
	}
	private void checkFileExists(File newFile)
    {
        if (newFile.exists())
        {
            // Try to delete the file
            if (!Files.remove(newFile))
            {
                throw new IllegalStateException("Unable to overwrite " + newFile.getAbsolutePath());
            }
        }
    }
	
	
	public void onUploaded(List<String> names)throws Exception{
		
	}
}
