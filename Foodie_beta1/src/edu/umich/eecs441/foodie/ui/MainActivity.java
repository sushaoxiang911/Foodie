package edu.umich.eecs441.foodie.ui;


import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import edu.umich.eecs441.foodie.R;
import edu.umich.eecs441.foodie.database.FoodieClient;
import edu.umich.eecs441.foodie.web.WebConnectionCheck;

public class MainActivity extends Activity {
	
	private Button homeButton;
	private Button searchButton;
	private Button bookMarkButton;
	private Button logoutButton;
	private Button signupButton;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		final Toast loginToast = Toast.makeText(MainActivity.this, 
				"Please log in.", Toast.LENGTH_SHORT);
		final Toast checkToast = Toast.makeText(MainActivity.this, 
				"Please connect to internet.", Toast.LENGTH_SHORT);
		Context context = getApplicationContext();
		String destination = context.getFilesDir().getPath() + File.separator
				+ "tessdata";
		File outputFolder = new File(destination);
		if (!outputFolder.exists()) {
			outputFolder.mkdirs();
			File outputFile = new File(destination + File.separator + "chi_sim.traineddata");
			try {
				InputStream is = context.getAssets().open("tessdata" + 
								File.separator + "chi_sim.traineddata");
				outputFile.createNewFile();
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
		    	if (!WebConnectionCheck.hasInternetConnection(MainActivity.this))
		    	{
		    		checkToast.show();
		    	}
		    	else
		    	{
		    		searchButton.setBackground(getResources().getDrawable(R.drawable.search2));
		    		homeButton.setBackground(getResources().getDrawable(R.drawable.home1));
		    		onGoToSearch(arg0);
		    	}
		    }
		});
		
		bookMarkButton = (Button) findViewById(R.id.bookmarkButton);
		bookMarkButton.setOnClickListener(new OnClickListener() {

		    @Override
		    public void onClick(View arg0) {
		    	if (!WebConnectionCheck.hasInternetConnection(MainActivity.this))
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
		
		logoutButton = (Button) findViewById(R.id.logoutButton);
		if (FoodieClient.getInstance().getClientStatus() == FoodieClient.ONLINE) {
			logoutButton.setOnClickListener(new OnClickListener() {
				
			    @Override
			    public void onClick(View arg0) {
			    	logoutButton.setBackground(getResources().getDrawable(R.drawable.logout2));
			    	getSharedPreferences(Remember.PREFS_NAME, MODE_PRIVATE).edit().putBoolean(Remember.IF_PREF, false).commit();	
			    	onGoToHome(arg0);
			    	FoodieClient.getInstance().clientLogout();
			    }
			});
		} else {
			logoutButton.setVisibility(Button.INVISIBLE);
		}
		
		signupButton = (Button) findViewById(R.id.signupButtonH);
		if (FoodieClient.getInstance().getClientStatus() == FoodieClient.ONLINE) {
			signupButton.setVisibility(Button.INVISIBLE);
		} else {
			signupButton.setOnClickListener(new OnClickListener() {

			    @Override
			    public void onClick(View arg0) {
			    	signupButton.setBackground(getResources().getDrawable(R.drawable.homesignup2));
			    	onGoToHome (arg0);
			    }
			});
		}
			
	}
	
	public void onGoToSearch (View view) {
		Intent intent = new Intent(view.getContext(), SearchActivity.class);
		this.startActivity(intent);
	}
	
	public void onGoToBookmark (View view) {
		Intent intent = new Intent(view.getContext(), BookmarkActivity.class);
		this.startActivity(intent);
	}
	
	public void onGoToHome (View view) {
		Intent intent = new Intent(view.getContext(), LoginActivity.class);
		this.startActivity(intent);
	}
	
	@Override
	public void onBackPressed() {
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
