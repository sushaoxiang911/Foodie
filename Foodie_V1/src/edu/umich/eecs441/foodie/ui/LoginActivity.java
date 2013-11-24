package edu.umich.eecs441.foodie.ui;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.example.foodie.R;

import edu.umich.eecs441.foodie.database.FoodieClient;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
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
	
	private ProgressDialog dialog;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView (R.layout.activity_login);
		username = (EditText) this.findViewById(R.id.username);
		password = (EditText) this.findViewById(R.id.password);
		login = (Button) this.findViewById(R.id.button1);
		signup = (Button) this.findViewById(R.id.button2);
		
		login.setOnClickListener(new OnClickListener () {

			@Override
			public void onClick(View arg0) {
				login.setBackground(getResources().getDrawable(R.drawable.login2));
				
				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							LoginActivity.this.runOnUiThread(new Runnable() {

								@Override
								public void run() {
									LoginActivity.this.startProgressDialog();
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
								Log.i(TAG + "login click", "does not exist");
							} else if (loginResult == -2) {
								LoginActivity.this.runOnUiThread(new Runnable() {

									@Override
									public void run() {
										LoginActivity.this.dismissProgressDialog();
									}
								});

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
				
				/*ExecutorService es = Executors.newSingleThreadExecutor();
				Future<Integer> result = es.submit(new Callable<Integer> () {

					@Override
					public Integer call() throws Exception {
						LoginActivity.this.startProgressDialog();
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
		});
		
		
		
		signup.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				signup.setBackground(getResources().getDrawable(R.drawable.signup2));
				
				new Thread(new Runnable(){

					@Override
					public void run() {
						
						String usernameString = username.getText().toString();
						String passwordString = password.getText().toString();
						
						username.setText("", TextView.BufferType.EDITABLE);
						password.setText("", TextView.BufferType.EDITABLE);
						
						try {
							// the progress dialog
							LoginActivity.this.runOnUiThread(new Runnable() {
								@Override
								public void run() {
									LoginActivity.this.startProgressDialog();
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
								 Toast.makeText(getBaseContext(), 
										   "User existed.", 
									          Toast.LENGTH_SHORT).show(); 
								Log.i(TAG + "sign up click", "user existed");
							} else {
								Log.i(TAG + "sign up click", "success");
							}
							
							
							
						} catch (Exception e) {
							e.printStackTrace();
						}
						
					}
					
				}).start();
				
			}
			
		});
		
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}


	public void startProgressDialog() {
		dialog = ProgressDialog.show(LoginActivity.this, "Waiting...", "Logging in");
	}



	public void dismissProgressDialog() {
		dialog.dismiss();
	}

}
