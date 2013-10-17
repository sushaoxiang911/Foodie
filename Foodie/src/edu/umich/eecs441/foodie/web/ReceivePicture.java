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

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.provider.MediaStore.Images;
import android.util.Log;

public class ReceivePicture extends AsyncTask<String, Void, Bitmap>{

	private final String TAG = "ReceivePicture.";
//	private final String urlPrefix = "http://so.meishi.cc/?q=";
	private final String urlPrefix = "http://www.douguo.com/search/recipe/";
	private ImageViewSettable imageViewSettableActivity;
	private String mealName;
	private String urlPath;
	
	public ReceivePicture (ImageViewSettable activity) {
		Log.i("ReceivePicture constructor", "enter");
		imageViewSettableActivity = activity;
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
	}
	
	
	// check if the picture doesnot exist
	private Bitmap getPicture (String url) throws Exception{
		URL picUrl = new URL (url);
		return BitmapFactory.decodeStream((InputStream)picUrl.getContent());
	}
	
	
	protected void onPreExecute() {
		Log.i(TAG + "onPreExecute", "enter!");
		imageViewSettableActivity.startProgressDialog();
	}
	
	@Override
	protected Bitmap doInBackground(String... arg0) {
		// TODO Auto-generated method stub
		try {
			Document doc = Jsoup.parse(getHTML(arg0[0]));
		/*	Element content = doc.getElementById("listtyle1_list");
			Elements links = content.getElementsByClass("cpimg");
		*/
			Elements links = doc.getElementsByClass("scoic mrl");
			
			Log.i(TAG + "doInBackground", "Links size: " + String.valueOf(links.size()));
			
			Log.i(TAG + "doInBackground", links.first().toString());
			
//			String picContent = links.first().absUrl("src");
//			Log.i(TAG + "doInBackground", "path: " + picContent);
//			return getPicture(picContent);
//			return getHTML(arg0[0]);
			return null;
			
			
		} catch (Exception e) {
			Log.i(TAG + "getPictureUri", "getHTML failed");
			e.printStackTrace();
		}
		return null;
	}
	
	protected void onPostExecute(Bitmap result) {
		Log.i(TAG + "onPostExecute", "enter!");
		imageViewSettableActivity.setImageView(result);
		imageViewSettableActivity.dismissProgressDialog();	
//		Log.i(TAG + "onPostExecute", "result: " + result);
	}
}
