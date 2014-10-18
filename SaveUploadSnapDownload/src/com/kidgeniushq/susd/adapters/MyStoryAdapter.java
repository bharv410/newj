package com.kidgeniushq.susd.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kidgeniushq.susd.MainActivity;
import com.kidgeniushq.susd.R;
import com.kidgeniushq.susd.utility.Utility;

public class MyStoryAdapter extends BaseAdapter {

    private Context mContext;

    public MyStoryAdapter(Context c) {
        mContext = c;
    }

    @Override
    public int getCount() {
        return MainActivity.allMyStories.size();
    }

    @Override
    public Object getItem(int arg0) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
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
			name.setText(MainActivity.allMyStories.get(i).toString());
			picture.setImageBitmap(Utility.getPhoto(MainActivity.allMyStories.get(i).getData()));
			System.out.println("Adding mystory");

			return v;
		} catch (Exception e) {
			e.printStackTrace();
			return view;
		}
	}
}