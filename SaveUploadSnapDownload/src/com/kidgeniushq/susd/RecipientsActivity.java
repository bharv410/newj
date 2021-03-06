package com.kidgeniushq.susd;

import java.io.File;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
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
import com.parse.ParseAnalytics;

public class RecipientsActivity extends Activity {

	public static final String TAG = RecipientsActivity.class.getSimpleName();
	ArrayList<String> recipients;
	protected Uri mMediaUri;
	protected String mFileType;
	protected GridView mGridView;
	// private AdView mAdView;
	ProgressDialog progress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.user_grid2);
		int currentAPIVersion = android.os.Build.VERSION.SDK_INT;
		if (currentAPIVersion >= android.os.Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().hide();
		}
		mGridView = (GridView) findViewById(R.id.friendsGrid);
		mGridView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		mGridView.setOnItemClickListener(mOnItemClickListener);

		TextView emptyTextView = (TextView) findViewById(android.R.id.empty);
		mGridView.setEmptyView(emptyTextView);
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
			
			new SendToFriendsAsyncTask().execute();
			progress = ProgressDialog.show(this, "Sending...",
					"wait just a quick second", true);
		}
	public class SendToFriendsAsyncTask extends
			AsyncTask<String, Void, Boolean> {
		@Override
		protected Boolean doInBackground(String... params) {
			boolean story;
			if (recipients.contains("My Story")) {
				recipients.remove(0);
				story = true;
			} else {
				story = false;
			}

			
			if(getIntent().getStringExtra("type").equals("vid")){
				return MyApplication.snapchat.sendSnap(new File(MyApplication.currentFileSendPath), recipients, true,
						story, 10);
			}else{
				Map<String, String> dimensions = new HashMap<String, String>();
				dimensions.put("saved", MyApplication.username);
				ParseAnalytics.trackEvent("PHOTOSENT", dimensions);
				return MyApplication.snapchat.sendSnap(new File(getFilesDir(),"uploadimage.jpeg"), recipients, false,
						story, 10);
				
			}
		}

		@Override
		protected void onPostExecute(Boolean result) {
			progress.dismiss();
			if (result) {
				Toast tst = Toast.makeText(getApplicationContext(), "Sent",
						Toast.LENGTH_SHORT);
				tst.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
				tst.show();
				finish();
			} else {
				//didnt work. try converting jpg to png
				Toast tst = Toast
						.makeText(
								getApplicationContext(),
								"Error sending snap",
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
