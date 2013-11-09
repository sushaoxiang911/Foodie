package edu.umich.eecs441.foodie.web;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
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
import android.util.Log;

public class ReceivePicture extends AsyncTask<String, Void, Bitmap>{

	private final String TAG = "ReceivePicture.";
	private final String urlPrefix = "http://so.meishi.cc/?q=";
//	private final String urlPrefix = "http://www.douguo.com/search/recipe/";
	
	private String mealName;
	
	
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
			
			String HTML = getHTML(arg0[0]);
//			Log.i(TAG + "doInBackground", "HTML: " + HTML);
			
			Document doc = Jsoup.parse(HTML);
			Element content = doc.getElementById("listtyle1_list");
			if (content == null) {
				Log.i(TAG + "doInBackground", "no listtyle1_list");
				return null;
				
			/*	contentSettableActivity.setImageView(BitmapFactory.decodeResource(getResources(), id));*/
			}
			Elements links = content.getElementsByClass("cpimg");
			if (links.size() == 0) {
				//TODO: when there is no result!
				Log.i(TAG + "doInBackground", "No result pic found");
				return null;
				
			}
			Log.i(TAG + "doInBackground", String.valueOf(links.size()));
/*			Elements links = doc.getElementsByClass("scoic mrl");
			
			Log.i(TAG + "doInBackground", "Links size: " + String.valueOf(links.size()));
			
			Log.i(TAG + "doInBackground", links.first().toString());
			*/
			String picContent = links.first().absUrl("src");
			
/*			//TODO: get the meal name and translate
			Elements nameLinks = content.getElementsByClass("info1");
			if (nameLinks.size() == 0) {
				Log.i(TAG + "doInBackground", "No result name found");
			}
			Log.i(TAG + "doInBackground", String.valueOf(nameLinks.size()));
			
			// get meal name
			mealName = nameLinks.first().getElementsByTag("a").first().text();
			Log.i(TAG + "doInBackground", "meal name= " + mealName);
*/			
			mealName = arg0[0];
			
			
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
		new ReceiveTranslation (contentSettableActivity).execute(mealName);
//		contentSettableActivity.dismissProgressDialog();	
//		Log.i(TAG + "onPostExecute", "result: " + result);
	}
	
	
	public String getMealName() {
		return mealName;
	}
}
