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

public class ReceivePicture{
	private final String urlPrefix = "http://so.meishi.cc/?q=";
	
	private String recMealName = "";
	private String picMealName = "";
	private String picUrl = "";
	
	public ReceivePicture (String mealName) {
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
	
	
	private Bitmap getPicture (String url) throws Exception{
		URL picUrl = new URL (url);
		return BitmapFactory.decodeStream((InputStream)picUrl.getContent());
	}
	
	
	
	
	protected Bitmap getMealPicture() throws Exception {
			
		String HTML = getHTML(recMealName);
			
		Document doc = Jsoup.parse(HTML);
		Element content = doc.getElementById("listtyle1_list");
		if (content == null) {
			return null;
				
		}
		Elements links = content.getElementsByClass("img");
		if (links.size() == 0) {
			return null;
				
		}
		String picContent = links.first().absUrl("src");
		// get meal name
		picMealName = links.first().attr("alt");
			
		picUrl = picContent;
		return getPicture(picContent);
	
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
