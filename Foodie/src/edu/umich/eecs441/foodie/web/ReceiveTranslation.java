package edu.umich.eecs441.foodie.web;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

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

public class ReceiveTranslation extends AsyncTask <String, Void, String>{

	static private String TAG = "ReceiveTranslation";
	
	private ContentSettable contentSettableActivity;
	
	private String YouDaoBaseUrl = "http://fanyi.youdao.com/openapi.do";
	private String YouDaoKeyFrom = "Foodie";
	private String YouDaoKey = "2109957658";
	private String YouDaoType  = "data";
	private String YouDaoDocType = "xml";
	private String YouDaoVersion = "1.1";

	public ReceiveTranslation (ContentSettable activity) {
		contentSettableActivity = activity;
	}
	
	
	private String getUrl (String YouDaoSearchContent) {
		String YouDaoUrl = YouDaoBaseUrl + "?keyfrom=" + YouDaoKeyFrom  
                + "&key=" + YouDaoKey + "&type=" + YouDaoType + "&doctype="  
                + YouDaoDocType + "&type=" + YouDaoType + "&version="  
                + YouDaoVersion + "&q=" + YouDaoSearchContent;  
     	
		
		return YouDaoUrl;
	}
	
	
	private String Translate (String YouDaoSearchContent) throws Exception {
		
		String result = "";
		
		String YouDaoUrl = getUrl(YouDaoSearchContent);
		
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
				
				if (basic.getLength() == 0) {
					return new String("Result not found");
				}
				
				Log.i(TAG + "Translate", "basic number" + basic.getLength());

				Element basicElement = (Element)basic.item(0);
				NodeList explanations = ((Element)basicElement.getElementsByTagName("explains").item(0)).getElementsByTagName("ex");
				
				Log.i(TAG + "Translate", "explanations number = " + explanations.getLength());
				
				for (int i = 0; i < explanations.getLength(); i++) {
					Node translation = explanations.item(i); 
					
					if (translation.getNodeType() == Node.ELEMENT_NODE) {
						Element translationElement = (Element) translation;
						
						Node child = translationElement.getFirstChild();
						if (child instanceof CharacterData) {
							CharacterData cd = (CharacterData) child;
							result += cd.getData();
							result += ",";
						}
					}
				}	
			}
			
		}
		
		if (result.length() != 0) {
			return result.substring(0, result.length() - 1);
//			contentSettableActivity.setText(result.substring(0, result.length() - 1));
		} else {
			Log.i(TAG + "Translate", "No Result");
			return new String("Result not found!");
		}
	}


	@Override
	protected String doInBackground(String... params) {
		try {
			return Translate (params[0]);
		} catch (Exception e) {
			Log.i(TAG + "doInBackground", "go in exception");
			e.printStackTrace();
			return new String ("error");
		}
	}
	
	protected void onPostExecute(String result) {
		contentSettableActivity.setText(result);
		Log.i(TAG + "onPostExecute", "finish");
	}
	
}
