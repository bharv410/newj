package com.kidgeniushq.susd;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.kidgeniushq.susd.asynctasks.LoginAsyncTask;
import com.kidgeniushq.susd.asynctasks.UploadStoryAsyncTask;
import com.kidgeniushq.susd.mainfragments.FeedFragment;
import com.kidgeniushq.susd.mainfragments.HelpFragment;
import com.kidgeniushq.susd.mainfragments.UploadFragment;
import com.kidgeniushq.susd.utility.MyApplication;
import com.kidgeniushq.susd.utility.MyApplication.TrackerName;
import com.kidgeniushq.susd.utility.MyStorysAlarmReciever;
import com.viewpagerindicator.LinePageIndicator;

//icon by samuel green
@TargetApi(Build.VERSION_CODES.HONEYCOMB) public class MainActivity extends FragmentActivity {
	MyStorysAlarmReciever alarm = new MyStorysAlarmReciever();
	byte[] snapData;
	SectionsPagerAdapter mSectionsPagerAdapter;
	ViewPager mViewPager;
	Uri currImageURI;
	Bitmap currentBitmap;
	public static final String TAG = MainActivity.class.getSimpleName();
	public static final int TAKE_PHOTO_REQUEST = 0;
	public static final int TAKE_VIDEO_REQUEST = 1;
	public static final int PICK_PHOTO_REQUEST = 2;
	public static final int PICK_VIDEO_REQUEST = 3;
	public static final int MEDIA_TYPE_IMAGE = 4;
	public static final int MEDIA_TYPE_VIDEO = 5;
	public static final int FILE_SIZE_LIMIT = 1024*1024*10; // 10 MB
	protected Uri mMediaUri;
	protected DialogInterface.OnClickListener mDialogListener;
	public Tracker t;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		t=((MyApplication) getApplication()).getTracker(MyApplication.TrackerName.APP_TRACKER);
		int currentAPIVersion = android.os.Build.VERSION.SDK_INT;
		if (currentAPIVersion >= android.os.Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().hide();
		} 
		//this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		//getMyStories();
		// alarm.setAlarm(getApplicationContext());		
		getNameAndPw();
		
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setOffscreenPageLimit(4);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		mViewPager.setOnPageChangeListener(new OnPageChangeListener(){
			 
            @Override
            public void onPageScrollStateChanged(int position) {}
            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {}
            @Override
            public void onPageSelected(int position) {
            	// Get tracker.
                Tracker t = ((MyApplication) getApplication()).getTracker(
                    TrackerName.APP_TRACKER);
                switch(position){
                case 0:
                    // Set screen name.
                    t.setScreenName("feed");
                    break;

                case 1:
                    // Set screen name.
                    t.setScreenName("upload");
                    break;
                case 2:
                	// Set screen name.
                    t.setScreenName("help");
                    break;
                case 3:
                    // Set screen name.
                    t.setScreenName("popular");
                    break;
                }
             // Send a screen view.
                t.send(new HitBuilders.AppViewBuilder().build());
            }

        });

		
		LinePageIndicator titleIndicator = (LinePageIndicator)findViewById(R.id.indicator);
		 titleIndicator.setViewPager(mViewPager);
	}
	public void getNameAndPw(){
		String line;
		BufferedReader in = null;

		try {
			in = new BufferedReader(new FileReader(new File(
					getApplicationContext().getFilesDir(), "username.txt")));
			while ((line = in.readLine()) != null) {
				System.out.println(line);
				MyApplication.username =line;
			}
			in.close();

			in = new BufferedReader(new FileReader(new File(
					getApplicationContext().getFilesDir(), "password.txt")));
			while ((line = in.readLine()) != null) {
				System.out.println(line);
				MyApplication.password = line;
			}
			
			 
			 LoginAsyncTask lat = new LoginAsyncTask(
			 getApplicationContext(), MainActivity.this);
			 lat.execute(MyApplication.username, MyApplication.password);
			 return;
			 
		} catch (FileNotFoundException e) {
			System.out.println(e);
		} catch (IOException e) {
			System.out.println(e);
		}
				
		//if it makes it here then first login
		login();
	}
	private void login() {
//		MyApplication.username = "boutmabenjamins";
//		MyApplication.password = "iforgot05";
//		LoginAsyncTask lat = new LoginAsyncTask(getApplicationContext(),
//				MainActivity.this);
//		lat.execute(MyApplication.username, MyApplication.password);
setupDialogListener();
		 AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
		
		 alertDialog.setTitle("Login");
		 final EditText quantity = new EditText(MainActivity.this);
		 final EditText lot = new EditText(MainActivity.this);
		
		 quantity.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
		 quantity.setHint("username");
		 lot.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
		 lot.setHint("password");
		
		 LinearLayout ll = new LinearLayout(this);
		 ll.setOrientation(LinearLayout.VERTICAL);
		 ll.addView(quantity);
		 ll.addView(lot);
		 alertDialog.setView(ll);
		
		 alertDialog.setCancelable(false);
		 alertDialog.setPositiveButton("Login",
		 new DialogInterface.OnClickListener() {
		 public void onClick(DialogInterface dialog, int id) {
		 MyApplication.username = quantity.getText().toString();
		 MyApplication.password = lot.getText().toString();
		 
		
		 
		 LoginAsyncTask lat = new LoginAsyncTask(
		 getApplicationContext(), MainActivity.this);
		 lat.execute(MyApplication.username, MyApplication.password);
		 }
		 });
		 AlertDialog alert = alertDialog.create();
		 alert.show();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if (resultCode == RESULT_OK) {			
			if (requestCode == PICK_PHOTO_REQUEST || requestCode == PICK_VIDEO_REQUEST) {
				if (data == null) {
					Toast.makeText(this, "error", Toast.LENGTH_LONG).show();
				}
				else {
					mMediaUri = data.getData();
				}
				
				Log.i(TAG, "Media URI: " + mMediaUri);
				if (requestCode == PICK_VIDEO_REQUEST) {
					// make sure the file is less than 10 MB
					int fileSize = 0;
					InputStream inputStream = null;
					
					try {
						inputStream = getContentResolver().openInputStream(mMediaUri);
						fileSize = inputStream.available();
					}
					catch (FileNotFoundException e) {
						Toast.makeText(this, "file error", Toast.LENGTH_LONG).show();
						return;
					}
					catch (IOException e) {
						Toast.makeText(this, "file error", Toast.LENGTH_LONG).show();
						return;
					}
					finally {
						try {
							inputStream.close();
						} catch (IOException e) { /* Intentionally blank */ }
					}
					
					if (fileSize >= FILE_SIZE_LIMIT) {
						Toast.makeText(this, "file too large", Toast.LENGTH_LONG).show();
						return;
					}
				}
			}
			else {
				Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
				mediaScanIntent.setData(mMediaUri);
				sendBroadcast(mediaScanIntent);
			}
			
			Intent recipientsIntent = new Intent(this, RecipientsActivity.class);
			recipientsIntent.setData(mMediaUri);
			
			String fileType;
			if (requestCode == PICK_PHOTO_REQUEST || requestCode == TAKE_PHOTO_REQUEST) {
				fileType = ".png";
			}
			else {
				fileType = ".mp4";
			}
			
			recipientsIntent.putExtra("fileType", fileType);
			startActivity(recipientsIntent);
		}
		else if (resultCode != RESULT_CANCELED) {
			Toast.makeText(this, "error", Toast.LENGTH_LONG).show();
		}
	}
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			if (position == 0)
				return new FeedFragment();
			else if (position == 1)
				return new UploadFragment();
			else if (position == 2)
				return new HelpFragment();
			else
				return new AddFriendsFragment();
