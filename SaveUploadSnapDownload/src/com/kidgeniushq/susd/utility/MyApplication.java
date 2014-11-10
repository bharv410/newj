package com.kidgeniushq.susd.utility;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Application;
import android.graphics.Bitmap;

import com.habosa.javasnap.Friend;
import com.habosa.javasnap.Snap;
import com.habosa.javasnap.Snapchat;
import com.habosa.javasnap.Story;
import com.kidgeniushq.susd.MainActivity;
import com.kidgeniushq.susd.model.MyStory;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.parse.Parse;
import com.parse.PushService;

public class MyApplication extends Application {
	//static variables for fragments to use
	public static File[] vidFiles;
		public static String username, password,currentFriend;
		public static ArrayList<MyStory> allMyStories;
		public static Snap[] allMySnaps;
		public static Snapchat snapchat;
		public static Story[] stories;
		public static Boolean loadingStoriesToImage=false;
		public static Story currentStory;
		public static Bitmap currentBitmap;
		public static boolean requestInProgress = false;//so we don't load too much at once
		public static List<Bitmap> imageList;
		public static int addedImageToMyStory;
		public static Friend[] myFriends;
		public static ArrayList<String> friendsNames,unreadSenders;
		public static int vidIndex;//fixes bug for vids
		public static int gridX,gridY;
		public static HashMap<String,Bitmap> myUnreads;
		final String PROPERTY_ID ="UA-52625155-3";
	@Override
	public void onCreate(){
		super.onCreate();
		gridX=0;
		gridY=0;
		addedImageToMyStory=0;
		friendsNames= new ArrayList<String>();
		unreadSenders= new ArrayList<String>();
		myUnreads= new HashMap<String,Bitmap>();
		Parse.initialize(this, "NSi9jWGJWQnWljIOtGjOXtvPpKIwefhpVdq9SlOb", "LOPmGbcEfYol9Gl5zjVJShBrJb03qmKAVWRtX2i1");
		  // Also in this method, specify a default Activity to handle push notifications
		  PushService.setDefaultPushCallback(this, MainActivity.class);
		
		// Create global configuration and initialize ImageLoader with this config
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
        ImageLoader.getInstance().init(config);
        }
}
