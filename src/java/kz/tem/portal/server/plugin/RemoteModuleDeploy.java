package kz.tem.portal.server.plugin;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class RemoteModuleDeploy {
	
	public static void deploy(String fileName,String moduleName, String portalUrl)throws Exception{
		System.out.println("Deploying module "+fileName+"...");
		
		
		String USER_AGENT = "Mozilla/5.0";
		String url = portalUrl+"/services/upload";
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		String boundary = Long.toHexString(System.currentTimeMillis());
		//add reuqest header
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", USER_AGENT);
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		
		con.setRequestProperty("modulename",moduleName);
		con.setUseCaches(false);
		con.setDoOutput(true);
		con.setRequestProperty(
			    "Content-Type", "multipart/form-data; boundary=" + boundary);
		
		
		File file = new File(fileName);
		con.setRequestProperty("filename", file.getName());
		// Send post request
		con.setDoOutput(true);
		OutputStream os = con.getOutputStream();
//		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		
		FileInputStream fis = new FileInputStream(file);
		
		int read = 0;
		byte[] bytes = new byte[1024];

		while ((read = fis.read(bytes)) != -1) {
			os.write(bytes, 0, read);
		}
		
		
		os.flush();
		os.close();
		fis.close();
//		wr.writeBytes(urlParameters);
//		wr.flush();
//		wr.close();

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'POST' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		
		
		System.out.println("Deploying module "+fileName+" complete");
	}
	
	
	public static void main(String[] args) throws Exception {
		System.out.println("RemoteModuleDeploy...");
		deploy("G:\\projects\\tem-portal\\msys-text\\msystext\\target\\msystext-0.0.1-bundle.zip",
				"msystext",
				"http://localhost:8081/portal");
	}

}
