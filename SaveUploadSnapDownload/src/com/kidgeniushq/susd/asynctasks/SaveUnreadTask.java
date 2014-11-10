package com.kidgeniushq.susd.asynctasks;

import android.content.ContentValues;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.widget.Toast;

import com.habosa.javasnap.Snap;
import com.kidgeniushq.susd.MainActivity;
import com.kidgeniushq.susd.utility.MyApplication;
import com.kidgeniushq.susd.utility.Utility;

public class SaveUnreadTask extends AsyncTask<Void,Void,Boolean>{

	int count;
	@Override
	protected Boolean doInBackground(Void... arg0) {
		count=0;
		for(Snap s:MyApplication.allMySnaps){
			if(s.isImage()){
		byte[] snapBytes = MyApplication.snapchat.getSnap(s);
		MyApplication.myUnreads.put(s.getSender()+s.getSentTime(),Utility.getPhoto(snapBytes));
		MyApplication.unreadSenders.add(s.getSender()+s.getSentTime());
		count++;
			}
		}
		
		return null;
	}
	@Override
	public void onPostExecute(Boolean result){
		try{
			if(count>0){
		Toast.makeText(MainActivity.mContext, "got "+count+" unread snaps !!", Toast.LENGTH_LONG).show();
			}}catch(Exception e){
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
