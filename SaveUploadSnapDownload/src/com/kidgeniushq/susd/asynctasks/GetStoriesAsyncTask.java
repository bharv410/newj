package com.kidgeniushq.susd.asynctasks;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.habosa.javasnap.Story;
import com.kidgeniushq.susd.MainActivity;
import com.kidgeniushq.susd.R;
import com.kidgeniushq.susd.mainfragments.FeedFragment;

public class GetStoriesAsyncTask extends AsyncTask<String, Void, String> {
	Context context;
	Activity activity;
		public GetStoriesAsyncTask(Context ctx, Activity act){
			context=ctx;
			this.activity=act;
			
		}
		    @Override
		    protected String doInBackground(String... params) {
		    	Story[] storyObjs = MainActivity.snapchat.getStories();
		    	MainActivity.stories = Story.filterDownloadable(storyObjs);
		        return "Executed";
		    }

		    @Override
		    protected void onPostExecute(String result) {
		    	if(MainActivity.stories.length>0){
		    	Toast.makeText(context, "got " +MainActivity.stories.length + " stories", Toast.LENGTH_SHORT).show();
		    	FragmentActivity thisActivity=(FragmentActivity)activity;
		    	FeedFragment mystorysfrag = (FeedFragment)thisActivity.getSupportFragmentManager().findFragmentByTag(
		                "android:switcher:"+R.id.pager+":0");
				mystorysfrag.addImagesToScreen();
		    	System.out.println("success" +MainActivity.stories.length);
		    	}else
		    		Toast.makeText(context, "error", Toast.LENGTH_SHORT).show();
		    }
}
