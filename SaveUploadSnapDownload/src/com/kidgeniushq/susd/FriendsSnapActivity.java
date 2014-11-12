package com.kidgeniushq.susd;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.habosa.javasnap.Snapchat;
import com.habosa.javasnap.Story;
import com.kidgeniushq.susd.utility.MyApplication;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;

public class FriendsSnapActivity extends Activity {
	MyAdapter adapter;
	GridView gridView;
	ProgressBar friendProgressBar;
	ArrayList<Story> friendsStorys;
	ArrayList<String> feedFilePaths;
	DisplayImageOptions options;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friends_snap);
		int currentAPIVersion = android.os.Build.VERSION.SDK_INT;
		if (currentAPIVersion >= android.os.Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().hide();
		}
		feedFilePaths=new ArrayList<String>();
		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.avatar_empty)
		.showImageForEmptyUri(R.drawable.avatar_empty)
		.showImageOnFail(R.drawable.avatar_empty)
		.cacheInMemory(true) 
		.cacheOnDisk(true) 
		.considerExifParams(true) 
		.bitmapConfig(Bitmap.Config.RGB_565)
		.build(); 

		gridView = (GridView) findViewById(R.id.gridview2);
		LoadStories ls = new LoadStories();
		ls.execute();

		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				if (friendsStorys.get(position).isImage()) {
					MyApplication.currentBitmap=ImageLoader.getInstance().loadImageSync("file:///"+feedFilePaths.get(position), options);
					Intent intnr = new Intent(getApplicationContext(),
							BigView.class);
					startActivity(intnr);
				}
			}
		});
		friendProgressBar = (ProgressBar) findViewById(R.id.friendsProgressBar);
		friendProgressBar.setVisibility(ProgressBar.VISIBLE);
	}

	public void goToFriendsList(View v) {
		Intent i = new Intent(this, SearchFriendsActivity.class);
		startActivity(i);
	}

	private class MyAdapter extends BaseAdapter {
		private LayoutInflater inflater;

		public MyAdapter(Context context) {
			inflater = LayoutInflater.from(context);

		}

		@Override
		public int getCount() {
			return feedFilePaths.size();
		}

		@Override
		public Object getItem(int i) {
			return feedFilePaths.get(i);
		}

		@Override
		public long getItemId(int i) {
			return (long) (feedFilePaths.get(i).hashCode());
		}

		@Override
		public View getView(int i, View view, ViewGroup viewGroup) {
			try {
				View v = view;
				ImageView picture;
				TextView name;

				if (v == null) {
					v = inflater.inflate(R.layout.gridview_item, viewGroup,
							false);
					v.setTag(R.id.picture, v.findViewById(R.id.picture));
					v.setTag(R.id.text, v.findViewById(R.id.text));
				}
				picture = (ImageView) v.getTag(R.id.picture);
				name = (TextView) v.getTag(R.id.text);
				name.setText(friendsStorys.get(i).getSender());
				
				DisplayMetrics dimension = new DisplayMetrics();
				getWindowManager().getDefaultDisplay().getMetrics(dimension);
				int width = dimension.widthPixels;

				ImageSize targetSize = new ImageSize(width/2, 2 * width / 3);
				Bitmap bm=ImageLoader.getInstance().loadImageSync("file:///"+feedFilePaths.get(i), targetSize, options);
				picture.setImageBitmap(Bitmap.createScaledBitmap(bm, width / 2, 3 * width / 4, true));

				return v;
			} catch (Exception e) {
				return null;
			}

		}
	}

	// -------------------------------------------------------------------------------
	// -------------------------------------------------------------------------------
	// -------------------------------------------------------------------------------
	// -------------------------------------------------------------------------------
	// --------friendsStorys-----------------------------------------------------------------------
	// -------------------------------------------------------------------------------

	private class LoadStories extends AsyncTask<Integer, Integer, String> {
		@Override
		protected String doInBackground(Integer... index) {

			friendsStorys = new ArrayList<Story>();
			// get storys

			for (Story s : MyApplication.stories) {
				if (s.getSender().contains(MyApplication.currentFriend)
						&& s.isImage()) {
					byte[] snapBytes = Snapchat.getStory(s);
					String root = Environment.getExternalStorageDirectory()
							.toString();
					File myDir = new File(root + "/" + s.getSender());
					myDir.mkdirs();

					String fname = "Image" + s.getId() + ".jpg";
					File file = new File(myDir, fname);
					if (file.exists())
						file.delete();
					try {
						FileOutputStream out = new FileOutputStream(file);
						out.write(snapBytes, 0, snapBytes.length);
						out.flush();
						out.close();
						feedFilePaths.add(file.getAbsolutePath());
					} catch (Exception e) {
						e.printStackTrace();
					}
					friendsStorys.add(s);
				}

			}
			return null;
		}

		@Override
		public void onProgressUpdate(Integer... args) {
		}

		@Override
		protected void onPostExecute(String result) {
			friendProgressBar.setVisibility(ProgressBar.GONE);
			if(feedFilePaths.size()<1){
				Toast.makeText(getApplicationContext(), MyApplication.currentFriend+" has no story pics", Toast.LENGTH_LONG).show();
			finish();
			}else{
			
			adapter = new MyAdapter(getApplicationContext());
			gridView.setAdapter(adapter);
			}
		}
	}

	public void back(View v) {
		finish();
	}
}