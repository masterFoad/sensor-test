package com.sensor.test.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import com.google.gson.Gson;
import org.json.simple.JSONObject;

public class RestUtils {


	public static class JActivity {
		public String Username;
		public String Action;
		public long ActionTime;
		public int id;


		public JActivity(String username, String action, long actionTime, int id) {
			Username = username;
			Action = action;
			ActionTime = actionTime;
			this.id = id;
		}


		public String getUsername() {
			return Username;
		}

		public void setUsername(String username) {
			Username = username;
		}

		public String getAction() {
			return Action;
		}

		public void setAction(String action) {
			Action = action;
		}

		public long getActionTime() {
			return ActionTime;
		}

		public void setActionTime(long actionTime) {
			ActionTime = actionTime;
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getJsonString(){
			Gson gson = new Gson();

    		return gson.toJson(this);
		}
	}
	
	@SuppressWarnings("unchecked")
	public static void postActionToCloud(String action) throws IOException {
		
		// Create all-trusting host name verifier
		HostnameVerifier allHostsValid = new HostnameVerifier() {
		    public boolean verify(String hostname, SSLSession session) {
		        return true;
		    }
		};
		// Install the all-trusting host verifier
		HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
		
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		
		String url="https://gym.apic.eu-gb.mybluemix.net/api/Actions/1";
		URL object=new URL(url);

		HttpsURLConnection con = (HttpsURLConnection) object.openConnection();
		con.setDoOutput(true);
		con.setDoInput(true);
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("Accept", "application/json");
		con.setRequestMethod("PUT");

//		JSONObject parent=new JSONObject();

		var ja = new JActivity("Eran", action, timestamp.getTime(), 1);
//		parent.put("Username", "Eran");
//		parent.put("Action", action);
//		parent.put("ActionTime", timestamp.getTime());
//		parent.put("id", 1);

		OutputStreamWriter wr= new OutputStreamWriter(con.getOutputStream());
		wr.write(ja.getJsonString());
		wr.flush();


//		System.out.println("SENT &$@$^@#^@#^@#^@#^@#");

		//display what returns the POST request
		StringBuilder sb = new StringBuilder();  
		int HttpResult = con.getResponseCode(); 
		if (HttpResult == HttpURLConnection.HTTP_OK) {
		    BufferedReader br = new BufferedReader(
		            new InputStreamReader(con.getInputStream(), "utf-8"));
		    String line = null;  
		    while ((line = br.readLine()) != null) {  
		        sb.append(line + "\n");  
		    }
		    br.close();
		    System.out.println("" + sb.toString());
		} else {
		    System.out.println(con.getResponseMessage());
		}  
	}

}
