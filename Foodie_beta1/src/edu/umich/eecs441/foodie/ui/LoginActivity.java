package edu.umich.eecs441.foodie.ui;

import java.io.IOException;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import edu.umich.eecs441.foodie.R;
import edu.umich.eecs441.foodie.database.FoodieClient;
import edu.umich.eecs441.foodie.web.WebConnectionCheck;

public class LoginActivity extends Activity {
	
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
		password.setTypeface(Typeface.DEFAULT);
		//password.setTransformationMethod(new PasswordTransformationMethod());
		login = (Button) this.findViewById(R.id.button1);
		signup = (Button) this.findViewById(R.id.button2);
		tryit = (Button) this.findViewById(R.id.button3);
		
		final Toast checkToast = Toast.makeText(LoginActivity.this, 
				"Please connect to internet.", Toast.LENGTH_SHORT);   
		final Toast inputToast = Toast.makeText(LoginActivity.this, 
				"Username and password can NOT be empty.", Toast.LENGTH_SHORT);
		final Toast signupTipToast = Toast.makeText(LoginActivity.this, 
				"Please fill in both of the blanks above.", 
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
	   
	    // if the password is remembered
		if (getSharedPreferences(Remember.PREFS_NAME, MODE_PRIVATE).getBoolean(Remember.IF_PREF, false)) {
			if (WebConnectionCheck.hasInternetConnection(LoginActivity.this)) {

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
						
						
	    					String usernameString = getSharedPreferences(Remember.PREFS_NAME, MODE_PRIVATE).getString(Remember.PREF_USERNAME, null);
	    					String passwordString = getSharedPreferences(Remember.PREFS_NAME, MODE_PRIVATE).getString(Remember.PREF_PASSWORD, null);	
							
	    					int loginResult = FoodieClient.getInstance().clientLogin(usernameString, passwordString);
						
	    					if (loginResult == -1) {
	    						LoginActivity.this.runOnUiThread(new Runnable() {
	
	    							@Override
	    							public void run() {
	    								LoginActivity.this.dismissProgressDialog();
	    							}
	    						});
	    						getSharedPreferences(Remember.PREFS_NAME, MODE_PRIVATE).edit().putBoolean(Remember.IF_PREF, false);
	    						noUserToast.show();
	    					} else if (loginResult == -2) {
	    						LoginActivity.this.runOnUiThread(new Runnable() {
	
	    							@Override
	    							public void run() {
	    								LoginActivity.this.dismissProgressDialog();
	    							}
	    						});
	    						getSharedPreferences(Remember.PREFS_NAME, MODE_PRIVATE).edit().putBoolean(Remember.IF_PREF, false);
	    						wrongpwToast.show(); 
	    					} else {
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
			}
		}
		
		
		
		tryit.setOnClickListener(new OnClickListener() {

		    @Override
		    public void onClick(View arg0) {
		    	if (!WebConnectionCheck.hasInternetConnection(LoginActivity.this)) {
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
		    	if (!WebConnectionCheck.hasInternetConnection(LoginActivity.this)) {
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
		    								username.setText("");
		    								password.setText("");
		    								LoginActivity.this.dismissProgressDialog();
		    							}
		    						});
		    						noUserToast.show();
		    					} else if (loginResult == -2) {
		    						LoginActivity.this.runOnUiThread(new Runnable() {
		    							@Override
		    							public void run() {
		    								password.setText("");
		    								LoginActivity.this.dismissProgressDialog();
		    							}
		    						});
		    						wrongpwToast.show(); 
		    					} else {
		    						getSharedPreferences(Remember.PREFS_NAME,MODE_PRIVATE)
		    				        	.edit()
		    				        	.putString(Remember.PREF_USERNAME, usernameString)
		    				        	.putString(Remember.PREF_PASSWORD, passwordString)
		    				        	.putBoolean(Remember.IF_PREF, true)
		    				        	.commit();
		    						
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
		    	}
			}
		});
		
		
		
		signup.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
		    	if (!WebConnectionCheck.hasInternetConnection(LoginActivity.this)) {
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
		    					} else {
		    						signupSuccessToast.show();
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
	

}
