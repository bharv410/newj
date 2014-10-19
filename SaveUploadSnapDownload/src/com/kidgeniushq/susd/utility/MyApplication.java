package com.kidgeniushq.susd.utility;

import java.util.ArrayList;
import java.util.List;

import com.habosa.javasnap.Friend;
import com.habosa.javasnap.Snapchat;
import com.habosa.javasnap.Story;
import com.kidgeniushq.susd.model.MyStory;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.parse.Parse;

import android.app.Application;
import android.graphics.Bitmap;

public class MyApplication extends Application {
	//static variables for fragments to use
		public static String username, password;
		public static ArrayList<MyStory> allMyStories;
		public static Snapchat snapchat;
		public static Story[] stories;
		public static Boolean loadingStoriesToImage=false;
		public static Story currentStory;
		public static Bitmap currentBitmap;
		public static boolean requestInProgress = false;//so we don't load too much at once
		public static List<Bitmap> imageList;
		public static int addedImageToMyStory;
		public static Friend[] myFriends;
		public static ArrayList<String> friendsNames;
		public static int vidIndex;//fixes bug for vids
	@Override
	public void onCreate(){
		super.onCreate();
		addedImageToMyStory=0;
		friendsNames= new ArrayList<String>();
		Parse.initialize(this, "ueSahQADdPbW6NrOur4ySHyGkUbVpdeCyvOqS5MP", "KBJ0uWzC4OgeMi7GyPZVWJvT1wwyMEbdLK2AGK6C");
		// Create global configuration and initialize ImageLoader with this config
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
        ImageLoader.getInstance().init(config);
        }
}
