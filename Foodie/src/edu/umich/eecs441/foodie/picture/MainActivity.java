package edu.umich.eecs441.foodie.picture;

import com.example.foodie.R;

import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends Activity {

	private Button button;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		button = (Button)this.findViewById(R.id.button1);
		
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				
				startActivityForResult(intent, 1);
			}
		});
		
	}
	
	protected void onActivityResult (int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if (resultCode == Activity.RESULT_OK) {
			/*
			// check if the sd card is usable
			// TODO: if there is a way to save the picture to non sd place
			// since we will delete it
			String sdCard = Environment.getExternalStorageState();
			if (!sdCard.equals(Environment.MEDIA_MOUNTED)) {
				Log.i("MainActivity.onActivityResult", "sdCard not found");
				return;
			}
			
			// 
*/			
			// get the picture from bundle
			Bundle bundle = data.getExtras();
			Bitmap bitmap = (Bitmap) bundle.get("data");
			
			((ImageView) findViewById(R.id.imageView1)).setImageBitmap(bitmap);
			
			
		}
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
