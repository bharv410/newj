package com.kidgeniushq.susd;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kidgeniushq.susd.adapters.UserAdapter;
import com.kidgeniushq.susd.utility.MyApplication;

public class RecipientsActivity extends Activity {

	public static final String TAG = RecipientsActivity.class.getSimpleName();
	ArrayList<String> recipients;
	protected Uri mMediaUri;
	protected String mFileType;
	protected GridView mGridView;

	// private AdView mAdView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.user_grid2);
		Toast.makeText(getApplicationContext(), "Captions & filters coming soon!!!", Toast.LENGTH_LONG).show();

		// ADS
		// Resources res = getResources();
		// boolean allowAds = res.getBoolean(R.bool.adRecipients);
		// if(allowAds){
		// mAdView = (AdView) findViewById(R.id.adView);
		// mAdView.loadAd(new AdRequest.Builder().build());
		// }
		// ADS

		mGridView = (GridView) findViewById(R.id.friendsGrid);
		mGridView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		mGridView.setOnItemClickListener(mOnItemClickListener);

		TextView emptyTextView = (TextView) findViewById(android.R.id.empty);
		mGridView.setEmptyView(emptyTextView);

		mMediaUri = getIntent().getData();
		mFileType = getIntent().getExtras().getString("fileType");
	}

	@Override
	public void onResume() {
		super.onResume();

		if (mGridView.getAdapter() == null) {
			UserAdapter adapter = new UserAdapter(RecipientsActivity.this);
			mGridView.setAdapter(adapter);
		} else {
			// ((UserAdapter)mGridView.getAdapter()).refill(MyAppl);
		}
	}

	protected ArrayList<String> getRecipientIds() {
		ArrayList<String> recipientIds = new ArrayList<String>();
		for (int i = 0; i < mGridView.getCount(); i++) {
			if (mGridView.isItemChecked(i)) {
				recipientIds.add(MyApplication.friendsNames.get(i).toString());
			}
		}
		return recipientIds;
	}

	public void sendOut(View v) {

		recipients = getRecipientIds();
		// if(recipients.contains("My Story"))
		// new UploadStoryAsyncTask().execute();

		Toast.makeText(getApplicationContext(), recipients.get(0),
				Toast.LENGTH_SHORT).show();
		Toast.makeText(getApplicationContext(), recipients.get(1),
				Toast.LENGTH_SHORT).show();
		Toast.makeText(getApplicationContext(), recipients.get(2),
				Toast.LENGTH_SHORT).show();
		Toast.makeText(getApplicationContext(), recipients.get(3),
				Toast.LENGTH_SHORT).show();
		Toast.makeText(getApplicationContext(), recipients.get(4),
				Toast.LENGTH_SHORT).show();
		Toast.makeText(getApplicationContext(), recipients.get(5),
				Toast.LENGTH_SHORT).show();
	}

	public class UploadStoryAsyncTask extends AsyncTask<String, Void, Boolean> {
		@Override
		protected Boolean doInBackground(String... params) {
			boolean video = (getIntent().getStringExtra("fileType")
					.contains(".mp4"));
			boolean result = MyApplication.snapchat.sendStory(new File(
					mMediaUri.getPath()), video, 8, "");
			return result;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if (result) {
				Toast tst = Toast.makeText(getApplicationContext(),
						"Succesfully Uploaded to Story", Toast.LENGTH_SHORT);
				tst.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
				tst.show();
			} else {
				Toast tst = Toast
						.makeText(
								getApplicationContext(),
								"Sorry. .JPG files don't work at the momeny. Use a .PNG file",
								Toast.LENGTH_SHORT);
				tst.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
				tst.show();
			}
		}
	}

	public class SendToFriendsAsyncTask extends
			AsyncTask<String, Void, Boolean> {
		@Override
		protected Boolean doInBackground(String... params) {
			boolean story;
			if(recipients.contains("My Story")){
				recipients.remove(0);
				story=true;
			}else{
				story=false;
			}
			boolean video = (getIntent().getStringExtra("fileType")
					.contains(".mp4"));
			
			return MyApplication.snapchat.sendSnap(new File(mMediaUri.getPath()), recipients, video, story,10);
			
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if (result) {
				Toast tst = Toast.makeText(getApplicationContext(),
						"Succesfully Uploaded to Story", Toast.LENGTH_SHORT);
				tst.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
				tst.show();
			} else {
				Toast tst = Toast
						.makeText(
								getApplicationContext(),
								"Sorry. .JPG files don't work at the momeny. Use a .PNG file",
								Toast.LENGTH_SHORT);
				tst.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
				tst.show();
			}
		}
	}

	protected OnItemClickListener mOnItemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {

			ImageView checkImageView = (ImageView) view
					.findViewById(R.id.checkImageView);

			if (mGridView.isItemChecked(position)) {
				// add the recipient
				checkImageView.setVisibility(View.VISIBLE);
			} else {
				// remove the recipient
				checkImageView.setVisibility(View.INVISIBLE);
			}
		}
	};

}
