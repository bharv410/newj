package com.kidgeniushq.susd.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kidgeniushq.susd.R;
import com.kidgeniushq.susd.utility.MyApplication;

public class SnapAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	Context context;
	Activity activity;
	

	public SnapAdapter(Context ctx, Activity act) {
		inflater = LayoutInflater.from(ctx);
		context = ctx;
		activity = act;
	}

	@Override
	public int getCount() {
		//set this as the limit so i can hack avoid problm
		return MyApplication.imageList.size();
	}

	@Override
	public Object getItem(int i) {
		return MyApplication.stories[i];
	}

	@Override
	public long getItemId(int i) {
		return (long) (MyApplication.stories[i].hashCode());
	}

	@Override
	public View getView(int i, View view, ViewGroup viewGroup) {
		try {
			View v = view;
			TextView name;
			ImageView picture;

			if (v == null) {
				v = inflater.inflate(R.layout.gridview_item, viewGroup,
						false);
				v.setTag(R.id.picture, v.findViewById(R.id.picture));
				v.setTag(R.id.text, v.findViewById(R.id.text));
			}
			picture = (ImageView) v.getTag(R.id.picture);
			name = (TextView) v.getTag(R.id.text);
			name.setText(MyApplication.stories[i].getSender());
			DisplayMetrics dimension = new DisplayMetrics();
			activity.getWindowManager().getDefaultDisplay()
					.getMetrics(dimension);
			int width = dimension.widthPixels;

			try{
							picture.setImageBitmap(Bitmap.createScaledBitmap(
									MyApplication.imageList.get(i), width / 2, 3 * width / 4, true));
			}catch(Exception e){
				
			}
			return v;
		} catch (Exception e) {
			e.printStackTrace();
			return view;
		}
	}
}