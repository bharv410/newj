package com.kidgeniushq.susd;

import com.habosa.javasnap.Snapchat;
import com.habosa.javasnap.Story;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import android.app.IntentService;
import android.content.Intent;
import android.widget.Toast;

public class MyStoryGrabberService extends IntentService{
	public MyStoryGrabberService() {
		super("Service");
	}
	public MyStoryGrabberService(String name) {
		super(name);

	}
	@Override
	protected void onHandleIntent(Intent intent) {

		
		
		Snapchat snapchat = Snapchat.login("boutmabenjamins", "iforgot05");
		Story[] storyObjs = snapchat.getMyStories();
		Story[] downloadable = Story.filterDownloadable(storyObjs);
		for(Story s:downloadable){
			if(s.isMine()){
				System.out.println("IT IS MINNNEEE"+s.getTime()+s.getSender());

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
