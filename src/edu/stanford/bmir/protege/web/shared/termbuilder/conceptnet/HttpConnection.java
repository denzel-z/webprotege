package edu.stanford.bmir.protege.web.shared.termbuilder.conceptnet;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpConnection {
	
	private String response;
	private int TIMEOUT = 3000;
	
	public HttpConnection() {
		response = "";
	}
	
	public void sendGet(String url) throws Exception {
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		
		con.setRequestMethod("GET");
		con.setConnectTimeout(TIMEOUT);
		
		int responseCode = con.getResponseCode();
		BufferedReader in = new BufferedReader(
				new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer sb = new StringBuffer();
 
		while ((inputLine = in.readLine()) != null) {
			sb.append(inputLine);
		}
		in.close();
		
		response = sb.toString();
	}
	
	public String getResponseString() {
		return response;
	}
	
}