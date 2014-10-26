package com.kidgeniushq.susd.asynctasks;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.habosa.javasnap.Friend;
import com.habosa.javasnap.Story;
import com.kidgeniushq.susd.R;
import com.kidgeniushq.susd.mainfragments.FeedFragment;
import com.kidgeniushq.susd.utility.MyApplication;

public class GetStoriesAsyncTask extends AsyncTask<String, Void, String> {
	Context context;
	Activity activity;
		public GetStoriesAsyncTask(Context ctx, Activity act){
			context=ctx;
			this.activity=act;
			
		}
		    @Override
		    protected String doInBackground(String... params) {
		    	MyApplication.myFriends=MyApplication.snapchat.getFriends();
		    	Story[] storyObjs = MyApplication.snapchat.getStories();
		    	MyApplication.stories = Story.filterDownloadable(storyObjs);
		        return "Executed";
		    }

		    @Override
		    protected void onPostExecute(String result) {
		    	if(MyApplication.stories.length>0){
		    	Toast.makeText(context, "got " +MyApplication.stories.length + " stories", Toast.LENGTH_SHORT).show();
		    	FragmentActivity thisActivity=(FragmentActivity)activity;
		    	FeedFragment mystorysfrag = (FeedFragment)thisActivity.getSupportFragmentManager().findFragmentByTag(
		                "android:switcher:"+R.id.pager+":0");
		    	mystorysfrag.vidFiles=new File[MyApplication.stories.length];
				mystorysfrag.addImagesToScreen();
				MyApplication.friendsNames.add("My Story");
				for(Friend fr : MyApplication.myFriends){
					
					MyApplication.friendsNames.add(fr.getUsername());
				}
		    	}else
		    		Toast.makeText(context, "error", Toast.LENGTH_SHORT).show();
		    }
}
