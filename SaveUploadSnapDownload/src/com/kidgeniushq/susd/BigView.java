package com.kidgeniushq.susd;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;

import com.habosa.javasnap.Snapchat;
import com.kidgeniushq.susd.mainfragments.FeedFragment;
import com.kidgeniushq.susd.utility.Utility;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class BigView extends ActionBarActivity {
	ImageView iv;
	File imageFileFolder, imageFileName;
	MediaScannerConnection msConn;
	FileOutputStream fileOutputStream;
	File file1;
	Bitmap bm;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_big_view);
		iv = (ImageView) findViewById(R.id.bigImageView);
		
		iv.setImageBitmap(Utility.currentBitmap);
		//new LongOperation().execute();

	}
	private class LongOperation extends AsyncTask<Bitmap, Void, String> {
        @Override
        protected String doInBackground(Bitmap... params) {
        	bm = Utility.getPhoto(Snapchat.getStory(Utility.currentStory));
            return "succes";
        }

        @Override
        protected void onPostExecute(String result) {
        	iv.setImageBitmap(bm);
        }
    }
	public void savePhoto(Bitmap bmp) {
		imageFileFolder = new File(Environment.getExternalStorageDirectory(),
				"Saved Snaps");
		imageFileFolder.mkdir();
		FileOutputStream out = null;
		Calendar c = Calendar.getInstance();
		String date = fromInt(c.get(Calendar.MONTH))
				+ fromInt(c.get(Calendar.DAY_OF_MONTH))
				+ fromInt(c.get(Calendar.YEAR))
				+ fromInt(c.get(Calendar.HOUR_OF_DAY))
				+ fromInt(c.get(Calendar.MINUTE))
				+ fromInt(c.get(Calendar.SECOND));
		imageFileName = new File(imageFileFolder, date.toString() + ".jpg");
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
	@Override
	public void onBackPressed() {

		FeedFragment.requestInProgress=false;
		startActivity(new Intent(BigView.this,MainActivity.class));
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
		Toast.makeText(getApplicationContext(), "Saved to Gallery", Toast.LENGTH_LONG)
				.show();
	}
	public void save(View v){
		savePhoto(bm);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.big_view, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
