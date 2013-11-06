package edu.umich.eecs441.foodie.picture;

import java.io.IOException;

import android.app.Activity;

import com.googlecode.tesseract.android.TessBaseAPI;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;



public class PictureScanning {
	
	 private static final String TAG = "PictureScanning";
	    
	 private static final String TESSBASE_PATH = "/mnt/sdcard/tesseract/";
	 private static final String DEFAULT_LANGUAGE = "chi_sim";
//	 private static final String IMAGE_PATH = "/mnt/sdcard/ocr.jpg";
	    
	 static public String scanPicture (Bitmap bitmap) {
		 Log.i(TAG + "scanPicture", "enter");
		 
            
   			// TODO: is there a need to rotate, or how to do the vertical characters
		 Log.v(TAG, "Before baseApi"); 
 
		 TessBaseAPI baseApi = new TessBaseAPI();
		 baseApi.setDebug(true);
		 baseApi.init(TESSBASE_PATH, DEFAULT_LANGUAGE);
		 baseApi.setImage(bitmap);
		 String recognizedText = baseApi.getUTF8Text();
		 baseApi.end(); 
		 
		 Log.v(TAG, "OCR Result: " + recognizedText); 
		 
		 // clean up and show
		 if (DEFAULT_LANGUAGE.equalsIgnoreCase("eng")) {
			 recognizedText = recognizedText.replaceAll("[^a-zA-Z0-9]+", " ");
		 }
		 return recognizedText;
	 } 
 }





