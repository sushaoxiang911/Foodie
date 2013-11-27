package edu.umich.eecs441.foodie.web;

import java.io.IOException;

import edu.umich.eecs441.foodie.database.FoodieClient;
import edu.umich.eecs441.foodie.database.MealEntry;
import edu.umich.eecs441.foodie.picture.PictureScanning;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

/**
 * this class is for the async access
 * @author Shaoxiang Su
 *
 */
public class AsyncOperation extends AsyncTask <Bitmap, Void, MealEntry>{

	private static String TAG = "AsyncOperation.";
	
	private String tessdataPath;
	private ContentSettable contentSettableActivity;
	
	private Bitmap mealPic = null;
	
	private boolean existed;
	
	/**
	 * Constructor to build an AsyncOperation
	 * @param path String, the path of the tessdata for OCR
	 * @param activity ContentSettable, set content and control dialog
	 */
	public AsyncOperation (String path, ContentSettable activity) {
		tessdataPath = path;
		contentSettableActivity = activity;
	}
	// progress dialog doesnot block main thread, move to the main
	protected void onPreExecute() {
		Log.i(TAG + "onPreExecute", "enter!");
		contentSettableActivity.startProgressDialog();
	}
	
	
	@Override
	protected MealEntry doInBackground(Bitmap... arg0) {
		
		// get the recMealName
		PictureScanning ps = new PictureScanning (tessdataPath);
		String recMealName = ps.scanPicture(arg0[0]);
		
		// receive picture
		String picUrl = "";
		String picMealName = "";
		ReceivePicture rp = new ReceivePicture(recMealName);
		try {
			mealPic = rp.getMealPicture();
			picUrl = rp.getPicUrl();
			Log.i(TAG + "doInBackground", "picUrl = " + picUrl);
			picMealName = rp.getPicMealName();
			Log.i(TAG + "doInBackground", "picMealName = " + picMealName);
		} catch (Exception e) {
			Log.i(TAG + "doInBackground", "getMealPicture error");
			e.printStackTrace();
			return null;
		}
		// if mealPic is null, meaning there is no result
		if (mealPic == null) {
			Log.i(TAG + "doInBackground", "cannot get picture");
			return null;
		}
		
		String mealTranslation;
		// receive translation
		ReceiveTranslation rt = new ReceiveTranslation(picMealName);
		try {
			mealTranslation = rt.Translate();
		} catch (Exception e) {
			Log.i(TAG + "doInBackground", "translate failed");
			mealTranslation = "Oops! Foodie could not find the food information.";
			e.printStackTrace();
		}
		
		
		MealEntry result = new MealEntry(recMealName, picMealName, picUrl, mealTranslation);
		
		// if online, check existance
		if (FoodieClient.getInstance().getClientStatus() == FoodieClient.ONLINE){
			
			// check if the meal existed
			try {
				existed = result.existedMeal();
				Log.i(TAG + "onPostExecute", "existed = " + existed);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}		
		return result;
			
	
	}
	
	protected void onPostExecute(MealEntry result) {
		Log.i(TAG + "onPostExecute", "enter!");
		
		// if offline, directly set invisible
		if (FoodieClient.getInstance().getClientStatus() == FoodieClient.OFFLINE){
			if (result == null) {
				contentSettableActivity.setImageView(null);
				contentSettableActivity.setText(new String ("Oops! Foodie could not find the food information."));
			} else {
				contentSettableActivity.setImageView(mealPic);
				contentSettableActivity.setText(result.getMealTranslation());
			}
		} else {
			// if the new MealEntry is empty, set not found
			if (result == null) {
				// set error check
				contentSettableActivity.setImageView(null);
				contentSettableActivity.setText(new String ("Oops! Foodie could not find the food information."));
				contentSettableActivity.setButtonInvisible();
			} else {
				contentSettableActivity.setImageView(mealPic);
				contentSettableActivity.setText(result.getMealTranslation());
				contentSettableActivity.setButtonText(existed);
				contentSettableActivity.setMealEntry(result);
			}
		}
		contentSettableActivity.dismissProgressDialog();
	}

	
}


