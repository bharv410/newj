package com.kidgeniushq.susd;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Images.Media;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.kidgeniushq.susd.utility.MyApplication;

public class BigView extends Activity {
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
		getActionBar().hide();
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

	public Uri addImageToGallery(Context context, String filepath,
			String title, String description) {
		ContentValues values = new ContentValues();
		values.put(Media.TITLE, title);
		values.put(Media.DESCRIPTION, description);
		values.put(Images.Media.DATE_TAKEN, System.currentTimeMillis());
		values.put(Images.Media.MIME_TYPE, "image/jpeg");
		values.put(MediaStore.MediaColumns.DATA, filepath);
		return context.getContentResolver().insert(Media.EXTERNAL_CONTENT_URI,
				values);
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
