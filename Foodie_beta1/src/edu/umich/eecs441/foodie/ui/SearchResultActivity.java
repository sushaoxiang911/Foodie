package edu.umich.eecs441.foodie.ui;

import java.io.IOException;
import com.example.foodie.R;
import edu.umich.eecs441.foodie.database.FoodieClient;
import edu.umich.eecs441.foodie.database.MealEntry;
import edu.umich.eecs441.foodie.web.AsyncOperation;
import edu.umich.eecs441.foodie.web.ContentSettable;
import edu.umich.eecs441.foodie.web.WebConnectionCheck;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class SearchResultActivity extends Activity  implements ContentSettable{
	private Button homeButton;
	private Button searchButton;
	private Button bookMarkButton;
	
	private ImageView obtainedImage;
	
	private ProgressDialog dialog;
	private ImageView image;
	private TextView text;
	private Bitmap bitmap = null;

	private Button button;
	MealEntry newMealEntry = null;
	
	
	// for the button
	private boolean ifExisted = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_result);
		
		final Toast loginToast = Toast.makeText(SearchResultActivity.this, 
				"Please log in.", Toast.LENGTH_SHORT);
		final Toast checkToast = Toast.makeText(SearchResultActivity.this, 
				"Please connect to internet.", Toast.LENGTH_SHORT);
		image = (ImageView) this.findViewById(R.id.imageView1);
		
		text = (TextView)this.findViewById(R.id.textView2);
		
		obtainedImage = (ImageView)this.findViewById(R.id.imageView2);
		
		button = (Button)this.findViewById(R.id.button1);
		
		setButtonInvisible(false);
		
		
		if (getIntent().hasExtra("croppedBitmap")) {
			bitmap = (Bitmap)getIntent().getParcelableExtra("croppedBitmap");
		}
		
		if (bitmap != null) {
			obtainedImage.setImageBitmap(bitmap);
		}
		
		AsyncOperation ao = new AsyncOperation (getApplicationContext().getFilesDir().getPath(),
				SearchResultActivity.this);
		
		ao.execute(bitmap);
		
		if (FoodieClient.getInstance().getClientStatus() == FoodieClient.ONLINE) {
			button.setOnClickListener(new OnClickListener () {
				@Override
				public void onClick(View arg0) {
					if (!WebConnectionCheck.hasInternetConnection(SearchResultActivity.this))
			    	{
			    		checkToast.show();
			    	}
					else
					{
						button.setBackground(getResources().getDrawable(R.drawable.star2));
						if (newMealEntry != null) {
							if (ifExisted) {
								new Thread(new Runnable() {
									@Override
									public void run() {
										SearchResultActivity.this.runOnUiThread(new Runnable() {
											@Override
											public void run() {
												SearchResultActivity.this.dialog = 
														ProgressDialog.show(SearchResultActivity.this, "Waiting...", "Marking");
											}
										});
										
										// mark the meal
										try {
											newMealEntry.deleteMeal();
										} catch (IOException e) {
											e.printStackTrace();
										}
										
										
										SearchResultActivity.this.runOnUiThread(new Runnable() {
											@Override
											public void run() {
												// change button to not existed
												setButtonText(false);
												SearchResultActivity.this.dismissProgressDialog();
											}
										});
									}
								}).start();		
							} else {
								new Thread(new Runnable() {
									@Override
									public void run() {
										SearchResultActivity.this.runOnUiThread(new Runnable() {
											@Override
											public void run() {
												SearchResultActivity.this.dialog = 
														ProgressDialog.show(SearchResultActivity.this, "Waiting...", "Marking");
											}
										});
										
										// cancel mark
										try {
											newMealEntry.addMeal();
										} catch (IOException e) {
											e.printStackTrace();
										}
										
										
										SearchResultActivity.this.runOnUiThread(new Runnable() {
											@Override
											public void run() {
												// change button to existed
												setButtonText(true);
												SearchResultActivity.this.dismissProgressDialog();
											}
										});
									}
								}).start();	
							}
						}
					}
				}
			});
		
		} else {
			setButtonInvisible(false);
		}
				
		homeButton = (Button) findViewById(R.id.homeButton);
		homeButton.setOnClickListener(new OnClickListener() {

		    @Override
		    public void onClick(View arg0) {
		    	homeButton.setBackground(getResources().getDrawable(R.drawable.home2));
		    	if (FoodieClient.getInstance().getClientStatus() == FoodieClient.ONLINE) {
		    		onGoToHome(arg0);
		    	} else {
		    		Intent intent = new Intent(SearchResultActivity.this, MainActivity.class);
		    		startActivity(intent);
		    	}
		    }
		});
		
		searchButton = (Button) findViewById(R.id.searchButton);
		searchButton.setOnClickListener(new OnClickListener() {

		    @Override
		    public void onClick(View arg0) {
		    	if (!WebConnectionCheck.hasInternetConnection(SearchResultActivity.this))
		    	{
		    		checkToast.show();
		    	}
		    	else 
		    	{
		    		searchButton.setBackground(getResources().getDrawable(R.drawable.search2));
		    		onGoToSearch (arg0);
		    	}
		    }
		});
		
		bookMarkButton = (Button) findViewById(R.id.bookmarkButton);
		bookMarkButton.setOnClickListener(new OnClickListener() {

		    @Override
		    public void onClick(View arg0) {
		    	if (!WebConnectionCheck.hasInternetConnection(SearchResultActivity.this))
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

	
	public void onGoToSearch (View view) {
		Intent intent = new Intent(view.getContext(), SearchActivity.class);
		this.startActivity(intent);
	}
	
	// disable the back button
	@Override
	public void onBackPressed() {
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	// -----
	@Override
	public void startProgressDialog() {
		dialog = ProgressDialog.show(SearchResultActivity.this, "Waiting...", "Obtaining Image");
	}


	@Override
	public void dismissProgressDialog() {
		dialog.dismiss();
	}


	@Override
	public void setImageView(Bitmap bm) {
		if (bm == null) {
			image.setImageResource(R.drawable.error);
			
		} else {
			image.setImageBitmap(bm);
		}
	}

	@Override
	public void setText(String string) {
		text.setText(string);
	}

	// the callback of control button
	public void setButtonText (boolean existed) {
		ifExisted = existed;
		if (existed) {
			button.setBackground(getResources().getDrawable(R.drawable.star2));
		} else {
			button.setBackground(getResources().getDrawable(R.drawable.star1));
		}
	}

	@Override
	public void setButtonInvisible(boolean set) {
		if (!set) {
			button.setVisibility(Button.INVISIBLE);
		} else {
			button.setVisibility(Button.VISIBLE);
		}
	}
	
	public void setMealEntry(MealEntry mealEntry) {
		newMealEntry = mealEntry;
	}
}
