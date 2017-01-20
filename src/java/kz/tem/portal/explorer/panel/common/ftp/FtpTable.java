package kz.tem.portal.explorer.panel.common.ftp;

import java.io.File;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;

import kz.msystem.commons.socket.client.java.ServerSocketException;
import kz.msystem.commons.socket.processor.java.ftp.FileRecord;
import kz.msystem.commons.socket.processor.java.ftp.FtpMethods;
import kz.tem.portal.explorer.page.AbstractThemePage;
import kz.tem.portal.explorer.page.IComponentCreator;
import kz.tem.portal.explorer.panel.common.component.AjaxLabelLink;
import kz.tem.portal.explorer.panel.common.component.link.DownloadLink;
import kz.tem.portal.explorer.panel.common.component.link.InputStreamDownloadLink;
import kz.tem.portal.explorer.panel.common.dialog.ConfirmationDialog;
import kz.tem.portal.explorer.panel.common.table.AColumn;
import kz.tem.portal.explorer.panel.common.table.AbstractTable;
import kz.tem.portal.explorer.panel.common.toolbar.IToolListener;
import kz.tem.portal.explorer.panel.common.toolbar.IToolListener2;
import kz.tem.portal.explorer.panel.common.toolbar.SimpleToolbar;
import kz.tem.portal.server.bean.ITable;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.time.Duration;

@SuppressWarnings("serial")
public class FtpTable extends AbstractTable<FileRecord> {

	// private String socketServerHost;
	// private String socketServerPort;
	private String host;
	private String port;
	private String user;
	private String password;
	private String path;

	private boolean canUpload = true;
	private boolean canDelete = true;
	private boolean canNewFolder = true;

	private List<String> dirs = new LinkedList<String>();
	private static SimpleDateFormat sdf = new SimpleDateFormat(
			"yyyy.MM.dd HH:mm");

	public FtpTable(String id,String host, String port, String user, String password, String path, boolean canUpload ,boolean canDelete, boolean canNewFolder) {
		super(id,true);
		this.host=host;
		this.port=port;
		this.user=user;
		this.path=path;
		this.password=password;
		this.canUpload=canUpload;
		this.canDelete=canDelete;
		this.canNewFolder=canNewFolder;

	}
	

	public String getCurrentPath() {
		String p = path;
		for (String dir : dirs)
			p = p + "/" + dir;
		return p.replaceAll("//", "/");
	}

	
	
	public static String getFileExtension(FileRecord file) {
		if (file.isDirectory())
			return "folder";
		String ext = "csv";
		String[] ss = file.getName().split("\\.");
		if (ss.length > 1) {
			ext = ss[ss.length - 1].toLowerCase();
		}
		return ext;
	}
	


	@Override
	public Component before(String id) {
		
		SimpleToolbar tools = new SimpleToolbar(id);
		if (canUpload)
			tools.addTool("Загрузить", new IToolListener() {

				@Override
				public void onAction(AjaxRequestTarget target) throws Exception {

					((AbstractThemePage) getPage()).showModal(
							"Выбрать файлы для загрузки", target,
							new IComponentCreator() {

								@Override
								public Component create(String id)
										throws Exception {
									FtpMultiFileUploader cmp = new FtpMultiFileUploader(
											id, host, port, user, password,
											getCurrentPath()) {

										@Override
										public void onUploaded(List<String> names)
												throws Exception {
											super.onUploaded(names);
											for(String nm:names)
												FtpTable.this.onUploadedFile(getCurrentPath(), nm);
											build();
										}

									};
									return cmp;
								}
							});

				}
			});
		if (canNewFolder)
			tools.addTool("Новая папка", new IToolListener() {

				@Override
				public void onAction(AjaxRequestTarget target) throws Exception {

					NewFolderDialog.show(getPage(), target, "Новая папка",
							new IToolListener2() {

								@Override
								public void onAction(Object arg,
										AjaxRequestTarget target)
										throws Exception {
									FtpMethods ftp = new FtpMethods();
									if (ftp.dirExists(
											host,
											port,
											user,
											password,
											(getCurrentPath() + "/" + arg)
													.replaceAll("//", "/"))
											.equals("true"))
										throw new Exception(
												"Папка уже существует: " + arg);
									if (!ftp.makeDirectory(
											host,
											port,
											user,
											password,
											(getCurrentPath() + "/" + arg)
													.replaceAll("//", "/"))
											.toUpperCase().equals("OK"))
										throw new Exception(
												"Невозможно добавить папку: "
														+ arg);
									build();
								}
							});

				}
			});
		if (canDelete)
			tools.addTool("Удалить", new IToolListener() {

				@Override
				public void onAction(AjaxRequestTarget target) throws Exception {

					ConfirmationDialog.show(getPage(), target,
							"Удаление файлов...", "Удалить???",
							new IToolListener() {

								@Override
								public void onAction(AjaxRequestTarget target)
										throws Exception {
									System.out.println("удалил");
									FtpMethods ftp = new FtpMethods();
									for (FileRecord f : getSelectedRows()) {
										if (f.isDirectory()) {
											ftp.deleteDirectory(host, port,
													user, password,
													(f.getPath() + "/" + f
															.getName())
															.replaceAll("//",
																	"/"));
										} else {
											try {
												ftp.deleteFile(host, port,
														user, password,
														(f.getPath() + "/" + f
																.getName())
																.replaceAll(
																		"//",
																		"/"));
												FtpTable.this.onDeletedFile(target, f.getPath(), f.getName());
											} catch (Exception ex) {
												System.err
														.println("Could not delete ftp file: "
																+ f.getPath()
																+ "/"
																+ f.getName());
											}
										}
									}
									build();
									target.add(FtpTable.this);
								}
							}, null);
				}
			});
		return tools;
	}

