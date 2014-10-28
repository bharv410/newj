package com.kidgeniushq.susd;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.kidgeniushq.susd.customui.NotSwipeableViewPager;
import com.kidgeniushq.susd.utility.Utility;

@TargetApi(Build.VERSION_CODES.HONEYCOMB) public class SendImageActivity extends FragmentActivity {

	SectionsPagerAdapter mSectionsPagerAdapter;
	NotSwipeableViewPager mViewPager;
	Uri currImageURI;
	Bitmap currentBitmap;
	public static File currentImageFile;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_send_image);
		getActionBar().hide();
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());
		mViewPager = (NotSwipeableViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(Intent.createChooser(intent, "Select Picture"),
				1);

	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			// no error
			if (requestCode == 1) {
				// request code 1 means its from the upload image to story
				currImageURI = data.getData();
				InputStream iStream;
				try {
					iStream = getContentResolver()
							.openInputStream(currImageURI);
					byte[] inputData = Utility.getBytes(iStream);
					currentBitmap = Utility.getPhoto(inputData);
					ImageView imageViewForChosenPic = (ImageView) findViewById(R.id.uploadImageView);
					imageViewForChosenPic.setImageBitmap(currentBitmap);
					String filename = "/image";
					currentImageFile = new File(getFilesDir() + filename);
					FileOutputStream outputStream = new FileOutputStream(currentImageFile);
					outputStream.write(inputData);
					outputStream.close();
					//writes to file
					
					//get path so i can read the extensions
					//DOES NOT DO ANYTHING WITH IT YET
					String[] filePathColumn = { MediaStore.Images.Media.DATA };
		            Cursor cursor = getContentResolver().query(currImageURI,
		                    filePathColumn, null, null, null);
		            cursor.moveToFirst();
		            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
		            String picturePath = cursor.getString(columnIndex);
		            cursor.close();
		            Log.i("Image path", "" + picturePath);
		            
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void setAsStory(View v){
		Toast.makeText(getApplicationContext(), "Rate 5 stars & this feature will be unlocked by end of week!", Toast.LENGTH_SHORT).show();
	}
	
	public void sendTo(View v){
		Toast tst=Toast.makeText(getApplicationContext(),
				"Sorry. Can't send to friends yet", Toast.LENGTH_SHORT);
		tst.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);
		tst.show();
	}
	
	
	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a PlaceholderFragment (defined as a static inner class
			// below).
			return PlaceholderFragment.newInstance(position + 1);
		}
		@Override
		public int getCount() {
			// Show 3 total pages.
			return 3;
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
			View rootView = inflater.inflate(R.layout.fragment_send_image,
					container, false);
			return rootView;
		}
	}
	
	public void setBigText(View v){
		ImageView imageViewForChosenPic = (ImageView) findViewById(R.id.uploadImageView);
		try{
		imageViewForChosenPic.setImageBitmap(drawBigTextToBitmap(getApplicationContext(),currentBitmap,((EditText)findViewById(R.id.imageCaptionEditText)).getText().toString()));
		}catch (Exception e){
			e.printStackTrace();
		}
		}
	public void setSmallText(View v){
		ImageView imageViewForChosenPic = (ImageView) findViewById(R.id.uploadImageView);
		try{
		imageViewForChosenPic.setImageBitmap(drawSmallTextToBitmap(getApplicationContext(),currentBitmap,((EditText)findViewById(R.id.imageCaptionEditText)).getText().toString()));
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	public Bitmap drawSmallTextToBitmap(Context gContext, 
			  Bitmap bm, 
			  String gText) {
			  Resources resources = gContext.getResources();
			  float scale = resources.getDisplayMetrics().density;
			  android.graphics.Bitmap.Config bitmapConfig =
			      bm.getConfig();
			  if(bitmapConfig == null) {
			    bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;
			  }
			  Bitmap newBm = bm.copy(bitmapConfig, true);
			  Canvas canvas = new Canvas(newBm);
			  
			  
			  //set caption text
			  Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
			  paint.setColor(Color.WHITE);
			  paint.setTextSize((int) (24 * scale));
			  paint.setShadowLayer(1f, 0f, 1f, Color.WHITE);
			  Rect bounds = new Rect();
			  paint.getTextBounds(gText, 0, gText.length(), bounds);
			  int x = (newBm.getWidth() - bounds.width())/2;
			  int y = (newBm.getHeight() + bounds.height())/2;
			  
			  
			//set background
			  int screenWidth = getApplicationContext().getResources().getDisplayMetrics().widthPixels;
			  Rect greyBack = new Rect(0,newBm.getHeight()/2 + bounds.height(),screenWidth,newBm.getHeight()/2 - bounds.height());
			  Paint paintBG = new Paint();
			  paintBG.setARGB(128, 100, 100, 100); //added alpha because Snapchat has translucent //grey background
			  canvas.drawRect(greyBack, paintBG);
			  
			  
			  
			  canvas.drawText(gText, x, y, paint);
			  
			
			  
			  return newBm;
			}
	public Bitmap drawBigTextToBitmap(Context gContext, 
			  Bitmap bm, 
			  String gText) {
			  Resources resources = gContext.getResources();
			  float scale = resources.getDisplayMetrics().density;
			  android.graphics.Bitmap.Config bitmapConfig =
			      bm.getConfig();
			  if(bitmapConfig == null) {
			    bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;
			  }
			  Bitmap newBm = bm.copy(bitmapConfig, true);
			  Canvas canvas = new Canvas(newBm);
			  
			  
			  //set caption text
			  Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
			  paint.setColor(Color.WHITE);
			  paint.setTextSize((int) (44 * scale));
			  paint.setShadowLayer(1f, 0f, 1f, Color.WHITE);
			  Rect bounds = new Rect();
			  paint.getTextBounds(gText, 0, gText.length(), bounds);
			  int x = (newBm.getWidth() - bounds.width())/2;
			  int y = (newBm.getHeight() + bounds.height())/2;
			  
			  canvas.drawText(gText, x, y, paint);
			  
			  return newBm;
			}

}
