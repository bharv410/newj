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
import com.kidgeniushq.susd.utility.Utility;

public class MyStoryAdapter extends BaseAdapter {

    private Context mContext;
    private Activity mActivity;

    public MyStoryAdapter(Context c, Activity act) {
        mContext = c;
        mActivity=act;
    }

    @Override
    public int getCount() {
        return MyApplication.allMyStories.size();
    }

    @Override
    public Object getItem(int arg0) {
    	return MyApplication.allMyStories.get(arg0);
    }

    @Override
    public long getItemId(int position) {
        return ((long)MyApplication.allMyStories.get(position).hashCode());
    }
    @Override
	public View getView(int i, View view, ViewGroup viewGroup) {
		try {
			View v = view;
			TextView name;
			ImageView picture;

			if (v == null) {
				v = LayoutInflater.from(mContext).inflate(R.layout.gridview_item, viewGroup,
						false);
				v.setTag(R.id.picture, v.findViewById(R.id.picture));
				v.setTag(R.id.text, v.findViewById(R.id.text));
			}
			picture = (ImageView) v.getTag(R.id.picture);
			name = (TextView) v.getTag(R.id.text);
			name.setText(MyApplication.allMyStories.get(i).toString());
			
			DisplayMetrics dimension = new DisplayMetrics();
			mActivity.getWindowManager().getDefaultDisplay()
					.getMetrics(dimension);
			int width = dimension.widthPixels;
			
			picture.setImageBitmap(Bitmap.createScaledBitmap(Utility.getPhoto(
					MyApplication.allMyStories.get(i).getData()), width / 2, 3 * width / 4, true));
			System.out.println("Adding mystory");

			return v;
		} catch (Exception e) {
			e.printStackTrace();
			return view;
		}
	}
}