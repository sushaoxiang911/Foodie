package edu.umich.eecs441.foodie.picture;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.Calendar;
import java.util.Locale;

import com.example.foodie.R;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;



// This is an activity that get a picture from camera
// We first use the built-in camera, get a picture and then save
// an extra to a local place. Notice that we need a folder for 
// our app to save the learning file, we can just put the file there

// TODO try to use rectangle crop
// TODO how to integrate it with our app, not that many activity transferring

// TODO construct local file folder for foodie copy training data into the folder

public class MainActivity extends Activity {

	private Button button;
	final private int CAMERA = 1;
	final private int CROP = 2;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		button = (Button)this.findViewById(R.id.button1);
		
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
				
				File fos = null;
				try {
			//		fos = new File ("/mnt/sdcard/ocr1.jpg");
					fos = new File (Environment.getExternalStorageDirectory().getPath() + File.separator + "ocr1.jpg");
				} catch (Exception e) {
					e.printStackTrace();
				}
			//	Log.i("@!@", "/mnt/sdcard/ocr1.jpg");
				Uri photoUri = Uri.fromFile(fos);
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
				startActivityForResult(intent, CAMERA);
			}
		});
		
	}
	
	protected void onActivityResult (int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if (resultCode == Activity.RESULT_OK) {
			
			// the activity is camera, put into crop activity
			if (requestCode == CAMERA) {
				// get Uri
				File file = new File(Environment.getExternalStorageDirectory().getPath() + File.separator + "ocr1.jpg");
				Uri photoUri = Uri.fromFile(file);
				try {
			  			Log.i("picture onActivityResult", "requestCode == CAMERA");
			  			//call the standard crop action intent (the user device may not support it)
			  			Intent cropIntent = new Intent("com.android.camera.action.CROP"); 
			  			//indicate image type and Uri
			  			cropIntent.setDataAndType(photoUri, "image/*");
			  			//set crop properties
			  			cropIntent.putExtra("crop", "true");
			  			//indicate aspect of desired crop
			  			cropIntent.putExtra("aspectX", 7);
			  			cropIntent.putExtra("aspectY", 1);
			  			//indicate output X and Y
			  			cropIntent.putExtra("outputX", 1792);
			  			cropIntent.putExtra("outputY", 256);
			  			//retrieve data on return
			  			cropIntent.putExtra("return-data", true);
			  			//start the activity - we handle returning in onActivityResult
			  			startActivityForResult(cropIntent, CROP);  
		    	} catch(ActivityNotFoundException e){
		    	//respond to users whose devices do not support the crop action
		    		//display an error message
		    		Log.i("picutre onActivityResult", "does not support crop");
		    		e.printStackTrace();
		    	}
			} else if (requestCode == CROP) {
				// get the returned data
				Bundle extras = data.getExtras();
				// get the cropped map 
				Bitmap photoMap = extras.getParcelable("data");
				// reference to picture view
				((ImageView) findViewById(R.id.imageView1)).setImageBitmap(photoMap);
			}
			
			
			
	/*		
			File bb = new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + File.pathSeparator + "orc.jpg");
			
	
			String sdCard = Environment.getExternalStorageState();
			if (!sdCard.equals(Environment.MEDIA_MOUNTED)) {
				Log.i("MainActivity.onActivityResult", "sdCard not found");
				return;
			}
			
			// 
*/		
			/*String sdStatus = Environment.getExternalStorageState();  
			if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // ���sd�Ƿ����  
				Log.i("TestFile",  
						"SD card is not avaiable/writeable right now.");  
				return;  
			}  
			String name = new DateFormat().format("yyyyMMdd_hhmmss",Calendar.getInstance(Locale.US)) + ".jpg";     
			Log.i("@@@", name);
			Toast.makeText(this, name, Toast.LENGTH_LONG).show();  
			Bundle bundle = data.getExtras();  
			Bitmap bitmap = (Bitmap) bundle.get("data");// ��ȡ���ص���ݣ���ת��ΪBitmapͼƬ��ʽ  

			FileOutputStream b = null;  
			//???????????????????????????????Ϊʲô����ֱ�ӱ�����ϵͳ���λ���أ�����������������������  
			File file = new File(Environment.getExternalStorageDirectory() + "/myImage/");
			Log.i("path", Environment.getExternalStorageDirectory() + "/myImage/");
			file.mkdirs();// �����ļ���  
			String fileName = Environment.getExternalStorageDirectory() + "/myImage/"+name;  

			try {  
					b = new FileOutputStream(fileName);  
					bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// �����д���ļ�  
			} catch (FileNotFoundException e) {  
				e.printStackTrace();  
			} finally {  
				try {  
					b.flush();  
					b.close();  
				} catch (IOException e) {  
					e.printStackTrace();  
				}  
			}  
			
*/
/*			Bundle bundle = data.getExtras();  
			Bitmap bitmap = (Bitmap) bundle.get("data");// ��ȡ���ص���ݣ���ת��ΪBitmapͼƬ��ʽ  

			((ImageView) findViewById(R.id.imageView1)).setImageBitmap(bitmap);
		*/
			
//			performCrop();
			
		}
		
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	
/*	
    private void performCrop(){
    	//take care of exceptions
    	try {
    		
    		Log.i("performCrop", "go into the function");
    		
    		File bb = new File ("/mnt/sdcard/ocr1.jpg");
    		
    		//call the standard crop action intent (the user device may not support it)
	    	Intent cropIntent = new Intent("com.android.camera.action.CROP"); 
	    	//indicate image type and Uri
	    	cropIntent.setDataAndType(Uri.fromFile(bb), "image/*");
	    	//set crop properties
	    	cropIntent.putExtra("crop", "true");
	    	//indicate aspect of desired crop
	    	cropIntent.putExtra("aspectX", 1);
	    	cropIntent.putExtra("aspectY", 1);
	    	//indicate output X and Y
	    	cropIntent.putExtra("outputX", 256);
	    	cropIntent.putExtra("outputY", 256);
	    	//retrieve data on return
	    	cropIntent.putExtra("return-data", true);
	    	//start the activity - we handle returning in onActivityResult
	        //startActivityForResult(cropIntent, PIC_CROP);  
	    	//get the returned data
			Bundle extras = cropIntent.getExtras();
			//get the cropped bitmap
			Bitmap thePic = extras.getParcelable("cropIntent");
			Log.i("performCrop", "the pic!=null?: "+String.valueOf(thePic!=null));
			//retrieve a reference to the ImageView
			ImageView picView = (ImageView)findViewById(R.id.imageView1);
			//display the returned cropped image
			picView.setImageBitmap(thePic);
    	}
    	//respond to users whose devices do not support the crop action
    	catch(ActivityNotFoundException anfe){
    		//display an error message
    		String errorMessage = "Whoops - your device doesn't support the crop action!";
    		Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
    		toast.show();
    	}
    }*/



}
