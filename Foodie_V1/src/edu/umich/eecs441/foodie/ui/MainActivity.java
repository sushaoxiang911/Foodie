package edu.umich.eecs441.foodie.ui;


import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import com.example.foodie.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {
	
	private Button homeButton;
	private Button searchButton;
	private Button bookMarkButton;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Log.i("Main Activity", "Check tessdata");
		
		Context context = getApplicationContext();
		
		Log.i("confirm listener", "path = " + getApplicationContext().getFilesDir().getPath());
		
		String destination = context.getFilesDir().getPath() + File.separator
				+ "tessdata";
		
		File outputFolder = new File(destination);
		if (!outputFolder.exists()) {
			Log.i("Main Activity", "File doesn't exist");
			
			outputFolder.mkdirs();
			
			
			Log.i("Main Activity check folder", "exists = "  + new File (context.getFilesDir().getPath() + File.separator
				+ "tessdata").isDirectory());
			
		
			File outputFile = new File(destination + File.separator + "chi_sim.traineddata");
			try {
				InputStream is = context.getAssets().open("tessdata" + 
								File.separator + "chi_sim.traineddata");
				outputFile.createNewFile();
				
				Log.i("Main Activity check file", "exists = "  + new File (context.getFilesDir().getPath() + File.separator
						+ "tessdata" + File.separator + "chi_sim.traineddata").isDirectory());
					
				OutputStream os = new FileOutputStream(outputFile);
				byte[] buffer = new byte[5120];
				int length = is.read(buffer);
				while (length > 0) {
					os.write(buffer, 0, length);
					length = is.read(buffer);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			Log.i("Main Activity", "File Existed");
		}
		
		
		homeButton = (Button) findViewById(R.id.homeButton);
		homeButton.setOnClickListener(new OnClickListener() {

		    @Override
		    public void onClick(View arg0) {
		    	homeButton.setBackground(getResources().getDrawable(R.drawable.home2));
		    	
		    }
		});
		
		searchButton = (Button) findViewById(R.id.searchButton);
		searchButton.setOnClickListener(new OnClickListener() {

		    @Override
		    public void onClick(View arg0) {
		    	searchButton.setBackground(getResources().getDrawable(R.drawable.search2));
		    	onGoToSearch(arg0);
		    }
		});
		
		bookMarkButton = (Button) findViewById(R.id.bookmarkButton);
		bookMarkButton.setOnClickListener(new OnClickListener() {

		    @Override
		    public void onClick(View arg0) {
		    	bookMarkButton.setBackground(getResources().getDrawable(R.drawable.bookmark2));
		    	onGoToBookmark(arg0);
		    }
		});
	}
	
	public void onGoToSearch (View view) {
		Intent intent = new Intent(view.getContext(), SearchActivity.class);
		this.startActivity(intent);
	}
	
	public void onGoToBookmark (View view) {
		Intent intent = new Intent(view.getContext(), BookmarkActivity.class);
		this.startActivity(intent);
	}
	

	@Override
	public void onBackPressed() {
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
