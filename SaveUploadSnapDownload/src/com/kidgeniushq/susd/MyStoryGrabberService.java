package com.kidgeniushq.susd;

import android.app.IntentService;
import android.content.Intent;

import com.habosa.javasnap.Snapchat;
import com.habosa.javasnap.Story;
import com.kidgeniushq.susd.utility.MyApplication;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.SaveCallback;

public class MyStoryGrabberService extends IntentService{
	public MyStoryGrabberService() {
		super("Service");
	}
	public MyStoryGrabberService(String name) {
		super(name);

	}
	@Override
	protected void onHandleIntent(Intent intent) {
		Story[] downloadable;
		if(MyApplication.snapchat==null){
		Snapchat snapchat = Snapchat.login("boutmabenjamins", "iforgot05");
		Story[] storyObjs = snapchat.getMyStories();
		downloadable = Story.filterDownloadable(storyObjs);
		}else{
			downloadable=MyApplication.snapchat.getMyStories();
		}
		
		for(Story s:downloadable){
			if(s.isMine()){
				System.out.println(""+s.getTime()+s.getSender());
				final ParseObject testObject = new ParseObject("TestSnaps");
				testObject.put("viewcount", s.getViewCount());
				testObject.put("snapid", s.getId());
				testObject.put("date", s.getTime());
				final ParseFile chosenImage=new ParseFile("story.png", Snapchat.getStory(s));
				chosenImage.saveInBackground(new SaveCallback(){
					@Override
					public void done(ParseException arg0) {
						testObject.put("story", chosenImage);
						testObject.saveInBackground();
					}
				});
			}
		}	
	}
}
