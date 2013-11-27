package edu.umich.eecs441.foodie.web;


/**
 * control the prgress dialog for the main thread
 * @author picc
 *
 */
public interface ProgressDialogApplicable {
	// start progress dialog
	public void startProgressDialog ();
	
	// dismiss progress dialog
	public void dismissProgressDialog();
}
