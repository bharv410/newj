package com.kidgeniushq.susd.asynctasks;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.habosa.javasnap.Snap;
import com.kidgeniushq.susd.MainActivity;
import com.kidgeniushq.susd.utility.MyApplication;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.widget.Toast;

public class SaveUnreadToGalleryTask extends AsyncTask<Void,Void,Boolean>{

	int count;
	@Override
	protected Boolean doInBackground(Void... arg0) {
		count=0;
		for(Snap s:MyApplication.allMySnaps){
		byte[] snapBytes = MyApplication.snapchat.getSnap(s);
		 
	      // Create a new file in SD Card 
		File snapFile = new File((Environment.getExternalStorageDirectory().getAbsolutePath()
	              + "/UnreadSnaps/"),String.valueOf(System.currentTimeMillis()));
		FileOutputStream snapOs;
		try {
			snapOs = new FileOutputStream(snapFile);
		snapOs.write(snapBytes);
		snapOs.close();
		addImageToGallery(snapFile.getAbsolutePath());
		count++;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		}
		
		return null;
	}
	@Override
	public void onPostExecute(Boolean result){
		try{
		Toast.makeText(MainActivity.mContext, "saved "+count+" unread snaps to gallery!!", Toast.LENGTH_LONG).show();
		Toast.makeText(MainActivity.mContext, "saved "+count+" unread snaps to gallery!!", Toast.LENGTH_LONG).show();
	}catch(Exception e){
	}
	}
	
	public static void addImageToGallery(final String filePath) {
		 
	    ContentValues values = new ContentValues();
	 
	    values.put(Images.Media.DATE_TAKEN, System.currentTimeMillis());
	    values.put(Images.Media.MIME_TYPE, "image/jpeg");
	    values.put(MediaStore.MediaColumns.DATA, filePath);
	 
	    MainActivity.mContext.getContentResolver().insert(Images.Media.EXTERNAL_CONTENT_URI, values);
	} 

}
