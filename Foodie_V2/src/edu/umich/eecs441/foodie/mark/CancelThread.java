package edu.umich.eecs441.foodie.mark;

import java.io.IOException;

import android.util.Log;
import edu.umich.eecs441.foodie.database.MealEntry;

/**
 * This class is to delete a specific entry, put it inside the onClickListener
 * as "new CancelThread(theMealEntry).start;"
 * @author Shaoxiang Su
 *
 */
public class CancelThread implements Runnable {
	
	private MealEntry mealEntry;
	
	protected CancelThread(MealEntry mealEntry) {
		this.mealEntry = mealEntry;
	}
	@Override
	public void run() {
		try {
			mealEntry.deleteMeal();
		} catch (IOException e) {
			Log.i("CancelThread", "cancel failed");
			e.printStackTrace();
		}
	}
	
}
