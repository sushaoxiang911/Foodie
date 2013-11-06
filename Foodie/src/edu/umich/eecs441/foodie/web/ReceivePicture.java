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
import org.apache.http.protocol.HTTP;
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
	private final String urlPrefix = "http://so.meishi.cc/?q=";
//	private final String urlPrefix = "http://www.douguo.com/search/recipe/";
	private ContentSettable contentSettableActivity;
	
	public ReceivePicture (ContentSettable activity) {
		Log.i("ReceivePicture constructor", "enter");
		contentSettableActivity = activity;
	}
	
	private String getHTML (String meal) throws Exception {
		

		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpPost post = new HttpPost("http://so.meishi.cc/");
		
		List<NameValuePair> pairs = new ArrayList<NameValuePair> ();
		NameValuePair pair1 = new BasicNameValuePair("q", meal);
		NameValuePair pair2 = new BasicNameValuePair("sort", "click");
		
		
		
		pairs.add(pair1);
		pairs.add(pair2);
		HttpEntity entity = new UrlEncodedFormEntity(pairs, HTTP.UTF_8);
		post.setEntity(entity);
		
		HttpResponse response = httpClient.execute(post);
		
		return EntityUtils.toString(response.getEntity());
		
		
		
		
		
	}
	
	
	// check if the picture doesnot exist
	private Bitmap getPicture (String url) throws Exception{
		URL picUrl = new URL (url);
		return BitmapFactory.decodeStream((InputStream)picUrl.getContent());
	}
	
	
	protected void onPreExecute() {
		Log.i(TAG + "onPreExecute", "enter!");
		contentSettableActivity.startProgressDialog();
	}
	
	@Override
	protected Bitmap doInBackground(String... arg0) {
		try {
			Document doc = Jsoup.parse(getHTML(arg0[0]));
			Element content = doc.getElementById("listtyle1_list");
			Elements links = content.getElementsByClass("cpimg");
			if (links.size() == 0) {
				//TODO: when there is no result!
				Log.i(TAG + "doInBackground", "No result found");
				
			}
			Log.i(TAG + "onPreExecute", String.valueOf(links.size()));
/*			Elements links = doc.getElementsByClass("scoic mrl");
			
			Log.i(TAG + "doInBackground", "Links size: " + String.valueOf(links.size()));
			
			Log.i(TAG + "doInBackground", links.first().toString());
			*/
			String picContent = links.first().absUrl("src");
			Log.i(TAG + "doInBackground", "path: " + picContent);
			return getPicture(picContent);
//			return getHTML(arg0[0]);
//			return null;
			
			
		} catch (Exception e) {
			Log.i(TAG + "getPictureUri", "getHTML failed");
			e.printStackTrace();
		}
		return null;
	}
	
	protected void onPostExecute(Bitmap result) {
		Log.i(TAG + "onPostExecute", "enter!");
		contentSettableActivity.setImageView(result);
		contentSettableActivity.dismissProgressDialog();	
//		Log.i(TAG + "onPostExecute", "result: " + result);
	}
}
