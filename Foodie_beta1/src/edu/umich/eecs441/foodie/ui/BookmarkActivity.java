package edu.umich.eecs441.foodie.ui;


import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import com.example.foodie.R;
import edu.umich.eecs441.foodie.database.FoodieClient;
import edu.umich.eecs441.foodie.database.MealEntry;
import edu.umich.eecs441.foodie.mark.BookmarkAdapter;
import edu.umich.eecs441.foodie.web.WebConnectionCheck;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class BookmarkActivity extends Activity {
	
	private Button homeButton;
	private Button searchButton;
	private Button bookMarkButton;
	
	private ProgressDialog dialog;
	
	private ArrayList<MealEntry> mealList;
	private ArrayList<Bitmap> picList;
	
	
	
	private ListView bookmarkList;
	private BookmarkAdapter bookmarkAdapter;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bookmarks);
		
		bookmarkList = (ListView)this.findViewById(R.id.listView1);
		final Toast checkToast = Toast.makeText(BookmarkActivity.this, 
				"Please connect to internet.", Toast.LENGTH_SHORT);
		
		
		new Thread (new Runnable() {
			@Override
			public void run() {
				BookmarkActivity.this.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						BookmarkActivity.this.dialog = ProgressDialog.show(BookmarkActivity.this, "Waiting", "Loading Information");
					}
				});
				
				
				// loading data
				try {
					mealList = new ArrayList(FoodieClient.getInstance().getMarkingData());
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				picList = new ArrayList<Bitmap>();
				
				for (MealEntry m : mealList) {
					URL picUrl = null;
					Bitmap picMap = null;
					try {
						picUrl = new URL (m.getPicUrl());
					} catch (MalformedURLException e) {
						e.printStackTrace();
					} 
					if (picUrl != null) {
						try {
							picMap = BitmapFactory.decodeStream((InputStream)picUrl.getContent());
						} catch (IOException e) {
							e.printStackTrace();
							// picmap comes from resource
						}
					} else {
						// picmap comes from resource
					}
					picList.add(picMap);
				}
				
				BookmarkActivity.this.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						// call back function
						setContent();
						BookmarkActivity.this.dialog.dismiss();
					}
				});
			}
		}).start();
			
		homeButton = (Button) findViewById(R.id.homeButton);
		homeButton.setOnClickListener(new OnClickListener() {

		    @Override
		    public void onClick(View arg0) {
		    	homeButton.setBackground(getResources().getDrawable(R.drawable.home2));
		    	onGoToHome(arg0);
		    	
		    }
		});
		
		searchButton = (Button) findViewById(R.id.searchButton);
		searchButton.setOnClickListener(new OnClickListener() {

		    @Override
		    public void onClick(View arg0) {
		    	if (!WebConnectionCheck.hasInternetConnection(BookmarkActivity.this))
		    	{
		    		checkToast.show();
		    	}
		    	else
		    	{
		    		searchButton.setBackground(getResources().getDrawable(R.drawable.search2));
		    		bookMarkButton.setBackground(getResources().getDrawable(R.drawable.bookmark1));
		    		onGoToSearch(arg0);
		    	}
		    }
		});
		
		bookMarkButton = (Button) findViewById(R.id.bookmarkButton);
		bookMarkButton.setOnClickListener(new OnClickListener() {

		    @Override
		    public void onClick(View arg0) {
		    	if (!WebConnectionCheck.hasInternetConnection(BookmarkActivity.this))
		    	{
		    		checkToast.show();
		    	}
		    	else
		    	{
		    		bookMarkButton.setBackground(getResources().getDrawable(R.drawable.bookmark2));
		    	}
		    }
		});
	}
	
	public void onGoToHome (View view) {
		Intent intent = new Intent(view.getContext(), MainActivity.class);
		this.startActivity(intent);
	}
	
	public void onGoToSearch (View view) {
		Intent intent = new Intent(view.getContext(), SearchActivity.class);
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

	private void setContent() {
		bookmarkAdapter = new BookmarkAdapter(BookmarkActivity.this, mealList, picList);
		bookmarkList.setAdapter(bookmarkAdapter);
		
	}
}
