package kz.tem.portal.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kz.msystem.commons.socket.processor.java.ftp.FileRecord;
import kz.msystem.commons.socket.processor.java.ftp.FtpMethods;

public class Zzzzz {
	
	public static void readFtp()throws Exception{
		File f = null;
		try{
		String host = "192.168.0.18";
		String port = "21";
		String user = "admin";
		String pass = "admin";
		FtpMethods ftp = new FtpMethods();
		
		Map<String,Map<String,String>> map = new HashMap<String, Map<String,String>>();
		
		FileRecord[] recs = ftp.listFilesRecs(host, 21, user, pass, "/distr/msys-poi/bin/review-stats", 0, 1000,true,true);
		
		for(FileRecord rec:recs){
			if(rec.isDirectory()){
				System.out.println(rec.getName());
				f = new File("C:/tmp/stats/"+rec.getName());
				if(f.exists())
					continue;
				FileWriter fw = new FileWriter(f);
				FileRecord[] recs2 = ftp.listFilesRecs(host, 21, user, pass, "/distr/msys-poi/bin/review-stats/"+rec.getName(), 0, 1000,true,true);
				for(FileRecord rec2:recs2){
					if(rec2.getName().endsWith(".stat")){
						String txt = ftp.readFileText(host, "21", user, pass, (rec2.getPath()+"/"+rec2.getName()).replaceAll("//", "/"));
						fw.write(txt+"\n");
					}
				}
				fw.flush();
				fw.close();
				break;
			}
		}
		}catch(Exception ex){
			if(f!=null)
				f.delete();
		}
	}
	
	public static void writeCSV()throws Exception{
		Map<String, Map<String, String>> map = new HashMap<String, Map<String,String>>();
		File dir = new File("C:/tmp/stats");
		for(File f:dir.listFiles()){
			map.put(f.getName(), new HashMap<String, String>());
			BufferedReader br = new BufferedReader(new FileReader(f));
			while(br.ready()){
				String s = br.readLine();
				map.get(f.getName()).put(s.split(":")[0].trim(), s.split(":")[1].trim());
			}
			br.close();
		}
		System.out.println("WRITE...");
		FileWriter fw = new FileWriter("C:/tmp/all-stats.csv");
		String line = "date";
		for(String n:map.keySet()){
			line = line+";"+n;
		}
		fw.write(line+"\n");
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
		String startDate = "01.01.2016";
		String endDate = "31.12.2016";
//		
		while(sdf.parse(startDate).getTime()<sdf.parse(endDate).getTime()){
			line = startDate;
			for(String n:map.keySet()){
				line=line+";"+(map.get(n).containsKey(startDate)?map.get(n).get(startDate):"");
				
			}
			fw.write(line+"\n");
			fw.flush();
			startDate = sdf.format(new Date(sdf.parse(startDate).getTime()+1000*60*60*24));
		}
		fw.close();
//		
//		bw.close();

	}
	
	public static void main(String[] args) throws Exception{
		readFtp();
//		writeCSV();
//		String host = "192.168.0.18";
//		String port = "21";
//		String user = "admin";
//		String pass = "admin";
//		FtpMethods ftp = new FtpMethods();
//		File f = null;
//		Map<String,Map<String,String>> map = new HashMap<String, Map<String,String>>();
//		
//		FileRecord[] recs = ftp.listFilesRecs(host, 21, user, pass, "/distr/msys-poi/bin/review-stats", 0, 1000,true,true);
//		
//		for(FileRecord rec:recs){
//			if(rec.isDirectory()){
//				System.out.println(rec.getName());
//				f = new File("C:/tmp/"+rec.getName());
//				if(f.exists())
//					continue;
//				map.put(rec.getName(), new HashMap<String, String>());
//				FileRecord[] recs2 = ftp.listFilesRecs(host, 21, user, pass, "/distr/msys-poi/bin/review-stats/"+rec.getName(), 0, 1000,true,true);
//				for(FileRecord rec2:recs2){
//					if(rec2.getName().endsWith(".stat")){
//						String txt = ftp.readFileText(host, "21", user, pass, (rec2.getPath()+"/"+rec2.getName()).replaceAll("//", "/"));
//						System.out.println("\t"+txt);
//						map.get(rec.getName()).put(txt.split(":")[0].trim(), txt.split(":")[1].trim());
//					}
//				}
//				break;
//			}
//		}
//		
//		System.out.println("SAVE TO CSV...");
//		FileWriter fw = new FileWriter("C:/tmp/stats.csv");
//		BufferedWriter bw = new BufferedWriter(fw);
//		String line = "date";
//		for(String n:map.keySet()){
//			line = line + ";"+n;
//			
//		}
//		bw.write(line+"\n");
//		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
//		String startDate = "01.01.2016";
//		String endDate = "31.12.2016";
//		
//		while(sdf.parse(startDate).getTime()<sdf.parse(endDate).getTime()){
//			line = startDate;
//			for(String n:map.keySet()){
//				line=line+";"+(map.get(n).containsKey(startDate)?map.get(n).get(startDate):"");
//				
//			}
//			bw.write(line+"\n");
//			bw.flush();
//			startDate = sdf.format(new Date(sdf.parse(startDate).getTime()+1000*60*60*24));
//		}
//		
//		
//		bw.close();
		
	}
}
