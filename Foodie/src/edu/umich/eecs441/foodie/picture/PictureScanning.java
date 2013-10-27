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
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;




public class PictureScanning {
	
	 private static final String TAG = "MainActivity ...";
	    
	 private static final String TESSBASE_PATH = "/mnt/sdcard/tesseract/";
	 private static final String DEFAULT_LANGUAGE = "chi_sim";
	 private static final String IMAGE_PATH = "/mnt/sdcard/ocr.jpg";
	 private static final String EXPECTED_FILE = TESSBASE_PATH + "tessdata/" + DEFAULT_LANGUAGE
			 + ".traineddata";
	    
	 private TessBaseAPI service;
	 
	 
	 
	 
}
