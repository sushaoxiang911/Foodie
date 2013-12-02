package edu.umich.eecs441.foodie.picture;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.googlecode.tesseract.android.TessBaseAPI;
import android.graphics.Bitmap;
import android.util.Log;



public class PictureScanning {
	
	 private static final String TAG = "PictureScanning";
	    
	 private String TessbasePath;
	 
	 private static final String DEFAULT_LANGUAGE = "chi_sim";
	 
	 /**
	  * Construct a picture scanning with a tessbase path
	  * @param path
	  */
	 public PictureScanning (String path) {
		 TessbasePath = path;
	 }
	
	 /**
	  * scan a bit map and return the recognized string
	  * @param bitmap
	  * @return
	  */
	 public String scanPicture (Bitmap bitmap) {
 
		 TessBaseAPI baseApi = new TessBaseAPI();
		 baseApi.setDebug(true);
		 baseApi.init(TessbasePath, DEFAULT_LANGUAGE);
		 baseApi.setImage(bitmap);
		 String recognizedText = baseApi.getUTF8Text();
		 baseApi.end(); 
		 
		 Log.v(TAG, "OCR Result before filter: " + recognizedText); 
		 
		 // clean up and show
		 if (DEFAULT_LANGUAGE.equalsIgnoreCase("eng")) {
			 recognizedText = recognizedText.replaceAll("[^a-zA-Z0-9]+", " ");
		 }
		 
		 String regEx = "[^\u4E00-\u9FA5]";
		 Pattern p = Pattern.compile(regEx);
		 Matcher m = p.matcher(recognizedText);
		 String filteredText = m.replaceAll("").trim();
		 
		 Log.v(TAG, "OCR Result after filter: " + filteredText);
		 
		 return filteredText;
	 }	
 }





