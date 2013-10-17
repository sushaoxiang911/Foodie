package edu.umich.eecs441.foodie.web;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.os.AsyncTask;
import android.util.Log;

public class ReceivePicture extends AsyncTask<String, Void, String>{

	private final String TAG = "ReceivePicture.";
	private final String urlPrefix = "http://so.meishi.cc/?q=";
	private ProgressDialogApplicable progressDialogActivity;
	private String mealName;
	private String urlPath;
	
	public ReceivePicture (ProgressDialogApplicable activity) {
		Log.i("ReceivePicture constructor", "enter");
		progressDialogActivity = activity;
	}
	
	private String getHTML (String meal) throws Exception {
		
		Log.i(TAG + "getHTML", "enter");
		mealName = meal;
		urlPath = urlPrefix + mealName;
		URL url = new URL (urlPath);
		
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		
		// GET Method
		conn.setRequestMethod("GET");
		conn.setConnectTimeout(5*1000);  
		
		Log.i(TAG + "getHTML", "URL: " + url.getPath());
		
		InputStream inStream = conn.getInputStream();  
	 
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();  
		byte[] buffer = new byte[1024];  
		int len = 0;  
  
		while( (len = inStream.read(buffer)) !=-1 ){  
           outStream.write(buffer, 0, len);  
		}  
  
		byte[] data = outStream.toByteArray();
  
		outStream.close();  
  
		inStream.close();
		
		return new String(data, "UTF-8");
		/*HttpPost httpRequest = new HttpPost("http://so.meishi.cc");
		List<NameValuePair> params = new ArrayList <NameValuePair>();
		params.add(new BasicNameValuePair("q", meal));
		HttpEntity httpEntity = new UrlEncodedFormEntity(params, "UTF-8");
		httpRequest.setEntity(httpEntity);
		HttpClient client = new DefaultHttpClient();
		HttpResponse httpResponse = client.execute(httpRequest);
		
		return EntityUtils.toString(httpResponse.getEntity());*/
  
			
	}
	protected void onPreExecute() {
		Log.i(TAG + "onPreExecute", "enter!");
		progressDialogActivity.startProgressDialog();
	}
	
	@Override
	protected String doInBackground(String... arg0) {
		// TODO Auto-generated method stub
		try {
			// get the document
			/*Document doc = Jsoup.parse(getHTML(arg0[0]));
			Element content = doc.getElementById("listtyle1_list");
			Elements links = content.getElementsByClass("cpimg");
			
			String picContent = links.first().html();
			
			return picContent;*/
			return getHTML(arg0[0]);
			
		} catch (Exception e) {
			Log.i(TAG + "getPictureUri", "getHTML failed");
			e.printStackTrace();
		}
		return "";
	}
	
	protected void onPostExecute(String result) {
		Log.i(TAG + "onPostExecute", "enter!");
		progressDialogActivity.dismissProgressDialog();
		Log.i(TAG + "onPostExecute", "result: " + result.contains("*"));
	}
}
