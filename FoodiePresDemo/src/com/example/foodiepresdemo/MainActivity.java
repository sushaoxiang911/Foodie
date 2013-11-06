package com.example.foodiepresdemo;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
