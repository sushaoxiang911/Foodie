package edu.umich.eecs441.foodie.ui;

import edu.umich.eecs441.foodie.R;
import edu.umich.eecs441.foodie.database.FoodieClient;
import edu.umich.eecs441.foodie.web.WebConnectionCheck;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class PreviewActivity extends Activity {
	
	private Button homeButton;
	private Button searchButton;
	private Button bookMarkButton;
	
	private Button recapture;
	
	private ImageView obtainedImage;
	
	private Button confirm;
	
	private Bitmap bitmap = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_preview);
		
		final Toast loginToast = Toast.makeText(PreviewActivity.this, 
				"Please log in.", Toast.LENGTH_SHORT);
		final Toast checkToast = Toast.makeText(PreviewActivity.this, 
				"Please connect to internet.", Toast.LENGTH_SHORT);
		
		confirm = (Button)this.findViewById(R.id.button1);
		
		recapture = (Button)this.findViewById(R.id.button2);
		
		obtainedImage = (ImageView)this.findViewById(R.id.imageView2);
		
		// set the obtainedImage get the map
		if (getIntent().hasExtra("croppedBitmap")) {
			bitmap = (Bitmap)getIntent().getParcelableExtra("croppedBitmap");
		}
				
		if (bitmap != null) {
			obtainedImage.setImageBitmap(bitmap);
		}
		
		
		// set recapture listener
		recapture.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				recapture.setBackground(getResources().getDrawable(R.drawable.backtocamera2));
				Intent intent = new Intent(PreviewActivity.this, SearchActivity.class);
				startActivity(intent);
			}
					
		});
		
		
		confirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (!WebConnectionCheck.hasInternetConnection(PreviewActivity.this))
		    	{
		    		checkToast.show();
		    	}
		    	else
		    	{
		    		confirm.setBackground(getResources().getDrawable(R.drawable.next2));
				
		    		Intent intent = new Intent(PreviewActivity.this, SearchResultActivity.class);
		    		intent.putExtra("croppedBitmap", bitmap);
		    		startActivity(intent);
		    	}
			}
			
		});
		
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
		    	searchButton.setBackground(getResources().getDrawable(R.drawable.search2));
		    }
		});
		
		bookMarkButton = (Button) findViewById(R.id.bookmarkButton);
		bookMarkButton.setOnClickListener(new OnClickListener() {

		    @Override
		    public void onClick(View arg0) {
		    	if (!WebConnectionCheck.hasInternetConnection(PreviewActivity.this))
		    	{
		    		checkToast.show();
		    	}
		    	else
		    	{
		    		if (FoodieClient.getInstance().getClientStatus() == FoodieClient.ONLINE) {
		    			bookMarkButton.setBackground(getResources().getDrawable(R.drawable.bookmark2));
		    			onGoToBookmark(arg0);
		    		} else {
		    			loginToast.show(); 
		    		}
		    	}
		    }
		});
		
	}
	
	public void onGoToBookmark (View view) {
		Intent intent = new Intent(view.getContext(), BookmarkActivity.class);
		this.startActivity(intent);
	}
	
	public void onGoToHome (View view) {
		Intent intent = new Intent(view.getContext(), MainActivity.class);
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
