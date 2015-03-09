package com.cloud.secure;

import java.io.IOException;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class CameraPreview extends SurfaceView implements
		SurfaceHolder.Callback {
	private SurfaceHolder mSurfaceHolder;
	@SuppressWarnings("deprecation")
	private Camera cam;

	// Constructor that obtains context and camera
	@SuppressWarnings("deprecation")
	public CameraPreview(Context context, Camera camera) {
		super(context);
		this.cam = camera;
		this.mSurfaceHolder = this.getHolder();
		this.mSurfaceHolder.addCallback(this);
		this.mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void surfaceCreated(SurfaceHolder surfaceHolder) {
		try {
			cam.setPreviewDisplay(surfaceHolder);
			cam.startPreview();
		} catch (IOException e) {
			Log.e("CameraPreview",
					"surfaceCreated() failed due to " + e.getMessage());
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
		cam.stopPreview();
		cam.release();
	}

	@SuppressWarnings("deprecation")
	@Override
	public void surfaceChanged(SurfaceHolder surfaceHolder, int format,
			int width, int height) {
		// start preview with new settings
		try {
			cam.setPreviewDisplay(surfaceHolder);
			cam.startPreview();
		} catch (Exception e) {
			Log.e("CameraPreview",
					"surfaceChanged() failed due to " + e.getMessage());
		}
	}
}