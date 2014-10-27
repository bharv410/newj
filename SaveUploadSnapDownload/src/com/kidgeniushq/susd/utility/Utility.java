package com.kidgeniushq.susd.utility;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.habosa.javasnap.Story;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class Utility {
	
	
	public static byte[] getBytes(InputStream inputStream) {
		ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
		int bufferSize = 1024;
		byte[] buffer = new byte[bufferSize];
		int len = 0;
		try {
			while ((len = inputStream.read(buffer)) != -1) {
				byteBuffer.write(buffer, 0, len);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return byteBuffer.toByteArray();
	}
	public static Bitmap getPhoto(byte[] image) {
		
		BitmapFactory.Options options=new BitmapFactory.Options();// Create object of bitmapfactory's option method for further option use
        options.inPurgeable = true; // inPurgeable is used to free up memory while required
        options.inSampleSize = 2;
		  return BitmapFactory.decodeByteArray(image, 0, image.length,options);
		 }
	public static Bitmap drawTextToBitmap(Context gContext, 
			  Bitmap bm, 
			  String gText) {
			  Resources resources = gContext.getResources();
			  float scale = resources.getDisplayMetrics().density;
			  android.graphics.Bitmap.Config bitmapConfig =
			      bm.getConfig();
			  if(bitmapConfig == null) {
			    bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;
			  }
			  Bitmap newBm = bm.copy(bitmapConfig, true);
			  Canvas canvas = new Canvas(newBm);
			  Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
			  paint.setColor(Color.WHITE);
			  paint.setTextSize((int) (44 * scale));
			  paint.setShadowLayer(1f, 0f, 1f, Color.WHITE);
			  Rect bounds = new Rect();
			  paint.getTextBounds(gText, 0, gText.length(), bounds);
			  int x = (newBm.getWidth() - bounds.width())/2;
			  int y = (newBm.getHeight() + bounds.height())/2;
			 
			  canvas.drawText(gText, x, y, paint);
			 
			  return newBm;
			}

}
