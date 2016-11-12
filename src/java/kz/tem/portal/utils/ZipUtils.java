package kz.tem.portal.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
/**
 * 
 * @author Ruslan Temirbulatov
 *
 */
public class ZipUtils {

	
	public static void zip(String sourceFolder, String zipFileName)throws Exception{
		List<String> fileList = generateFileList(sourceFolder,new File(sourceFolder));
    	zipIt(sourceFolder,zipFileName,fileList);
	}
	
//    private static final String OUTPUT_ZIP_FILE = "G:\\MyFile.zip";
//    private static final String SOURCE_FOLDER = "G:\\projects\\tem-portal";

   

    public static void main( String[] args )throws Exception
    {
    	zip("C:\\var", "C:\\zxc.zip");
//    	ZipUtils appZip = new ZipUtils();
//    	appZip.generateFileList(new File(SOURCE_FOLDER));
//    	appZip.zipIt(OUTPUT_ZIP_FILE);
    }

    /**
     * Zip it
     * @param zipFile output ZIP file location
     */
    private static void zipIt(String sourceFolder, String zipFile, List<String> fileList){

     byte[] buffer = new byte[1024];

     try{

    	FileOutputStream fos = new FileOutputStream(zipFile);
    	ZipOutputStream zos = new ZipOutputStream(fos);

    	System.out.println("Output to Zip : " + zipFile);

    	for(String file : fileList){

    		System.out.println("File Added : " + file);
    		ZipEntry ze= new ZipEntry(file);
        	zos.putNextEntry(ze);

        	FileInputStream in =
                       new FileInputStream(sourceFolder + File.separator + file);

        	int len;
        	while ((len = in.read(buffer)) > 0) {
        		zos.write(buffer, 0, len);
        	}

        	in.close();
    	}

    	zos.closeEntry();
    	//remember close it
    	zos.close();

    	System.out.println("Done");
    }catch(IOException ex){
       ex.printStackTrace();
    }
   }

    /**
     * Traverse a directory and get all files,
     * and add the file into fileList
     * @param node file or directory
     */
    private static List<String> generateFileList(String sourceFolder,File node){
    	List<String> fileList = new ArrayList<String>();
    	//add file only
	if(node.isFile()){
		fileList.add(generateZipEntry(sourceFolder,node.getAbsoluteFile().toString()));
	}

	if(node.isDirectory()){
		String[] subNote = node.list();
		for(String filename : subNote){
			fileList.addAll(generateFileList(sourceFolder,new File(node, filename)));
		}
	}
		return fileList;
    }

    /**
     * Format the file path for zip
     * @param file file path
     * @return Formatted file path
     */
    private static String generateZipEntry(String sourceFolder, String file){
    	return file.substring(sourceFolder.length()+1, file.length());
    }
}
