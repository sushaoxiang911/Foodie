package edu.umich.eecs441.foodie.web;

import edu.umich.eecs441.foodie.database.MealEntry;
import android.graphics.Bitmap;

public interface ContentSettable extends ProgressDialogApplicable {
	public void setImageView(Bitmap bm);
	
	public void setText (String string);
	
	public void setButtonText (boolean existed);
	
	public void setButtonInvisible(boolean set);

	public void setMealEntry(MealEntry mealEntry);
}
