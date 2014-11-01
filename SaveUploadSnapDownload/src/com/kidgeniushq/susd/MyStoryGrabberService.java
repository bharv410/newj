package com.kidgeniushq.susd;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.habosa.javasnap.Snapchat;
import com.habosa.javasnap.Story;
import com.kidgeniushq.susd.utility.MyApplication;
import com.mixpanel.android.mpmetrics.MixpanelAPI;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

public class MyStoryGrabberService extends IntentService{
	public MixpanelAPI mMixpanel;
	public MyStoryGrabberService() {
		super("Service");
	}
	public MyStoryGrabberService(String name) {
		super(name);

	}
	@Override
	protected void onHandleIntent(Intent intent) {
		mMixpanel = MixpanelAPI.getInstance(getApplicationContext(),
				"5cbb4a097c852a733dd1836f865b082d");
		
		final List<String> allIds = new ArrayList<String>();
		Snapchat snapchat = Snapchat.login(intent.getStringExtra("un"), intent.getStringExtra("pw"));
		Story[] storyObjs = snapchat.getMyStories();
		Story[] downloadable = Story.filterDownloadable(storyObjs);
		
		ParseQuery<ParseObject> query = ParseQuery
				.getQuery(MyApplication.username);
		query.findInBackground(new FindCallback<ParseObject>() {
			@Override
			public void done(List<ParseObject> scoreList, ParseException e) {
				System.out.println("QUERYING COMPLETE");
				if (e == null) {
					Log.d("score", "Retrieved " + scoreList.size() + " scores");
					for (int i = 0; i < scoreList.size(); i++) {
						final ParseObject snap = scoreList.get(i);
						allIds.add(snap.getString("snapid"));
					}
				}
			}
		});
	
		for(Story s:downloadable){
			//if its a unique story
			if(s.isMine() && !allIds.contains(s.getId())){
				System.out.println(""+s.getTime()+s.getSender());
				final ParseObject testObject = new ParseObject(intent.getStringExtra("un"));
				testObject.put("viewcount", s.getViewCount());
				testObject.put("snapid", s.getId());
				testObject.put("date", s.getTime());
				final ParseFile chosenImage=new ParseFile("story.png", Snapchat.getStory(s));
				chosenImage.saveInBackground(new SaveCallback(){
					@Override
					public void done(ParseException arg0) {
						testObject.put("story", chosenImage);
						testObject.saveInBackground();
						JSONObject props = new JSONObject();
						try {
							props.put("username", "un");
							mMixpanel.track("Succesfully grabbedstory!", props);
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});
			}
		}	
	}
}
