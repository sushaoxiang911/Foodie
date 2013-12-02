package edu.umich.eecs441.foodie.web;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class WebConnectionCheck {
	public static boolean hasInternetConnection(Context context) {
	    ConnectivityManager cm =
	    		(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo netInfo = cm.getActiveNetworkInfo();
	    if (netInfo != null && netInfo.isConnected()) {    	
	    	return true;
	    } 
	    return false;
	}
}
