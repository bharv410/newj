package com.kidgeniushq.susd.asynctasks;

import android.app.Activity;
import android.content.ContentValues;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.widget.Button;
import android.widget.Toast;

import com.habosa.javasnap.Snap;
import com.kidgeniushq.susd.MainActivity;
import com.kidgeniushq.susd.R;
import com.kidgeniushq.susd.utility.MyApplication;
import com.kidgeniushq.susd.utility.Utility;

public class SaveUnreadTask extends AsyncTask<Void,Void,Boolean>{
	Activity act;
	int count;
	
	public SaveUnreadTask(Activity activity){
		this.act=activity;
	}
	@Override
	protected Boolean doInBackground(Void... arg0) {
		count=0;
		for(Snap s:MyApplication.allMySnaps){
		byte[] snapBytes = MyApplication.snapchat.getSnap(s);
		if(s.isImage()){
			MyApplication.myUnreads.put("pic: "+s.getSender()+s.getSentTime(),snapBytes);
			MyApplication.unreadSenders.add("pic: "+s.getSender()+s.getSentTime());
		}else{
			MyApplication.myUnreads.put("vid: "+s.getSender()+s.getSentTime(),snapBytes);
			MyApplication.unreadSenders.add("vid: "+s.getSender()+s.getSentTime());
		}
		count++;
			
		}
		
		return null;
	}
	@Override
	public void onPostExecute(Boolean result){
		try{
			if(count>0){
		Toast.makeText(MainActivity.mContext, "got "+count+" unread snaps !!", Toast.LENGTH_LONG).show();
		Button btn =(Button)act.findViewById(R.id.unreadButton);
		btn.setText("" +count +" unread snaps");
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
