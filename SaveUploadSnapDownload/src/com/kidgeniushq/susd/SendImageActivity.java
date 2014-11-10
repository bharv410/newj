package com.kidgeniushq.susd;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.kidgeniushq.susd.utility.MyApplication;

public class SendImageActivity extends Activity {
	// byte[] inputData;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_send_image);
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().hide();
		}

		ImageView imageViewForChosenPic = (ImageView) findViewById(R.id.uploadImageView);
		imageViewForChosenPic.setImageBitmap(MyApplication.currentBitmap); // set
																			// the
																			// image
																			// on
																			// the
																			// view

	}

	public void sendTo(View v) {
		try {
			FileOutputStream fOut = new FileOutputStream(new File(getFilesDir(),"uploadimage.jpeg"));
			MyApplication.currentBitmap.compress(Bitmap.CompressFormat.JPEG,
					60, fOut);
			fOut.flush();
			fOut.close();
			startActivity(new Intent(this, RecipientsActivity.class));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setBigText(View v) {
		ImageView imageViewForChosenPic = (ImageView) findViewById(R.id.uploadImageView);
		try {
			MyApplication.currentBitmap = drawBigTextToBitmap(
					getApplicationContext(), MyApplication.currentBitmap,
					((EditText) findViewById(R.id.imageCaptionEditText))
							.getText().toString());
			imageViewForChosenPic.setImageBitmap(MyApplication.currentBitmap);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setSmallText(View v) {
		ImageView imageViewForChosenPic = (ImageView) findViewById(R.id.uploadImageView);
		try {
			MyApplication.currentBitmap = drawSmallTextToBitmap(
					getApplicationContext(), MyApplication.currentBitmap,
					((EditText) findViewById(R.id.imageCaptionEditText))
							.getText().toString());
			imageViewForChosenPic.setImageBitmap(MyApplication.currentBitmap);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Bitmap drawSmallTextToBitmap(Context gContext, Bitmap bm,
			String gText) {
		Resources resources = gContext.getResources();
		float scale = resources.getDisplayMetrics().density;
		android.graphics.Bitmap.Config bitmapConfig = bm.getConfig();
		if (bitmapConfig == null) {
			bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;
		}
		Bitmap newBm = bm.copy(bitmapConfig, true);
		Canvas canvas = new Canvas(newBm);

		// set caption text
		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setColor(Color.WHITE);
		paint.setTextSize((int) (24 * scale));
		paint.setShadowLayer(1f, 0f, 1f, Color.WHITE);
		Rect bounds = new Rect();
		paint.getTextBounds(gText, 0, gText.length(), bounds);
		int x = (newBm.getWidth() - bounds.width()) / 2;
		int y = (newBm.getHeight() + bounds.height()) / 2;

		// set background
		int screenWidth = getApplicationContext().getResources()
				.getDisplayMetrics().widthPixels;
		Rect greyBack = new Rect(0, newBm.getHeight() / 2 + bounds.height(),
				screenWidth, newBm.getHeight() / 2 - bounds.height());
		Paint paintBG = new Paint();
		paintBG.setARGB(128, 100, 100, 100); // added alpha because Snapchat has
												// translucent //grey background
		canvas.drawRect(greyBack, paintBG);

		canvas.drawText(gText, x, y, paint);

		return newBm;
	}

	public Bitmap drawBigTextToBitmap(Context gContext, Bitmap bm, String gText) {
		Resources resources = gContext.getResources();
		float scale = resources.getDisplayMetrics().density;
		android.graphics.Bitmap.Config bitmapConfig = bm.getConfig();
		if (bitmapConfig == null) {
			bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;
		}
		Bitmap newBm = bm.copy(bitmapConfig, true);
		Canvas canvas = new Canvas(newBm);

		// set caption text
		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setColor(Color.WHITE);
		paint.setTextSize((int) (44 * scale));
		paint.setShadowLayer(1f, 0f, 1f, Color.WHITE);
		Rect bounds = new Rect();
		paint.getTextBounds(gText, 0, gText.length(), bounds);
		int x = (newBm.getWidth() - bounds.width()) / 2;
		int y = (newBm.getHeight() + bounds.height()) / 2;

		canvas.drawText(gText, x, y, paint);

		return newBm;
	}

}
