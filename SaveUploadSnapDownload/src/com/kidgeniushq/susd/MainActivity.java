package com.kidgeniushq.susd;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.habosa.javasnap.Snapchat;
import com.habosa.javasnap.Story;
import com.kidgeniushq.susd.asynctasks.LoginAsyncTask;
import com.kidgeniushq.susd.asynctasks.UploadStoryAsyncTask;
import com.kidgeniushq.susd.mainfragments.FeedFragment;
import com.kidgeniushq.susd.mainfragments.MyStoryGridFragment;
import com.kidgeniushq.susd.mainfragments.UploadFragment;
import com.kidgeniushq.susd.model.MyStory;
import com.kidgeniushq.susd.utility.MyStorysAlarmReciever;
import com.kidgeniushq.susd.utility.Utility;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

//icon by samuel green
public class MainActivity extends FragmentActivity {
	
	MyStorysAlarmReciever alarm = new MyStorysAlarmReciever();
	byte[] snapData=null;
	SectionsPagerAdapter mSectionsPagerAdapter;
	ViewPager mViewPager;
	public static String username, password;
	public static ArrayList<MyStory> allMyStories;
	public static Snapchat snapchat;
	Uri currImageURI;
	Bitmap currentBitmap;
	public static Story[] stories;
	//public static List<byte[]> storyBytes;
	public static Boolean loadingStoriesToImage=false;
	//public static int snapImagesOnScreen=0;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//getMyStories();
		//alarm.setAlarm(getApplicationContext());

		login();

		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
	}
	private void getMyStories(){
		System.out.println(" here1");
		MainActivity.allMyStories = new ArrayList<MyStory>();
		ParseQuery<ParseObject> query = ParseQuery.getQuery("TestSnaps");
		query.orderByDescending("createdAt");
		query.setLimit(6);
		query.findInBackground(new FindCallback<ParseObject>() {
			@Override
			public void done(List<ParseObject> scoreList, ParseException e) {
				System.out.println(" here2");
				if (e == null) {
					
					Log.d("score", "Retrieved " + scoreList.size() + " scores");
					for (ParseObject snap : scoreList) {
						System.out.println(snap.getInt("viewcount"));
						 int vc=snap.getInt("viewcount");
						 String id=snap.getString("snapid");
						 String date=snap.getString("date");
						ParseFile actualSnap = snap.getParseFile("story");
						actualSnap.getDataInBackground(new GetDataCallback() {
							public void done(byte[] data, ParseException e) {
								System.out.println(" here2");
								if (e == null) {
									snapData=data;
								}
							}
						});
						//see mystory constructor
						MainActivity.allMyStories.add(new MyStory(vc,id,date,snapData));
						System.out.println(" " +vc);
						System.out.println(id);
					}
					System.out.println("DONE");
					System.out.println("DONE");
					System.out.println("DONE");
					//get the mystorys frag object so i can call a method
					MyStoryGridFragment mystorysfrag = (MyStoryGridFragment)getSupportFragmentManager().findFragmentByTag(
		                       "android:switcher:"+R.id.pager+":0");
					mystorysfrag.setStoryAdapter();
				 }
			}	
			});
	}
	private void login() {
		username = "boutmabenjamins";
		password = "iforgot05";
		LoginAsyncTask lat = new LoginAsyncTask(
				getApplicationContext(), MainActivity.this);
		lat.execute(username, password);
		
//		AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
//
//		alertDialog.setTitle("Login");
//		final EditText quantity = new EditText(MainActivity.this);
//		final EditText lot = new EditText(MainActivity.this);
//
//		quantity.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
//		quantity.setHint("username");
//		lot.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
//		lot.setHint("password");
//
//		LinearLayout ll = new LinearLayout(this);
//		ll.setOrientation(LinearLayout.VERTICAL);
//		ll.addView(quantity);
//		ll.addView(lot);
//		alertDialog.setView(ll);
//
//		alertDialog.setCancelable(false);
//		alertDialog.setPositiveButton("Login",
//				new DialogInterface.OnClickListener() {
//					public void onClick(DialogInterface dialog, int id) {
//						username = quantity.getText().toString();
//						password = lot.getText().toString();
//						LoginAsyncTask lat = new LoginAsyncTask(
//								getApplicationContext(), MainActivity.this);
//						lat.execute(username, password);
//					}
//				});
//		AlertDialog alert = alertDialog.create();
//		alert.show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			if(position==2)
				return new MyStoryGridFragment();
			
			
			if (position == 1)
				return new UploadFragment();
			else if(position ==0)
				return new FeedFragment();
			else
				return PlaceholderFragment.newInstance(position + 1);
			
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 5;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_section1).toUpperCase(l);
			case 1:
				return getString(R.string.title_section2).toUpperCase(l);
			case 2:
				return getString(R.string.title_section3).toUpperCase(l);
			}
			return null;
		}
	}

	
	public static class PlaceholderFragment extends Fragment {
		private static final String ARG_SECTION_NUMBER = "section_number";

		public static PlaceholderFragment newInstance(int sectionNumber) {
			PlaceholderFragment fragment = new PlaceholderFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}

	public void uploadImageToStory(View v) {
		//set listener for the caption
		 final EditText txtEdit= (EditText) findViewById(R.id.captionEditText);
		 txtEdit.setOnFocusChangeListener(new OnFocusChangeListener() {          
		        public void onFocusChange(View v, boolean hasFocus) {
		            if(!hasFocus)
		            	currentBitmap=Utility.drawTextToBitmap(getApplicationContext(), 
		        				currentBitmap, 
		        				  txtEdit.getText().toString()); 
		        }
		    });
		
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(
				Intent.createChooser(intent,
						"Select Picture"), 1);
	}

	public void uploadVideoToStory(View v) {
		Toast.makeText(getApplicationContext(), "Not available yet", Toast.LENGTH_SHORT).show();
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			//no error
			if (requestCode == 1) {
				//request code 1 means its from the upload image to story
				currImageURI = data.getData();
				InputStream iStream;
				try {
					iStream = getContentResolver()
							.openInputStream(currImageURI);
					byte[] inputData = Utility.getBytes(iStream);
					currentBitmap = Utility.getPhoto(inputData);
					ImageView imageViewForChosenPic = (ImageView)findViewById(R.id.uploadImageView);
					imageViewForChosenPic.setImageBitmap(currentBitmap);
					String filename = "/image";
					File f = new File(getFilesDir() + filename);
					FileOutputStream outputStream = new FileOutputStream(f);
					outputStream.write(inputData);
					outputStream.close();
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void sendItOut(View v){
		ImageView imageViewForChosenPic = (ImageView)findViewById(R.id.uploadImageView);		
		imageViewForChosenPic.setImageBitmap(currentBitmap);
		
		UploadStoryAsyncTask us = new UploadStoryAsyncTask(getApplicationContext(), this, false);
		us.execute();
	}
}
