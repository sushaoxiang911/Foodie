package edu.umich.eecs441.foodie.ui;

import java.io.IOException;
import edu.umich.eecs441.foodie.R;
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
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class SearchActivity extends Activity {

	private Button backHomeButton;
	
	public static final int MEDIA_TYPE_IMAGE = 1;
	public static final int MEDIA_TYPE_VIDEO = 2;
	
	private Button captureButton;
	
	private Camera mCamera;
	private CameraPreview mPreview;
	//private ImageView resultPreview;
	private int xLeft,
				yTop,
				xWidth,
				yHeight;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		
		// Create an instance ofCamera
		mCamera = getCameraInstance();
		
		mPreview = new CameraPreview(this, mCamera);		
	   
		FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
		preview.addView(mPreview);
		captureButton = (Button) findViewById(R.id.button_capture);
		captureButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				captureButton.setBackground(getResources().getDrawable(R.drawable.bigcamerab));
				AutoFocusCallback myAutoFocusCallback = new AutoFocusCallback(){
					  @Override
					  public void onAutoFocus(boolean arg0, Camera arg1) {
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
	}
	
	public void onGoToHome (View view) {
		Intent intent = new Intent(view.getContext(), MainActivity.class);
		this.startActivity(intent);
	}
	public static Camera getCameraInstance() {
		Camera c = null;
		try {
			c = Camera.open(); 
		} catch (Exception e) {
		}
		return c;  
	}
	
	public class CameraPreview extends SurfaceView implements
		SurfaceHolder.Callback {
		private SurfaceHolder mHolder;
		private Camera mCamera;
		
		public CameraPreview(Context context, Camera camera) {
			super(context);
			mCamera = camera;
			mHolder = getHolder();
			mHolder.addCallback(this);
		}
	
		public void surfaceCreated(SurfaceHolder holder) {
			try {
				mCamera.setPreviewDisplay(holder);
				mCamera.setDisplayOrientation(90);
				mCamera.startPreview();
			} catch (IOException e) {
			}
		}
	
		public void surfaceDestroyed(SurfaceHolder holder) {
		}
		
		public void surfaceChanged(SurfaceHolder holder, int format, int w,
		int h) {
			if (mHolder.getSurface() == null) {
				return;
			}
		
			try {
				mCamera.stopPreview();
			} catch (Exception e) {
			}
			try {
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
				ImageView viewT = (ImageView) findViewById(R.id.box_preview);
				LinearLayout viewTParent = (LinearLayout)viewT.getParent();
				FrameLayout cameraT = (FrameLayout) findViewById(R.id.camera_preview);
				double ratioX = (double)bitmapPicture.getWidth() / cameraT.getWidth();
				double ratioY = (double)bitmapPicture.getHeight() / cameraT.getHeight();
				xLeft = (int)(((LinearLayout)viewTParent.getParent()).getLeft() * ratioX);
				yTop = (int)(viewTParent.getTop() * ratioY);
				xWidth = (int)(viewT.getWidth() * ratioX);
				yHeight = (int)(viewT.getHeight() * ratioY);
				Bitmap croppedBitmap = 
					Bitmap.createBitmap(bitmapPicture, xLeft, yTop, xWidth, yHeight);
				
				bitmapPicture.recycle();
				Intent intent = new Intent(SearchActivity.this, PreviewActivity.class);
				intent.putExtra("croppedBitmap", croppedBitmap);			
				startActivity(intent);
				
			}	
		};
	
	@Override
	protected void onStop() {
		super.onStop(); // Always call the superclass method first
	}
	
	
	
	@Override
	public void onPause() {
		super.onPause(); 
		if (mCamera != null) {
			mCamera.release();
			mCamera = null;
		}
		FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
		preview.removeView(mPreview);
	}
	
	@Override
	protected void onResume() {
		super.onResume(); 
		if(mCamera == null)
		{
			mCamera = getCameraInstance();
			mPreview = new CameraPreview(this, mCamera);
			FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
			preview.addView(mPreview);		
		}
	}
	@Override
	public void onBackPressed() {
		
	}

}
