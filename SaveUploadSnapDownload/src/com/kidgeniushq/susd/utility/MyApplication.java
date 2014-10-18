package com.kidgeniushq.susd.utility;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.parse.Parse;

import android.app.Application;

public class MyApplication extends Application {

	@Override
	public void onCreate(){
		super.onCreate();
		Parse.initialize(this, "ueSahQADdPbW6NrOur4ySHyGkUbVpdeCyvOqS5MP", "KBJ0uWzC4OgeMi7GyPZVWJvT1wwyMEbdLK2AGK6C");
		// Create global configuration and initialize ImageLoader with this config
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
        ImageLoader.getInstance().init(config);
        }
}
