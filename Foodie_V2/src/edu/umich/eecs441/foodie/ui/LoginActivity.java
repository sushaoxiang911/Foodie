package edu.umich.eecs441.foodie.ui;

import java.io.IOException;

import com.example.foodie.R;

import edu.umich.eecs441.foodie.database.FoodieClient;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity {
	
	private static String TAG = "LoginActivity.";
	
	private EditText username;
	private EditText password;
	private Button login;
	private Button signup;
	private Button tryit;
	
	private ProgressDialog dialog;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		

		setContentView (R.layout.activity_login);
		username = (EditText) this.findViewById(R.id.username);
		password = (EditText) this.findViewById(R.id.password);
		login = (Button) this.findViewById(R.id.button1);
		signup = (Button) this.findViewById(R.id.button2);
		tryit = (Button) this.findViewById(R.id.button3);
		final Toast checkToast = Toast.makeText(LoginActivity.this, 
				"Please connect to internet.", Toast.LENGTH_SHORT);   
		final Toast inputToast = Toast.makeText(LoginActivity.this, 
				"Username and password can NOT be empty.", Toast.LENGTH_SHORT);
		final Toast signupTipToast = Toast.makeText(LoginActivity.this, 
				"Please fill in the blanks above.", 
				Toast.LENGTH_SHORT);
		
		final Toast wrongpwToast = Toast.makeText(LoginActivity.this, 
				"Wrong password.", 
				Toast.LENGTH_SHORT);
		final Toast ueserExistedToast = Toast.makeText(LoginActivity.this, 
				"The user name has been used.\n Try a new user name.", 
				Toast.LENGTH_SHORT);
		final Toast signupSuccessToast = Toast.makeText(LoginActivity.this, 
				"Sign up successfully.\n Please log in.", 
				Toast.LENGTH_SHORT);
		final Toast noUserToast = Toast.makeText(LoginActivity.this, 
				"The username does not exist.\n", 
				Toast.LENGTH_SHORT);
	    	
	    	
		tryit.setOnClickListener(new OnClickListener() {

		    @Override
		    public void onClick(View arg0) {
		    	if (!hasInternetConnection()) {
		    		checkToast.show();
		    	}
		    	else {
		    		tryit.setBackground(getResources().getDrawable(R.drawable.try2));
		    		onGoToHomeGuest(arg0);
		    	}
		    }
		});
		
		login.setOnClickListener(new OnClickListener () {

			@Override
			public void onClick(View arg0) {
		    	if (!hasInternetConnection()) {
		    		checkToast.show();
		    	}
		    	else if (username.getText().toString().length()==0
		    			|| password.getText().toString().length()==0) {
		    		inputToast.show();
		    	}		    	
		    	else {
		    		login.setBackground(getResources().getDrawable(R.drawable.login2));
				
		    		new Thread(new Runnable() {
		    			@Override
		    			public void run() {
		    				try {
		    					LoginActivity.this.runOnUiThread(new Runnable() {

		    						@Override
		    						public void run() {
		    							LoginActivity.this.startLoginDialog();
		    						}
		    					});
							
							
		    					String usernameString = username.getText().toString();
		    					String passwordString = password.getText().toString();	
								
		    					int loginResult = FoodieClient.getInstance().clientLogin(usernameString, passwordString);
							
		    					if (loginResult == -1) {
		    						LoginActivity.this.runOnUiThread(new Runnable() {

		    							@Override
		    							public void run() {
		    								LoginActivity.this.dismissProgressDialog();
		    							}
		    						});
		    						noUserToast.show();
		    						Log.i(TAG + "login click", "does not exist");
		    					} else if (loginResult == -2) {
		    						LoginActivity.this.runOnUiThread(new Runnable() {

		    							@Override
		    							public void run() {
		    								LoginActivity.this.dismissProgressDialog();
		    							}
		    						});
		    						wrongpwToast.show(); 
		    						Log.i(TAG + "login click", "wrong password");
		    					} else {
		    						Log.i(TAG + "login click", "right");
		    						LoginActivity.this.runOnUiThread(new Runnable() {

		    							@Override
		    							public void run() {
										
		    								Intent intent = new Intent (LoginActivity.this, MainActivity.class);
		    								LoginActivity.this.startActivity(intent);
		    								LoginActivity.this.dismissProgressDialog();
		    							}
		    						});

		    					}
						
		    				} catch (IOException e) {
		    					e.printStackTrace();
		    				} 
						
		    			}
					
		    		}).start();		
		    		
		    		login.setBackground(getResources().getDrawable(R.drawable.login1));
				
		    		/*ExecutorService es = Executors.newSingleThreadExecutor();
					Future<Integer> result = es.submit(new Callable<Integer> () {

						@Override
						public Integer call() throws Exception {
							LoginActivity.this.startLoginDialog();
							String usernameString = username.getText().toString();
							String passwordString = password.getText().toString();	
							
							int loginResult = FoodieClient.getInstance().clientLogin(usernameString, passwordString);
						
							if (loginResult == -1) {
								Log.i(TAG + "login click", "does not exist");
							} else if (loginResult == -2) {
								Log.i(TAG + "login click", "wrong password");
							} else {
								Log.i(TAG + "login click", "right");
							}
							LoginActivity.this.dismissProgressDialog();
							return loginResult;
						}
					});
					int x = -3;
					
					try {
						x = result.get();
					} catch (Exception e) {
					}
					
					if (x == -1) {}
					else if (x == -2) {}
					else if (x == -3) {}
					else {
						Intent intent = new Intent (LoginActivity.this, MainActivity.class);
						LoginActivity.this.startActivity(intent);
					}*/
		    	}
			}
		});
		
		
		
		signup.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
		    	if (!hasInternetConnection()) {
		    		checkToast.show();
		    	}
		    	else if (username.getText().toString().length()==0
		    			|| password.getText().toString().length()==0) {
		    		signupTipToast.show();
		    	}
		    	else {
		    		signup.setBackground(getResources().getDrawable(R.drawable.signup2));
				
		    		new Thread(new Runnable(){

		    			@Override
		    			public void run() {
						
		    				String usernameString = username.getText().toString();
		    				String passwordString = password.getText().toString();
						
					
		    				try {
		    					// the progress dialog
		    					LoginActivity.this.runOnUiThread(new Runnable() {
		    						@Override
		    						public void run() {
		    							LoginActivity.this.startSignupDialog();
									
		    							username.setText("", TextView.BufferType.EDITABLE);
		    							password.setText("", TextView.BufferType.EDITABLE);
									
		    						}
		    					});
							
		    					// sign up
		    					int result = FoodieClient.signUp(usernameString, passwordString);
							
		    					//dismiss progress dialog
		    					LoginActivity.this.runOnUiThread(new Runnable() {

		    						@Override
		    						public void run() {
		    							LoginActivity.this.dismissProgressDialog();
		    						}
		    					});
							
		    					if (result == 0) {
		    						ueserExistedToast.show();
		    						Log.i(TAG + "sign up click", "user existed");
		    					} else {
		    						signupSuccessToast.show();
		    						Log.i(TAG + "sign up click", "success");
		    					}
							
		    				} catch (Exception e) {
		    					e.printStackTrace();
		    				}
						
		    			}
					
		    		}).start();
		    		
		    		signup.setBackground(getResources().getDrawable(R.drawable.signup1));
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


	public void startLoginDialog() {
		dialog = ProgressDialog.show(LoginActivity.this, "Waiting...", "Logging in");
	}
	
	public void startSignupDialog() {
		dialog = ProgressDialog.show(LoginActivity.this, "Waiting...", "Signing up");
	}
	
	public void dismissProgressDialog() {
		dialog.dismiss();
	}
	
	public void onGoToHomeGuest (View view) {
		Intent intent = new Intent(view.getContext(), MainActivity.class);
		this.startActivity(intent);
	}
	
	public boolean hasInternetConnection() {
	    ConnectivityManager cm =
	    		(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo netInfo = cm.getActiveNetworkInfo();
	    if (netInfo != null && netInfo.isConnected()) {    	
	    	return true;
	    } 
	    return false;
	}

}