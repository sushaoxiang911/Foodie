package edu.umich.eecs441.foodie.web;

import android.graphics.Bitmap;

public interface ContentSettable extends ProgressDialogApplicable {
	public void setImageView(Bitmap bm);
	
	public void setText (String string);
}
