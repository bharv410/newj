package com.kidgeniushq.susd;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.MediaController;
import android.widget.VideoView;

import com.habosa.javasnap.Snapchat;
import com.kidgeniushq.susd.mainfragments.FeedFragment;
import com.kidgeniushq.susd.utility.Utility;

public class VideoViewActivity extends ActionBarActivity {
	private VideoView myVideoView;

	private int position = 0;

	private ProgressDialog progressDialog;

	private MediaController mediaControls;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video_view);
		// set the media controller buttons

		if (mediaControls == null) {

			mediaControls = new MediaController(VideoViewActivity.this);

		}

		// initialize the VideoView

		myVideoView = (VideoView) findViewById(R.id.bigVideoView);

		// create a progress bar while the video file is loading

		progressDialog = new ProgressDialog(VideoViewActivity.this);

		// set a title for the progress bar

		progressDialog.setTitle("JavaCodeGeeks Android Video View Example");

		// set a message for the progress bar

		progressDialog.setMessage("Loading...");

		// set the progress bar not cancelable on users' touch

		progressDialog.setCancelable(false);

		// show the progress bar

		progressDialog.show();
		
		byte[] snapBytes=Snapchat.getStory(Utility.currentStory);
		File vidFile = new File(getFilesDir()
				+ "/video.mp4");
		
		if(snapBytes[0] == 0x50 && snapBytes[1] == 0x4B) {
		    Log.d("snapBytes", "Snap is compressed in a ZIP file");
		    ByteArrayInputStream zipInput = new ByteArrayInputStream(snapBytes);
		    try {
		        ZipInputStream zis = new ZipInputStream(zipInput);
		        ZipEntry ze = zis.getNextEntry();

		        while (ze != null) {
		            String fileName = ze.getName();
		            if (fileName.contains("media")) {
		                FileOutputStream out = new FileOutputStream(vidFile);
		                int leido;
		                byte[] buffer = new byte[1024];
		                while (0 < (leido = zis.read(buffer))) {
		                    out.write(buffer, 0, leido);
		                }
		                out.close();
		            }
		            ze = zis.getNextEntry();
		        }
		    } catch (FileNotFoundException e) {
		        e.printStackTrace();
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
		} else {
		    try {
		        FileOutputStream out = new FileOutputStream(vidFile);
		        out.write(snapBytes, 0, snapBytes.length);
		        out.close();
		    } catch (FileNotFoundException e) {
		        e.printStackTrace();
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
		
		

		try {

			// set the media controller in the VideoView

			myVideoView.setMediaController(mediaControls);

			myVideoView.setVideoURI(Uri.fromFile(vidFile));

		} catch (Exception e) {

			Log.e("Error", e.getMessage());

			e.printStackTrace();

		}

		myVideoView.requestFocus();

		// we also set an setOnPreparedListener in order to know when the video
		// file is ready for playback

		myVideoView.setOnPreparedListener(new OnPreparedListener() {

			public void onPrepared(MediaPlayer mediaPlayer) {

				// close the progress bar and play the video

				progressDialog.dismiss();

				// if we have a position on savedInstanceState, the video
				// playback should start from here

				myVideoView.seekTo(position);

				if (position == 0) {

					myVideoView.start();

				} else {

					// if we come from a resumed activity, video playback will
					// be paused

					myVideoView.pause();

				}

			}

		});

	}}
	@Override
	public void onBackPressed() {

		FeedFragment.requestInProgress=false;
		startActivity(new Intent(VideoViewActivity.this,MainActivity.class));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.video_view, menu);
		return true;
	}
		@Override
	
	    public void onSaveInstanceState(Bundle savedInstanceState) {
	
	        super.onSaveInstanceState(savedInstanceState);
	
	        //we use onSaveInstanceState in order to store the video playback position for orientation change
	
	        savedInstanceState.putInt("Position", myVideoView.getCurrentPosition());
	
	        myVideoView.pause();
	
	    }
	
	 
	
	    @Override
	
	    public void onRestoreInstanceState(Bundle savedInstanceState) {
	
	        super.onRestoreInstanceState(savedInstanceState);
	
	        //we use onRestoreInstanceState in order to play the video playback from the stored position 
	
	        position = savedInstanceState.getInt("Position");
	
	        myVideoView.seekTo(position);
	
	    }


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
