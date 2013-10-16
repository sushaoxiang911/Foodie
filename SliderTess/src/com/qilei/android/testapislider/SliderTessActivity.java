package com.qilei.android.testapislider;

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



public class SliderTessActivity extends Activity {
    private static final String TAG = "MainActivity ...";
    
    private static final String TESSBASE_PATH = "/mnt/sdcard/tesseract/";
    private static final String DEFAULT_LANGUAGE = "chi_sim";
    private static final String IMAGE_PATH = "/mnt/sdcard/ocr.jpg";
    private static final String EXPECTED_FILE = TESSBASE_PATH + "tessdata/" + DEFAULT_LANGUAGE
            + ".traineddata";
    
    private TessBaseAPI service;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        testOcr();
        
    }
    
    public void testOcr(){
        mHandler.post(new Runnable() {
            
            @Override
            public void run() {
                Log.d(TAG, "begin>>>>>>>");
                ocr();
                //test();
            }
        });

    }
    public void test(){
        // First, make sure the eng.traineddata file exists.
        /*assertTrue("Make sure that you've copied " + DEFAULT_LANGUAGE + ".traineddata to "
                + EXPECTED_FILE, new File(EXPECTED_FILE).exists());*/
        final TessBaseAPI baseApi = new TessBaseAPI();
        baseApi.init(TESSBASE_PATH, DEFAULT_LANGUAGE);
        final Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
        //digits is a .jpg image I found in one of the issues here.
        ImageView img = (ImageView) findViewById(R.id.image);
        img.setImageBitmap(bmp);//I can see the ImageView. So we know that it should work if I sent it to the setImage()
        baseApi.setImage(bmp);
        Log.v("Kishore","Kishore:Working");//This statement is never reached. Futhermore, on putting some more Log.v commands in the setImage function, I found out that the native function nativeSetImagePix is never accessed. I have attached the Logcat output below to show that it is not accessed.
        
        String outputText = baseApi.getUTF8Text();
        Log.v("Kishore","Kishore:"+outputText);
        baseApi.end();
        bmp.recycle();
    }
    
    protected void ocr() { 
         
        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inSampleSize = 2;
        Bitmap bitmap = BitmapFactory.decodeFile(IMAGE_PATH, options); 
 
        
        Log.d("nimei", "---in ocr()  before try--");
        try {
            Log.v(TAG, "not in the exception");
            ExifInterface exif = new ExifInterface(IMAGE_PATH);
            int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL); 
 
            Log.v(TAG, "Orient: " + exifOrientation); 
 
            int rotate = 0;
            switch (exifOrientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
            } 
 
            Log.v(TAG, "Rotation: " + rotate); 
 
//            if (rotate != 0) {     //杩欏効蹇呴』瑕佹敞閲婃帀锛屽惁鍒欎細鎶ラ敊 浠�箞 ARGB_8888涔嬬被鐨勶紝by 绁佺锛�012骞�鏈�2鏃�17:05:26
 
                // Getting width & height of the given image.
                int w = bitmap.getWidth();
                int h = bitmap.getHeight(); 
 
                // Setting pre rotate
                Matrix mtx = new Matrix();
                mtx.preRotate(rotate); 
 
                // Rotating Bitmap
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, false);
                // tesseract req. ARGB_8888
                bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);   
//            }                  
 
        } catch (IOException e) {
            Log.e(TAG, "Rotate or coversion failed: " + e.toString());
            Log.v(TAG, "in the exception");
        } 
 
        ImageView iv = (ImageView) findViewById(R.id.image);
        iv.setImageBitmap(bitmap);
        iv.setVisibility(View.VISIBLE); 
 
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
        if (recognizedText.length() != 0) {
            ((TextView) findViewById(R.id.field)).setText(recognizedText.trim());
        }
    }
    private Handler mHandler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            
        };
    };
}