package com.kidgeniushq.susd;

import java.util.ArrayList;
import java.util.List;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.kidgeniushq.susd.utility.MyApplication;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class AddFriendsFragment extends ListFragment {
	ArrayList<String> populars;
	ArrayAdapter<String> adapter;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		populars = new ArrayList<String>();
		populars.add("VSpink");
		populars.add("RyanSeacrest");
		populars.add("mtv");
		populars.add("easports");
		populars.add("grubhub");
		populars.add("tacobell");
		populars.add("mcdonalds");
		populars.add("nba");
		populars.add("boutmabenjamins");
		adapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_list_item_1, populars);
		setListAdapter(adapter);
//		ParseQuery<ParseObject> query = ParseQuery
//				.getQuery("PopularSuggestions");
//		query.orderByDescending("createdAt");
//		FindCallback<ParseObject> fc =new FindCallback<ParseObject>() {
//			@Override
//			public void done(List<ParseObject> scoreList, ParseException e) {
//				System.out.println("QUERYING  COMPLETE");
//				if (e == null) {
//					Log.d("score", "Retrieved " + scoreList.size() + " scores");
//					for (int i = 0; i < scoreList.size(); i++) {
//						populars.add(scoreList.get(i).getString("name"));
//					}setListAdapter(adapter);
//				}
//			}
//		};
//		query.findInBackground(fc);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_add_friends,
				container, false);
		return rootView;
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		new AddFriendTask().execute(populars.get(position));

	}

	public class AddFriendTask extends AsyncTask<String, Void, Boolean> {
		@Override
		protected Boolean doInBackground(String... params) {
			return MyApplication.snapchat.addFriend(params[0]);
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if (result)
				Toast.makeText(getActivity(), "Success!", Toast.LENGTH_SHORT)
						.show();
			else
				Toast.makeText(getActivity(), "error!", Toast.LENGTH_SHORT)
						.show();
		}
	}
}