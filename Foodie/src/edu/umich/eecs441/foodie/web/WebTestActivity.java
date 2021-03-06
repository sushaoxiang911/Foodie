package edu.umich.eecs441.foodie.web;


import com.example.foodie.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class WebTestActivity extends Activity 
							 implements ContentSettable {

	private EditText searchBox;
	private Button searchButton;
	private ProgressDialog dialog;
//	private ImageView image;
	private TextView text;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView (R.layout.activity_web_test);
		searchBox = (EditText) this.findViewById(R.id.editText1);
		searchButton = (Button) this.findViewById(R.id.button1);
		text = (TextView) this.findViewById(R.id.textView1);
		
		
		searchButton.setOnClickListener(new OnClickListener () {	
			@Override
			public void onClick(View arg0) {
				String searchContent = searchBox.getText().toString();
//				ReceivePicture rp = new ReceivePicture(WebTestActivity.this);
//				rp.execute(searchContent);
				ReceiveTranslation rt = new ReceiveTranslation(WebTestActivity.this);
				try {
					rt.execute(searchContent);
				} catch (Exception e) {
					Log.i("error", "");
					e.printStackTrace();
				}
				
				
				
			}
			
		});
		
		
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}


	@Override
	public void startProgressDialog() {
		dialog = ProgressDialog.show(WebTestActivity.this, "Waiting...", "Obtaining Image");
	}


	@Override
	public void dismissProgressDialog() {
		dialog.dismiss();
	}


	@Override
	public void setImageView(Bitmap bm) {
		// TODO Auto-generated method stub
//		image.setImageBitmap(bm);
	}


	@Override
	public void setText(String string) {
		text.setText(string);
	}
	
	
}
