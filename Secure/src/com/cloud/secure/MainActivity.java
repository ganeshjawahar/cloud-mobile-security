package com.cloud.secure;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONObject;
import org.json.simple.parser.JSONParser;

import android.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.widget.FrameLayout;

public class MainActivity extends Activity {
	private Camera cam;
	private CameraPreview camPrev;

	private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
			cam = getCameraInstance();
			camPrev = new CameraPreview(this, cam);
			FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
			preview.addView(camPrev);

			Date startTime = new Date();
			Date endTime = new Date();
			endTime.setTime(startTime.getTime() + 1000 * 60 * 40);

			String input = "{\"userId\":\"" + "demo" + "\",\"startTime\":\""
					+ startTime + "\",\"endTime\":\"" + endTime
					+ "\",\"location\":\"" + "east-iiith" + "\"}";
			try {
				String sessionInfo = postCall(
						"http://ec2-54-201-111-236.us-west-2.compute.amazonaws.com:8080/RestService/post/createSessionInfo",
						input);
				JSONParser parser = new JSONParser();
				JSONObject obj = (JSONObject) parser.parse(sessionInfo);
				sessionid = (String) obj.get("sessionId");

				new AlertDialog.Builder(this).setTitle(sessionid)
						.setMessage(sessionid).show();

			} catch (Exception e) {
				e.printStackTrace();

				new AlertDialog.Builder(this).setTitle(e.getMessage())
						.setMessage(e.getMessage()).show();
			}

			TimerTask task = new TakePicTask();
			Timer timer = new Timer();
			timer.schedule(task, 3000, 120000);
		}
	}

	static String user = "demo";
	static String sessionid = null;

	class TakePicTask extends TimerTask {
		@SuppressWarnings("deprecation")
		@Override
		public void run() {
			cam.takePicture(null, null, jpgCallBack);
		}
	}

	private Camera getCameraInstance() {
		Camera camera = null;
		try {
			camera = Camera.open();
			camera.setDisplayOrientation(90);
		} catch (Exception e) {
			Log.e("MainActivity",
					"getCameraInstance() failed due to " + e.getMessage());
		}
		return camera;
	}

	public static String postCall(String urlStr, String input) throws Exception {
		/*
		 * Utility to make a post call to the server.
		 */
		URL url = new URL(urlStr);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setDoOutput(true);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "application/json");
		OutputStream os = conn.getOutputStream();
		os.write(input.getBytes());
		os.flush();

		BufferedReader br = new BufferedReader(new InputStreamReader(
				(conn.getInputStream())));

		String output;
		// pw.println("Output from Server .... \n");
		StringBuffer buff = new StringBuffer();
		while ((output = br.readLine()) != null) {
			buff.append(output);
			// pw.println(output);
		}

		conn.disconnect();
		return buff.toString();
	}

	PictureCallback jpgCallBack = new PictureCallback() {
		@SuppressWarnings("deprecation")
		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			final String base64Data = Base64.encodeToString(data, 0);
			try {
				postCall(
						"http://ec2-54-201-111-236.us-west-2.compute.amazonaws.com:8080/RestService/post/imageInfo",
						"{\"userId\":\"" + user + "\",\"sessionId\":\""
								+ sessionid + "\",\"snapedAt\":\"" + new Date()
								+ "\",\"data\":\"" + base64Data + "\"}");
			} catch (Exception e) {
				e.printStackTrace();
			}
			camera.startPreview();
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
