package com.kidgeniushq.susd;

import java.io.File;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.kidgeniushq.susd.utility.MyApplication;
import com.kidgeniushq.susd.utility.Utility;
import com.nostra13.universalimageloader.core.ImageLoader;

public class BigView extends Activity {
	ImageView iv;
	File imageFileFolder, imageFileName;
	MediaScannerConnection msConn;
	FileOutputStream fileOutputStream;
	File file1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_big_view);
		int currentAPIVersion = android.os.Build.VERSION.SDK_INT;
		if (currentAPIVersion >= android.os.Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().hide();
		} 
		iv = (ImageView) findViewById(R.id.bigImageView);
		iv.setImageBitmap(MyApplication.currentBitmap);

	}


	public void savePhoto(Bitmap bmp) {
		imageFileFolder = new File(Environment.getExternalStorageDirectory(),
				"SUSD");
		imageFileFolder.mkdir();
		FileOutputStream out = null;
		Calendar c = Calendar.getInstance();
		String date = fromInt(c.get(Calendar.MONTH))
				+ fromInt(c.get(Calendar.DAY_OF_MONTH))
				+ fromInt(c.get(Calendar.YEAR))
				+ fromInt(c.get(Calendar.HOUR_OF_DAY))
				+ fromInt(c.get(Calendar.MINUTE))
				+ fromInt(c.get(Calendar.SECOND));
		imageFileName = new File(imageFileFolder, date.toString()+ MyApplication.currentStory.getSender()+ ".jpg");
		try {
			out = new FileOutputStream(imageFileName);
			bmp.compress(Bitmap.CompressFormat.JPEG, 100, out);
			out.flush();
			out.close();
			scanPhoto(imageFileName.toString());
			out = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void repost(View v){		
		startActivity(new Intent(this,
				SendImageActivity.class));
	}
	public void tweetItOut(){
		// Create intent using ACTION_VIEW and a normal Twitter url:
		String tweetUrl = 
		    String.format("https://twitter.com/intent/tweet?text=%s&url=%s",
		        urlEncode("SAVE SNAPCHATS ANDROID APP: "), urlEncode("http://bit.ly/1yI9QXe"));
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(tweetUrl));

		// Narrow down to official Twitter app, if available:
		List<ResolveInfo> matches = getPackageManager().queryIntentActivities(intent, 0);
		for (ResolveInfo info : matches) {
		    if (info.activityInfo.packageName.toLowerCase().startsWith("com.twitter")) {
		        intent.setPackage(info.activityInfo.packageName);
		    }
		}
		startActivity(intent);
		}
		public static String urlEncode(String s) {
		    try {
		        return URLEncoder.encode(s, "UTF-8");
		    }
		    catch (UnsupportedEncodingException e) {
		        Log.wtf("URLEncoder", "UTF-8 should always be supported", e);
		        throw new RuntimeException("URLEncoder.encode() failed for " + s);
		    }
		}
private class RepostAsyncTask extends AsyncTask<String, Void, Boolean> {
		
		@Override
		protected Boolean doInBackground(String... params) {
			boolean result = MyApplication.snapchat.sendStory(imageFileName, false, 5, "");
			return result;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if(result){
				Toast.makeText(getApplicationContext(), "Uploaded to your snapchat story!!", Toast.LENGTH_LONG).show();
			}else{
				Toast.makeText(getApplicationContext(), "Error reposting ;/", Toast.LENGTH_LONG).show();

			}
				
		}
	}
	

	@Override
	public void onBackPressed() {
		MyApplication.requestInProgress = false;
		super.onBackPressed();
	}

	public String fromInt(int val) {
		return String.valueOf(val);
	}

	public void scanPhoto(final String imageFileName) {
		msConn = new MediaScannerConnection(BigView.this,
				new MediaScannerConnectionClient() {
					public void onMediaScannerConnected() {
						msConn.scanFile(imageFileName, null);
						Log.i("msClient obj  in Photo Utility",
								"connection established");
					}

					public void onScanCompleted(String path, Uri uri) {
						msConn.disconnect();
						Log.i("msClient obj in Photo Utility", "scan completed");	
					}
				});
		msConn.connect();
		
		finish();
	}

	public void save(View v) {
		savePhoto(MyApplication.currentBitmap);
	}
}
