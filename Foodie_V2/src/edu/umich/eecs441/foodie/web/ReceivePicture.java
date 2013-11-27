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

public class ReceivePicture{

	private final String TAG = "ReceivePicture.";
	private final String urlPrefix = "http://so.meishi.cc/?q=";
//	private final String urlPrefix = "http://www.douguo.com/search/recipe/";
	
	private String recMealName = "";
	private String picMealName = "";
	private String picUrl = "";
	
	public ReceivePicture (String mealName) {
		Log.i("ReceivePicture constructor", "enter");
		recMealName = mealName;
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
	
	
	
	
	protected Bitmap getMealPicture() throws Exception {
			
		String HTML = getHTML(recMealName);
//		Log.i(TAG + "getMealPicture", "HTML: " + HTML);
			
		Document doc = Jsoup.parse(HTML);
		Element content = doc.getElementById("listtyle1_list");
		if (content == null) {
			Log.i(TAG + "getMealPicture", "no listtyle1_list");
			return null;
				
				/*	contentSettableActivity.setImageView(BitmapFactory.decodeResource(getResources(), id));*/
		}
		Elements links = content.getElementsByClass("cpimg");
		if (links.size() == 0) {
			//TODO: when there is no result!
			Log.i(TAG + "getMealPicture", "No result pic found");
			return null;
				
		}
		Log.i(TAG + "getMealPicture", String.valueOf(links.size()));
/*		Elements links = doc.getElementsByClass("scoic mrl");
			
		Log.i(TAG + "getMealPicture", "Links size: " + String.valueOf(links.size()));
			
		Log.i(TAG + "getMealPicture", links.first().toString());
		*/
		String picContent = links.first().absUrl("src");
			
		//get the pic meal name
		Elements nameLinks = content.getElementsByClass("info1");
		if (nameLinks.size() == 0) {
			Log.i(TAG + "getMealPicture", "No result name found");
		}
		Log.i(TAG + "getMealPicture", String.valueOf(nameLinks.size()));
			
		// get meal name
		picMealName = nameLinks.first().getElementsByTag("a").first().text();
//		Log.i(TAG + "getMealPicture", "meal name= " + mealName);
			
		picUrl = picContent;
		Log.i(TAG + "getMealPicture", "path: " + picContent);
		return getPicture(picContent);
//		return getHTML(arg0[0]);
//		return null;
	
	}
	
	
	public String getRecMealName() {
		return recMealName;
	}
	
	public String getPicMealName() {
		return picMealName;
	}
	
	public String getPicUrl () {
		return picUrl;
	}
	
}
