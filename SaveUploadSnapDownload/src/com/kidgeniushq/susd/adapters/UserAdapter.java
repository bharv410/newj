package com.kidgeniushq.susd.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.kidgeniushq.susd.R;
import com.kidgeniushq.susd.utility.MyApplication;

public class UserAdapter extends ArrayAdapter<String> {
	
	protected Context mContext;
	
	public UserAdapter(Context context) {
		super(context, R.layout.message_item, MyApplication.friendsNames);
		mContext = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.user_item, null);
			holder = new ViewHolder();
			holder.nameLabel = (TextView)convertView.findViewById(R.id.nameLabel);
			holder.checkImageView = (ImageView)convertView.findViewById(R.id.checkImageView);
			convertView.setTag(holder);
		}
		else {
			holder = (ViewHolder)convertView.getTag();
		}
		
		holder.nameLabel.setText(MyApplication.friendsNames.get(position));
		
		GridView gridView = (GridView)parent;
		if (gridView.isItemChecked(position)) {
			holder.checkImageView.setVisibility(View.VISIBLE);
		}
		else {
			holder.checkImageView.setVisibility(View.INVISIBLE);
		}
		
		return convertView;
	}
	
	private static class ViewHolder {
		ImageView checkImageView;
		TextView nameLabel;
	}
	
	public void refill(List<String> users) {
		MyApplication.friendsNames.clear();
		MyApplication.friendsNames.addAll(users);
		notifyDataSetChanged();
	}
}