//			else if (position == 2)
//				return new MyStoryGridFragment();
//			else
//				return PlaceholderFragment.newInstance(position + 1);

		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 4;
		}
		@Override
        public CharSequence getPageTitle(int position) {
			if (position == 0)
				return "Feed";
			else if(position == 1)
				return "Upload";
			else if(position == 2)
				return "Help";
			else
				return "Add Popular accounts!";
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
////2nd time
//		startActivity(new Intent(this,SendImageActivity.class));
		
		//3rd time
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setItems(new String[]{"Take Pic","Take Vid","Choose Pic","Choose Vid"}, mDialogListener);
		AlertDialog dialog = builder.create();
		dialog.show();
	}

	public void uploadVideo(View v) {
		startActivity(new Intent(this,SendImageActivity.class));
	}

	
//	private void getMyStories() {
//		System.out.println("GETTING MY STORIES");
//		MyApplication.allMyStories = new ArrayList<MyStory>();
//		ParseQuery<ParseObject> query = ParseQuery.getQuery("TestSnaps");
//		query.orderByDescending("createdAt");
//		query.setLimit(6);
//		query.findInBackground(new FindCallback<ParseObject>() {
//			@Override
//			public void done(List<ParseObject> scoreList, ParseException e) {
//				System.out.println("QUERYING TESTSNAPS COMPLETE");
//				if (e == null) {
//					Log.d("score", "Retrieved " + scoreList.size() + " scores");
//					for (int i=0;i< scoreList.size();i++) {
//						final ParseObject snap = scoreList
//								.get(i);
//						System.out.println("got SNAP WITH ID: "
//								+ snap.getString("snapid"));
//						MyApplication.allMyStories.add(new MyStory(snap
//								.getInt("viewcount"), snap.getString("snapid"),
//								snap.getString("date")));
//						ParseFile actualSnap = snap.getParseFile("story");
//						actualSnap.getDataInBackground(new GetDataCallback() {
//							public void done(byte[] data, ParseException e) {
//								System.out.println("QUERYING PHOTO COMPLETE");
//								if (e == null) {
//									// see mystory constructor
//									MyApplication.allMyStories.get(
//											MyApplication.addedImageToMyStory)
//											.setImageBytes(data);
//									MyApplication.addedImageToMyStory++;
//									System.out
//											.println("ADDED TO GLOBAL VARIABLE");
//								}
//							}
//						});
//					}
//					System.out.println("DONE");
//				} else {
//					System.out.println("error");
//				}
//			}
//		});
//	}

	public void sendItOut(View v) {
		ImageView imageViewForChosenPic = (ImageView) findViewById(R.id.uploadImageView);
		imageViewForChosenPic.setImageBitmap(currentBitmap);
		UploadStoryAsyncTask us = new UploadStoryAsyncTask(
				getApplicationContext(), this, false);
		us.execute();
	}
	private Map<String, String> getConnectionDetails() {
		
		Map<String, String> networkDetails = new HashMap<String, String>();
		try {
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo wifiNetwork = connectivityManager
		.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (wifiNetwork != null && wifiNetwork.isConnected()) {
		
		networkDetails.put("Type", wifiNetwork.getTypeName());
		networkDetails.put("Sub type", wifiNetwork.getSubtypeName());
		networkDetails.put("State", wifiNetwork.getState().name());
		}
		
		NetworkInfo mobileNetwork = connectivityManager
		.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		if (mobileNetwork != null && mobileNetwork.isConnected()) {
		networkDetails.put("Type", mobileNetwork.getTypeName());
		networkDetails.put("Sub type", mobileNetwork.getSubtypeName());
		networkDetails.put("State", mobileNetwork.getState().name());
		if (mobileNetwork.isRoaming()) {
		networkDetails.put("Roming", "YES");
		} else {
		networkDetails.put("Roming", "NO");
		}
		}
		} catch (Exception e) {
		networkDetails.put("Status", e.getMessage());
		}
		return networkDetails;
		}
	private boolean isExternalStorageAvailable() {
		String state = Environment.getExternalStorageState();
		
		if (state.equals(Environment.MEDIA_MOUNTED)) {
			return true;
		}
		else {
			return false;
		}
	}
	
	private void setupDialogListener(){
		mDialogListener=new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch(which) {
					case 0: // Take picture
						Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
						mMediaUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
						if (mMediaUri == null) {
							// display an error
							Toast.makeText(MainActivity.this, "errpr captureing image",
									Toast.LENGTH_LONG).show();
						}
						else {
							takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, mMediaUri);
							startActivityForResult(takePhotoIntent, TAKE_PHOTO_REQUEST);
						}
						break;
					case 1: // Take video
						Intent videoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
						mMediaUri = getOutputMediaFileUri(MEDIA_TYPE_VIDEO);
						if (mMediaUri == null) {
							// display an error
							Toast.makeText(MainActivity.this,"error taking video",
									Toast.LENGTH_LONG).show();
						}
						else {
							videoIntent.putExtra(MediaStore.EXTRA_OUTPUT, mMediaUri);
							videoIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 10);
							videoIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0); // 0 = lowest res
							startActivityForResult(videoIntent, TAKE_VIDEO_REQUEST);
						}
						break;
					case 2: // Choose picture
						Intent choosePhotoIntent = new Intent(Intent.ACTION_GET_CONTENT);
						choosePhotoIntent.setType("image/*");
						startActivityForResult(choosePhotoIntent, PICK_PHOTO_REQUEST);
						break;
					case 3: // Choose video
						Intent chooseVideoIntent = new Intent(Intent.ACTION_GET_CONTENT);
						chooseVideoIntent.setType("video/*");
						Toast.makeText(MainActivity.this, "video too large", Toast.LENGTH_LONG).show();
						startActivityForResult(chooseVideoIntent, PICK_VIDEO_REQUEST);
						break;
				}
			}
		};
	}
	private Uri getOutputMediaFileUri(int mediaType) {
		// To be safe, you should check that the SDCard is mounted
	    // using Environment.getExternalStorageState() before doing this.
		if (isExternalStorageAvailable()) {
			// get the URI
			
			// 1. Get the external storage directory
			String appName = MainActivity.this.getString(R.string.app_name);
			File mediaStorageDir = new File(
					Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
					appName);
			
			// 2. Create our subdirectory
			if (! mediaStorageDir.exists()) {
				if (! mediaStorageDir.mkdirs()) {
					Log.e(TAG, "Failed to create directory.");
					return null;
				}
			}
			
			// 3. Create a file name
			// 4. Create the file
			File mediaFile;
			Date now = new Date();
			String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(now);
			
			String path = mediaStorageDir.getPath() + File.separator;
			if (mediaType == MEDIA_TYPE_IMAGE) {
				mediaFile = new File(path + "IMG_" + timestamp + ".jpg");
			}
			else if (mediaType == MEDIA_TYPE_VIDEO) {
				mediaFile = new File(path + "VID_" + timestamp + ".mp4");
			}
			else {
				return null;
			}
			
			Log.d(TAG, "File: " + Uri.fromFile(mediaFile));
			
			// 5. Return the file's URI				
			return Uri.fromFile(mediaFile);
		}
		else {
			return null;
		}
	}
	public void saveToParse(View v){
//		final ParseObject testObject = new ParseObject("PopularSuggestions");
//		testObject.put("name", ((EditText)findViewById(R.id.addFriendEditText)).getText().toString());
//		testObject.saveInBackground(new SaveCallback(){
//
//			@Override
//			public void done(ParseException arg0) {
//				
//				
//			}
//			
//		});
		Toast.makeText(getApplicationContext(), "Under review!", Toast.LENGTH_SHORT).show();
	}
	public void giveError(View v){
		Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
	            "mailto","abc@gmail.com", null)); 
	emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Snap error"); 
	startActivity(Intent.createChooser(emailIntent, "Send email..."));
	}
	
	public void giveFeedback(View v){
		Uri uri = Uri.parse("market://details?id=" + getApplicationContext().getPackageName());
		Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
		try { 
		  startActivity(goToMarket);
		} catch (ActivityNotFoundException e) {
		  startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName())));
		} 
		
	}
	private void showDialog(){
		AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
		builder.setMessage("Please get internet connection and come back")
			.setTitle("Okay, will do")
			.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					finish();
				}
			} );
		AlertDialog dialog = builder.create();
		dialog.show();
	}
}