	List<FileRecord> list;
	
	@Override
	public ITable<FileRecord> data(int first, int count) throws Exception {
		FtpMethods ftp = new FtpMethods();
		list = new LinkedList<FileRecord>();
		if (dirs.size() > 0) {
			FileRecord parent = new FileRecord();
			parent.setType(FileRecord.DIRECTORY);
			parent.setName("...");
			list.add(parent);
		}
		list.addAll(ftp.listFilesRecsList(host,
				Integer.parseInt(port), user, password,
				getCurrentPath(), first, count, true, true,
				FtpMethods.SORT_FIELD_CREATED, true));
		String totalStr = ftp.totalFilesCount(host, port, user,
				password, getCurrentPath(), true, true);
		final Long total = Long.parseLong(totalStr);
		return new ITable<FileRecord>() {

			@Override
			public Long total() {
				return total;
			}

			@Override
			public List<FileRecord> records() {
				return list;
			}
		};
	}

	public AColumn<FileRecord> columnFile(){
		return new AColumn<FileRecord>("Файл", "name") {

			@Override
			public Component cell(String id,
					final FileRecord record)
					throws Exception {

				if (record.isDirectory()) {
					AjaxLabelLink ll = new AjaxLabelLink(
							id, record.getName()) {

						@Override
						public void onClick(
								AjaxRequestTarget target)
								throws Exception {
							if (record.getName().equals(
									"...")) {
								dirs.remove(dirs.size() - 1);
							} else
								dirs.add(record.getName());
							setFirst(0);
							build();
							target.add(FtpTable.this);

						}

						@Override
						public void onAjaxLink(
								AjaxLink<Void> link) {
							super.onAjaxLink(link);
							link.add(new AttributeModifier(
									"class",
									"filetype "
											+ getFileExtension(record)));
						}

					};
					return ll;
				} else {
					
					
//					org.apache.wicket.markup.html.link.DownloadLink dl = new org.apache.wicket.markup.html.link.DownloadLink(id, new AbstractReadOnlyModel<File>() {
//
//						@Override
//						public File getObject() {
//							FtpMethods ftp = new FtpMethods();
//							try {
//								ftp.downloadFtpFile(host, port, user, password, (record.getPath()+"/"+record.getName()).replaceAll("//", "/"), "C:/tmp");
//							} catch (ServerSocketException e) {
//								e.printStackTrace();
//							}
//							File f = new File("C:/tmp", record.getName());
//							return f;
//						}
//					},record.getName());
//					dl.setBody(Model.of(""+record.getName()));
//					dl.setCacheDuration(Duration.ONE_SECOND);
//					dl.setOutputMarkupPlaceholderTag(true); 
//					InputStreamDownloadLink dl = new InputStreamDownloadLink(id, new AbstractReadOnlyModel<InputStream>(){
//
//						@Override
//						public InputStream getObject() {
//							FtpMethods ftp = new FtpMethods();
//							try {
//								return ftp.getInputStream(host, port, user, password, (record.getPath()+"/"+record.getName()).replaceAll("//", "/"));
//							} catch (ServerSocketException e) {
//								e.printStackTrace();
//								throw new RuntimeException(e);
//							}
//						}}, Model.of(record.getName()));
					DownloadLink dl = new DownloadLink(id,
							host, port, user, password,
							record);

					return dl;
				}
				// Label l = new
				// Label(id,"<span class=\"filetype "+getFileExtension(record)+"\">"+record.getName()+"</span>");
				// l.setEscapeModelStrings(false);

			}
		};
	}
	public AColumn<FileRecord> columnCreatedDate(){
		return new AColumn<FileRecord>("Создан", "created") {

			@Override
			public Component cell(String id,
					FileRecord record) throws Exception {

				return new Label(id,
						record.isFile() ? sdf.format(record
								.getCreated()) : "");
			}

		};
	}
	@Override
	public AColumn[] columns() throws Exception {
		return new AColumn[] {
				columnFile(),columnCreatedDate()};
	}

	public void onUploadedFile(String path, String fileName)throws Exception{}
	public void onDeletedFile(AjaxRequestTarget target, String path, String fileName)throws Exception{}
}
