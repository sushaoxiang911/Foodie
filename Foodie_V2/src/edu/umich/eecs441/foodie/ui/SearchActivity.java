package edu.umich.eecs441.foodie.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;





import com.example.foodie.R;

import edu.umich.eecs441.foodie.database.FoodieClient;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class SearchActivity extends Activity {
	/*
	private Button homeButton;
	private Button searchButton;
	private Button bookMarkButton;
	 */
	private Button backHomeButton;
	
	// ----
	public static final int MEDIA_TYPE_IMAGE = 1;
	public static final int MEDIA_TYPE_VIDEO = 2;
	protected static final String TAG = null;
	
	private Button captureButton;
	
	private Camera mCamera;
	private CameraPreview mPreview;
	//private ImageView resultPreview;
	private int xLeft,
				yTop,
				xWidth,
				yHeight;
	// ----
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		
		// -------
		// Create an instance ofCamera
		mCamera = getCameraInstance();
		
		// Create our Preview view and set it as the content of our activity.
		mPreview = new CameraPreview(this, mCamera);		
	   
		FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
		preview.addView(mPreview);
		captureButton = (Button) findViewById(R.id.button_capture);
		captureButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				captureButton.setBackground(getResources().getDrawable(R.drawable.bigcamerab));
				Log.i("cameratest", "onTake");
				AutoFocusCallback myAutoFocusCallback = new AutoFocusCallback(){
					  @Override
					  public void onAutoFocus(boolean arg0, Camera arg1) {
						  // TODO Auto-generated method stub
						  Log.i("cameratest", "onFocus");
						  mCamera.takePicture(null, null, mPicture);
					  }
				};
				mCamera.autoFocus(myAutoFocusCallback);
			}
		});
		
		backHomeButton = (Button) findViewById(R.id.button_back);
		backHomeButton.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {
		    	backHomeButton.setBackground(getResources().getDrawable(R.drawable.backhomeb));
		    	onGoToHome(arg0);
		    }
		});
		
		//resultPreview = (ImageView) findViewById(R.id.result_preview);
		 
		 // ------
		/*
		homeButton = (Button) findViewById(R.id.homeButton);
		homeButton.setOnClickListener(new OnClickListener() {

		    @Override
		    public void onClick(View arg0) {
		    	homeButton.setBackground(getResources().getDrawable(R.drawable.home2));
		    	onGoToHome(arg0);
		    }
		});
		
		searchButton = (Button) findViewById(R.id.searchButton);
		searchButton.setOnClickListener(new OnClickListener() {

		    @Override
		    public void onClick(View arg0) {
		    	searchButton.setBackground(getResources().getDrawable(R.drawable.search2));
		    }
		});
		
		bookMarkButton = (Button) findViewById(R.id.bookmarkButton);
		bookMarkButton.setOnClickListener(new OnClickListener() {

		    @Override
		    public void onClick(View arg0) {
		    	bookMarkButton.setBackground(getResources().getDrawable(R.drawable.bookmark2));
		    	onGoToBookmark(arg0);
		    }
		});*/
	}
	/*
	public void onGoToBookmark (View view) {
		Intent intent = new Intent(view.getContext(), BookmarkActivity.class);
		this.startActivity(intent);
	}*/
	
	public void onGoToHome (View view) {
		Intent intent = new Intent(view.getContext(), MainActivity.class);
		this.startActivity(intent);
	}
	//---------------
	/* A safe way to get an instance of theCamera object. */
	public static Camera getCameraInstance() {
		Camera c = null;
		try {
			c = Camera.open(); 
		} catch (Exception e) {
			// Camera is not available (in use or does not exist)
		}
		return c;  
	}
	
	
	
	/* A basic Camera preview class */
	public class CameraPreview extends SurfaceView implements
		SurfaceHolder.Callback {
		private SurfaceHolder mHolder;
		private Camera mCamera;
		
		public CameraPreview(Context context, Camera camera) {
			super(context);
			mCamera = camera;
			
			// Install aSurfaceHolder.Callback so we get notified when the
			// underlying surface is created and destroyed.
			mHolder = getHolder();
			mHolder.addCallback(this);
		}
	
		public void surfaceCreated(SurfaceHolder holder) {
			// The Surface has been created, now tell the camera where to draw
			// the preview.
			try {
				mCamera.setPreviewDisplay(holder);
				mCamera.setDisplayOrientation(90);
				mCamera.startPreview();
			} catch (IOException e) {
				Log.d(TAG, "Errorsetting camera preview: " + e.getMessage());
			}
		}
	
		public void surfaceDestroyed(SurfaceHolder holder) {
			//Take care of releasing the Camera preview
		}
		
		public void surfaceChanged(SurfaceHolder holder, int format, int w,
		int h) {
			// If your preview can change or rotate, take care of those events
			// here.
			// Make sure to stop the preview before resizing or reformatting it.
		
			if (mHolder.getSurface() == null) {
				// preview surface does not exist
				return;
			}
		
			// stop preview before making changes
			try {
				mCamera.stopPreview();
			} catch (Exception e) {
				// ignore: tried to stop a non-existent preview
			}
		
			// set preview size and make any resize, rotate or
			// reformatting changes here
		
			// start preview with new settings
			try {
				// set parameters
				mCamera.setPreviewDisplay(mHolder);
				mCamera.setDisplayOrientation(90);
				Camera.Parameters para = mCamera.getParameters();
				para.setAntibanding(Camera.Parameters.ANTIBANDING_AUTO);
		        para.setWhiteBalance(Camera.Parameters.WHITE_BALANCE_AUTO);
		        para.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
		        para.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
		        para.setColorEffect(Camera.Parameters.EFFECT_NONE);
		        mCamera.setParameters(para);
				mCamera.startPreview();
			} catch (Exception e) {
				Log.d(TAG, "Errorstarting camera preview: " + e.getMessage());
			}
		}
	}
		
		private PictureCallback mPicture = new PictureCallback() {
		
			@Override
			public void onPictureTaken(byte[] data, Camera camera) {
		
				Bitmap raw = 
					BitmapFactory.decodeByteArray(data, 0, data.length); 
				
				// Setting post rotate to 90
                Matrix mtx = new Matrix();
                mtx.postRotate(90);
                Bitmap bitmapPicture = 
                	Bitmap.createBitmap(raw, 0, 0, raw.getWidth(), raw.getHeight(), mtx, true);
                
                raw.recycle();
                
                Log.i("cameratest", "rawWidth" + bitmapPicture.getWidth());
				Log.i("cameratest", "rawHeight" + bitmapPicture.getHeight());
                
				ImageView viewT = (ImageView) findViewById(R.id.box_preview);
				LinearLayout viewTParent = (LinearLayout)viewT.getParent();
				FrameLayout cameraT = (FrameLayout) findViewById(R.id.camera_preview);
				double ratioX = (double)bitmapPicture.getWidth() / cameraT.getWidth();
				double ratioY = (double)bitmapPicture.getHeight() / cameraT.getHeight();
				
				Log.i("cameratest", "xOriginal" + cameraT.getWidth());
				Log.i("cameratest", "yOriginal" + cameraT.getHeight());
				Log.i("cameratest", "xRatio" + ratioX);
				Log.i("cameratest", "yRatio" + ratioY);
				
				xLeft = (int)(((LinearLayout)viewTParent.getParent()).getLeft() * ratioX);
				yTop = (int)(viewTParent.getTop() * ratioY);
				xWidth = (int)(viewT.getWidth() * ratioX);
				yHeight = (int)(viewT.getHeight() * ratioY);
				
				Log.i("cameratest", "xleft" + xLeft);
				Log.i("cameratest", "yTop" + yTop);
				Log.i("cameratest", "xWidth" + xWidth);
				Log.i("cameratest", "yHeight" + yHeight);
				Bitmap croppedBitmap = 
					Bitmap.createBitmap(bitmapPicture, xLeft, yTop, xWidth, yHeight);
				
				bitmapPicture.recycle();
				//visualize image DELETED LATER
				//resultPreview.setImageBitmap(croppedBitmap);
				
				Log.i("onPictureTaken", "Ready to new intent");
				Intent intent = new Intent(SearchActivity.this, PreviewActivity.class);
				intent.putExtra("croppedBitmap", croppedBitmap);			
				Log.i("fda", "fda");
				
				startActivity(intent);
				
			}	
		};
	
	@Override
	protected void onStop() {
		Log.i("cameratest", "onStop");
		super.onStop(); // Always call the superclass method first
	}
	
	
	
	@Override
	public void onPause() {
		Log.i("cameratest", "onPause");
		super.onPause(); // Always call the superclass method first
		// Release the Camera because we don't need it when paused
		// and other activities might need to use it.
		if (mCamera != null) {
			mCamera.release();
			mCamera = null;
		}
	}

	//-------------
	
	// disable the back button
	@Override
	public void onBackPressed() {
		
	}

}
