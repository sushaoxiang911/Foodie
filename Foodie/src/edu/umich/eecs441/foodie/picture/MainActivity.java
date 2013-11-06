package edu.umich.eecs441.foodie.picture;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.Calendar;
import java.util.Locale;

import com.example.foodie.R;

import edu.umich.eecs441.foodie.web.ContentSettable;
import edu.umich.eecs441.foodie.web.ReceivePicture;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.app.Activity;
import android.app.ProgressDialog;
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

public class MainActivity extends Activity 
						  implements ContentSettable{

	private Button button;
	final private int CAMERA = 1;
	final private int CROP = 2;
	private ProgressDialog dialog;
	private ImageView image;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		image = (ImageView) this.findViewById(R.id.imageView1);
		
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
			
				String mealName = PictureScanning.scanPicture(photoMap);
				Log.i("MainActivity get the meal name", mealName);
				ReceivePicture receivePicture = new ReceivePicture(this);
				receivePicture.execute(mealName);
				
			
			}		
		}
		
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}


	@Override
	public void startProgressDialog() {
		dialog = ProgressDialog.show(MainActivity.this, "Waiting...", "Obtaining Image");
	}


	@Override
	public void dismissProgressDialog() {
		dialog.dismiss();
	}


	@Override
	public void setImageView(Bitmap bm) {
		// TODO Auto-generated method stub
		image.setImageBitmap(bm);
	}

	@Override
	public void setText(String string) {
		// TODO Auto-generated method stub
		
	}
	
}
