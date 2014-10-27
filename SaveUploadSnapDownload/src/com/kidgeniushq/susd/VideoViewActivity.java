package com.kidgeniushq.susd;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

import com.google.android.gms.analytics.HitBuilders;
import com.kidgeniushq.susd.utility.MyApplication;
import com.kidgeniushq.susd.utility.MyApplication.TrackerName;

@TargetApi(Build.VERSION_CODES.HONEYCOMB) public class VideoViewActivity extends Activity {
	private VideoView myVideoView;

	private int position = 0;

	private ProgressDialog progressDialog;

	private MediaController mediaControls;
	File vidFile;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video_view);
		((MyApplication) getApplication()).getTracker(MyApplication.TrackerName.APP_TRACKER);
		getActionBar().hide();
		if (mediaControls == null) {
			mediaControls = new MediaController(VideoViewActivity.this);
		}

		// initialize the VideoView
		myVideoView = (VideoView) findViewById(R.id.bigVideoView);
		progressDialog = new ProgressDialog(VideoViewActivity.this);
		progressDialog.setTitle("Grabbing vid");
		progressDialog.setMessage("Loading...");
		progressDialog.setCancelable(false);
		progressDialog.show();

		File sdCard = Environment.getExternalStorageDirectory();
		File dir = new File (sdCard.getAbsolutePath() + "/dir1/dir2");
		dir.mkdirs();
		vidFile = new File(dir, "/"+MyApplication.vidIndex+"video.mp4");
		System.out.print(vidFile.getAbsolutePath());

		try {
			// set the media controller in the VideoView
			myVideoView.setMediaController(mediaControls);
			myVideoView.setVideoURI(Uri.fromFile(vidFile));
		} catch (Exception e) {
			Log.e("Error", e.getMessage());
			e.printStackTrace();
		}
		myVideoView.requestFocus();
		myVideoView.setOnPreparedListener(new OnPreparedListener() {
			public void onPrepared(MediaPlayer mediaPlayer) {
				progressDialog.dismiss();
				myVideoView.seekTo(position);
				if (position == 0) {
					myVideoView.start();
				} else {
					myVideoView.pause();
				}
			}
		});
	}

	@Override
	public void onBackPressed() {

	MyApplication.requestInProgress=false;;
	super.onBackPressed();}
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {

		super.onSaveInstanceState(savedInstanceState);

		// we use onSaveInstanceState in order to store the video playback
		// position for orientation change

		savedInstanceState.putInt("Position", myVideoView.getCurrentPosition());

		myVideoView.pause();

	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {

		super.onRestoreInstanceState(savedInstanceState);

		// we use onRestoreInstanceState in order to play the video playback
		// from the stored position

		position = savedInstanceState.getInt("Position");

		myVideoView.seekTo(position);

	}
	@Override
	public void onStart() { 
        super.onStart(); 
      //Get tracker.
        com.google.android.gms.analytics.Tracker t = ((MyApplication) getApplication()).getTracker(
         TrackerName.APP_TRACKER);

        //Enable Advertising Features.
        t.enableAdvertisingIdCollection(true);
     // Set screen name.
        //t.setScreenName(screenName);

        // Send a screen view.
        t.send(new HitBuilders.AppViewBuilder().build());

    } 
	
	public void save(View v){
		String title=MyApplication.currentStory.getSender()+MyApplication.currentStory.getCaption();
		
		// Save the name and description of a video in a ContentValues map.  
        ContentValues values = new ContentValues(2);
        values.put(MediaStore.Video.Media.MIME_TYPE, "video/mp4");
        //values.put(MediaStore.Video.Media.DATA, vidFile.getAbsolutePath()); 
        System.out.print(vidFile.getAbsolutePath());
        // Add a new record (identified by uri) without the video, but with the values just set.
        Uri uri = getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values);

        // Now get a handle to the file for that record, and save the data into it.
        try {
            InputStream is = new FileInputStream(vidFile);
            OutputStream os = getContentResolver().openOutputStream(uri);
            byte[] buffer = new byte[4096]; // tweaking this number may increase performance
            int len;
            while ((len = is.read(buffer)) != -1){
                os.write(buffer, 0, len);
            }
            os.flush();
            is.close();
            os.close();
        } catch (Exception e) {
            Log.e("Video activity", "exception while writing video: ", e);
        } 

        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
	}
}
