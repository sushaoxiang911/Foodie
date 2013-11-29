package edu.umich.eecs441.foodie.ui;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.example.foodie.R;

import edu.umich.eecs441.foodie.database.FoodieClient;
import edu.umich.eecs441.foodie.database.MealEntry;
import edu.umich.eecs441.foodie.picture.PictureScanning;
import edu.umich.eecs441.foodie.web.AsyncOperation;
import edu.umich.eecs441.foodie.web.ContentSettable;
import edu.umich.eecs441.foodie.web.ReceivePicture;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
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
	
	
	
	// -----
	private static String TAG = "RecognizeActivity.";
	
	
	private ImageView obtainedImage;
	
	//	final private int CAMERA = 1;
	//	final private int CROP = 2;
	private ProgressDialog dialog;
	private ImageView image;
	private TextView text;
	private Bitmap bitmap = null;

	// mark or cancel mark button
	private Button button;
	// call back to get the newMealEntry
	MealEntry newMealEntry = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_result);
		
		final Toast loginToast = Toast.makeText(SearchResultActivity.this, 
				"Please log in.", Toast.LENGTH_SHORT);
		
		Log.i(TAG + "onCreate", "Client status = " + FoodieClient.getInstance().getClientStatus());
		
		// -----
		image = (ImageView) this.findViewById(R.id.imageView1);
		
		
		//recapture = (Button)this.findViewById(R.id.button2);
		
		text = (TextView)this.findViewById(R.id.textView2);
		
		obtainedImage = (ImageView)this.findViewById(R.id.imageView2);
		
		// the bookmark button!
		button = (Button)this.findViewById(R.id.button1);
		
		
		// set the obtainedImage get the map
		if (getIntent().hasExtra("croppedBitmap")) {
			Log.i(TAG + "onCreate", "the bitmap is obtained");
			bitmap = (Bitmap)getIntent().getParcelableExtra("croppedBitmap");
		}
		
		if (bitmap != null) {
			Log.i(TAG + "onCreate", "bitmap is not null");
			obtainedImage.setImageBitmap(bitmap);
		}
		
		
		// set recapture listener
		/*
		recapture.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(SearchResultActivity.this, SearchActivity.class);
				startActivity(intent);
			}
			
		});*/
		
//		startProgressDialog();
		
		AsyncOperation ao = new AsyncOperation (getApplicationContext().getFilesDir().getPath(),
				SearchResultActivity.this);
		
		ao.execute(bitmap);
		
		// if logging in set listener
		if (FoodieClient.getInstance().getClientStatus() == FoodieClient.ONLINE) {
			// set button listener
			// if existed, button onClick for cancel mark
			// if not existed, button onClick for mark
			button.setOnClickListener(new OnClickListener () {
				@Override
				public void onClick(View arg0) {
					// mealEntry is not null
					if (newMealEntry != null) {
						// check if the mealEntry is existed
						// here I use button text, actually, you can call back and set a guard to denote that
						if (button.getText().toString().equals("existed")) {
							Log.i(TAG + "mark onClick", "mealEntry existed");
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
							
							Log.i(TAG + "mark onClick", "mealEntry not existed");
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
			});
		
		} else {
			setButtonInvisible();
		}
		
		
		
/*		try {
			newMealEntry = ao.get();
		} catch (InterruptedException e) {
			Log.i(TAG + "onCreate", "after get(), InterruptedException");
			e.printStackTrace();
		} catch (ExecutionException e) {
			Log.i(TAG + "onCreate", "after get(), ExecutionException");
			e.printStackTrace();
		}
		if (newMealEntry != null) {
			Log.i(TAG + "newMealEntry", "mealName = " + newMealEntry.getPicMealName());
		}*/
		
		
//		PictureScanning ps = new PictureScanning (getApplicationContext().getFilesDir().getPath(),
//					SearchResultActivity.this);
//		ps.execute(bitmap);
		// -----
		
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
		    	searchButton.setBackground(getResources().getDrawable(R.drawable.search2));
		    	onGoToSearch (arg0);
		    }
		});
		
		bookMarkButton = (Button) findViewById(R.id.bookmarkButton);
		bookMarkButton.setOnClickListener(new OnClickListener() {

		    @Override
		    public void onClick(View arg0) {
		    	if (FoodieClient.getInstance().getClientStatus() == FoodieClient.ONLINE) {
		    		bookMarkButton.setBackground(getResources().getDrawable(R.drawable.bookmark2));
		    		onGoToBookmark(arg0);
		    	} else {
		    		loginToast.show();
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
			Log.i(TAG + "setImageView", "bitmap is null");
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
		if (existed) {
			button.setText("existed");
		} else {
			button.setText("Not existed");
		}
	}

	@Override
	public void setButtonInvisible() {
		button.setVisibility(Button.INVISIBLE);
		
	}
	
	public void setMealEntry(MealEntry mealEntry) {
		newMealEntry = mealEntry;
	}
	
	// -----
}
