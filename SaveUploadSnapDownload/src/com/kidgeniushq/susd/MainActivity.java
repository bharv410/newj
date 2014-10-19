package com.kidgeniushq.susd;

import java.util.ArrayList;
import java.util.List;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.kidgeniushq.susd.asynctasks.LoginAsyncTask;
import com.kidgeniushq.susd.asynctasks.UploadStoryAsyncTask;
import com.kidgeniushq.susd.mainfragments.FeedFragment;
import com.kidgeniushq.susd.mainfragments.UploadFragment;
import com.kidgeniushq.susd.model.MyStory;
import com.kidgeniushq.susd.utility.MyApplication;
import com.kidgeniushq.susd.utility.MyStorysAlarmReciever;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.viewpagerindicator.TitlePageIndicator;

//icon by samuel green
@TargetApi(Build.VERSION_CODES.HONEYCOMB) public class MainActivity extends FragmentActivity {
	MyStorysAlarmReciever alarm = new MyStorysAlarmReciever();
	byte[] snapData;
	SectionsPagerAdapter mSectionsPagerAdapter;
	ViewPager mViewPager;
	Uri currImageURI;
	Bitmap currentBitmap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		getActionBar().hide();
		//getMyStories();
		// alarm.setAlarm(getApplicationContext());
		login();
		

		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		//Bind the title indicator to the adapter
		 TitlePageIndicator titleIndicator = (TitlePageIndicator)findViewById(R.id.titles);
		 titleIndicator.setViewPager(mViewPager);
	}

	private void login() {
		MyApplication.username = "boutmabenjamins";
		MyApplication.password = "iforgot05";
		LoginAsyncTask lat = new LoginAsyncTask(getApplicationContext(),
				MainActivity.this);
		lat.execute(MyApplication.username, MyApplication.password);

//		 AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
//		
//		 alertDialog.setTitle("Login");
//		 final EditText quantity = new EditText(MainActivity.this);
//		 final EditText lot = new EditText(MainActivity.this);
//		
//		 quantity.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
//		 quantity.setHint("username");
//		 lot.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
//		 lot.setHint("password");
//		
//		 LinearLayout ll = new LinearLayout(this);
//		 ll.setOrientation(LinearLayout.VERTICAL);
//		 ll.addView(quantity);
//		 ll.addView(lot);
//		 alertDialog.setView(ll);
//		
//		 alertDialog.setCancelable(false);
//		 alertDialog.setPositiveButton("Login",
//		 new DialogInterface.OnClickListener() {
//		 public void onClick(DialogInterface dialog, int id) {
//		 MyApplication.username = quantity.getText().toString();
//		 MyApplication.password = lot.getText().toString();
//		 LoginAsyncTask lat = new LoginAsyncTask(
//		 getApplicationContext(), MainActivity.this);
//		 lat.execute(MyApplication.username, MyApplication.password);
//		 }
//		 });
//		 AlertDialog alert = alertDialog.create();
//		 alert.show();
	}

	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			if (position == 0)
				return new FeedFragment();
			else 
				return new UploadFragment();
//			else if (position == 2)
//				return new MyStoryGridFragment();
//			else
//				return PlaceholderFragment.newInstance(position + 1);

		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 2;
		}
		@Override
        public CharSequence getPageTitle(int position) {
			if (position == 0)
				return "Feed";
			else 
				return "Upload";
        }
	}

	public void uploadImage(View v) {
		// set listener for the caption
//		final EditText txtEdit = (EditText) findViewById(R.id.captionEditText);
//		txtEdit.setOnFocusChangeListener(new OnFocusChangeListener() {
//			public void onFocusChange(View v, boolean hasFocus) {
//				if (!hasFocus)
//					currentBitmap = Utility.drawTextToBitmap(
//							getApplicationContext(), currentBitmap, txtEdit
//									.getText().toString());
//			}
//		});

		startActivity(new Intent(this,SendImageActivity.class));
	}

	public void uploadVideo(View v) {
		Toast.makeText(getApplicationContext(), "Not available yet",
				Toast.LENGTH_SHORT).show();
		
	}

	private void getMyStories() {
		System.out.println("GETTING MY STORIES");
		MyApplication.allMyStories = new ArrayList<MyStory>();
		ParseQuery<ParseObject> query = ParseQuery.getQuery("TestSnaps");
		query.orderByDescending("createdAt");
		query.setLimit(6);
		query.findInBackground(new FindCallback<ParseObject>() {
			@Override
			public void done(List<ParseObject> scoreList, ParseException e) {
				System.out.println("QUERYING TESTSNAPS COMPLETE");
				if (e == null) {
					Log.d("score", "Retrieved " + scoreList.size() + " scores");
					for (int i=0;i< scoreList.size();i++) {
						final ParseObject snap = scoreList
								.get(i);
						System.out.println("got SNAP WITH ID: "
								+ snap.getString("snapid"));
						MyApplication.allMyStories.add(new MyStory(snap
								.getInt("viewcount"), snap.getString("snapid"),
								snap.getString("date")));
						ParseFile actualSnap = snap.getParseFile("story");
						actualSnap.getDataInBackground(new GetDataCallback() {
							public void done(byte[] data, ParseException e) {
								System.out.println("QUERYING PHOTO COMPLETE");
								if (e == null) {
									// see mystory constructor
									MyApplication.allMyStories.get(
											MyApplication.addedImageToMyStory)
											.setImageBytes(data);
									MyApplication.addedImageToMyStory++;
									System.out
											.println("ADDED TO GLOBAL VARIABLE");
								}
							}
						});
					}
					System.out.println("DONE");
				} else {
					System.out.println("error");
				}
			}
		});
	}

	public void sendItOut(View v) {
		ImageView imageViewForChosenPic = (ImageView) findViewById(R.id.uploadImageView);
		imageViewForChosenPic.setImageBitmap(currentBitmap);
		UploadStoryAsyncTask us = new UploadStoryAsyncTask(
				getApplicationContext(), this, false);
		us.execute();
	}
}
