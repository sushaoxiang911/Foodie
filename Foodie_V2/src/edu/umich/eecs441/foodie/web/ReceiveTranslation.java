package edu.umich.eecs441.foodie.web;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.lang3.text.WordUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import android.os.AsyncTask;
import android.util.Log;

public class ReceiveTranslation{

	static private String TAG = "ReceiveTranslation";
	
	private String YouDaoBaseUrl = "http://fanyi.youdao.com/openapi.do";
	private String YouDaoKeyFrom = "Foodie";
	private String YouDaoKey = "2109957658";
	private String YouDaoType  = "data";
	private String YouDaoDocType = "xml";
	private String YouDaoVersion = "1.1";
	
	private String mealName;
	
	private String translationResult = "";

	public ReceiveTranslation (String mealName) {
			this.mealName = mealName;
	}
	
	
	private String getUrl () {
		String YouDaoUrl = YouDaoBaseUrl + "?keyfrom=" + YouDaoKeyFrom  
                + "&key=" + YouDaoKey + "&type=" + YouDaoType + "&doctype="  
                + YouDaoDocType + "&type=" + YouDaoType + "&version="  
                + YouDaoVersion + "&q=" + mealName;  
     	
		
		return YouDaoUrl;
	}
	
	
	public String Translate () throws Exception {
		
		String result = "";
		
		// if the mealName is null or mealName is empty
		if (mealName == null || mealName.equals("")) {
			Log.i(TAG + "Translate", "YouDaoSearchContent is null");
			return new String ("Oops! Foodie could not find the food information.");
		}
		
		String YouDaoUrl = getUrl();
		
		// get method
		HttpGet httpGet = new HttpGet (YouDaoUrl);
		
		// send request
		HttpResponse httpResponse = new DefaultHttpClient().execute(httpGet);
		
		// if applicable
		if (httpResponse.getStatusLine().getStatusCode() == 200) {
			String xmlResult = EntityUtils.toString(httpResponse.getEntity());
			Log.i(TAG + "Translate", "xmlResult = " + xmlResult);
			// parse string to a document object
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			InputSource is = new InputSource(new StringReader(xmlResult));
			Document xml = builder.parse(is);
			
			// parse element
			xml.getDocumentElement().normalize();
			// error code
			Element errorCode = (Element) xml.getElementsByTagName("errorCode").item(0);
			
			Log.i(TAG + " Translate", "errorCode = " + errorCode.getTextContent());
			
			if (errorCode.getTextContent().equals("0")) {
				// find translation
				NodeList basic = xml.getElementsByTagName("basic");
				
				/***!!!!!!!!!!!!!!!!*****************/
				if (basic.getLength() == 0) {
					
					// get the web explanation
					NodeList web = xml.getElementsByTagName("web");
					
					if (web.getLength() == 0) {
						return new String("Oops! Foodie could not find the food information.");
					}
					Log.i(TAG + "Translate", "web nunber = " + web.getLength());
					Element webElement = (Element)web.item(0);
					
					NodeList explain = webElement.getElementsByTagName("explain");
					Log.i(TAG + "Translate", "web->explain number = " + explain.getLength());
					
					NodeList value = ((Element)explain.item(0)).getElementsByTagName("value");
					Log.i(TAG + "Translate", "web->explain->value number = " + value.getLength());
					
					NodeList ex = ((Element)value.item(0)).getElementsByTagName("ex");
					Log.i(TAG + "Translate", "web->explain->value->ex number = " + ex.getLength());
					
					Node webTranslation = ex.item(0);
					
					if (webTranslation.getNodeType() == Node.ELEMENT_NODE) {
						Element translationElement = (Element) webTranslation;
							
						Node child = translationElement.getFirstChild();
						if (child instanceof CharacterData) {
							CharacterData cd = (CharacterData) child;
							result =  WordUtils.capitalize(cd.getData());
							Log.i(TAG + "webTranslate", "result = " + result);
						}
					}
					if (result.length() != 0) {
						return result;
//						contentSettableActivity.setText(result.substring(0, result.length() - 1));
					} else {
						Log.i(TAG + "Translate", "No Result");
						return new String("Oops! Foodie could not find the food information.");
					}
				}
				
				Log.i(TAG + "Translate", "basic number" + basic.getLength());
				
				Element basicElement = (Element)basic.item(0);
				NodeList explanations = ((Element)basicElement.getElementsByTagName("explains").item(0)).getElementsByTagName("ex");
				
				Log.i(TAG + "Translate", "explanations number = " + explanations.getLength());
				
				// get the first explanation
				Node translation = explanations.item(0); 
					
				if (translation.getNodeType() == Node.ELEMENT_NODE) {
					Element translationElement = (Element) translation;
						
					Node child = translationElement.getFirstChild();
					if (child instanceof CharacterData) {
						CharacterData cd = (CharacterData) child;
						result =  WordUtils.capitalize(cd.getData());
						Log.i(TAG + "Translate", "result = " + result);
					}
				}
				if (result.length() != 0) {
					return result;
//					contentSettableActivity.setText(result.substring(0, result.length() - 1));
				} else {
					Log.i(TAG + "Translate", "No Result");
					return new String("Oops! Foodie could not find the food information.");
				}
			}
		}
		Log.i(TAG + "Translate", "No Result");
		return new String("Oops! Foodie could not find the food information.");
	}
	
	public String getTranslationResult() {
		return translationResult;
	}
	
	
}
