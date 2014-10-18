package com.kidgeniushq.susd.asynctasks;

import java.io.File;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.Gravity;
import android.widget.Toast;

import com.kidgeniushq.susd.MainActivity;

public class UploadStoryAsyncTask extends AsyncTask<String, Void, Boolean> {
	Context context;
	Activity activity;
	Boolean isItAVideo;
	ProgressDialog progress;
	public UploadStoryAsyncTask(Context ctx, Activity act, Boolean video){
		context=ctx;
		activity=act;
		isItAVideo=video;
	}
	@Override
    protected void onPreExecute() {
		progress = ProgressDialog.show(this.activity, "Uploading...",
			    "wait a sec", true);
    	
    }
	@Override
	protected Boolean doInBackground(String... params) {
		File file=new File(activity.getFilesDir() + "/image");
		boolean result = MainActivity.snapchat.sendStory(file, isItAVideo, 8, "");
		return result;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		progress.dismiss();
		
		if (result){
			Toast tst= 
			Toast.makeText(context,
					"Succesfully Uploaded to Story", Toast.LENGTH_SHORT);
			tst.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);
					tst.show();
		}else{
			Toast tst=Toast.makeText(context,
					"Error occured please try again later", Toast.LENGTH_SHORT);
			tst.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);
			tst.show();
		}
	}
}
