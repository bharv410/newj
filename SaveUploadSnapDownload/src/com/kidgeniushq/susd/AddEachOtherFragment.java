package com.kidgeniushq.susd;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.kidgeniushq.susd.utility.MyApplication;
import com.mixpanel.android.mpmetrics.MixpanelAPI;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

public class AddEachOtherFragment extends ListFragment {
	ArrayList<String> people;
	ArrayAdapter<String> adapter;
	MixpanelAPI mMixpanel;
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mMixpanel=
				MixpanelAPI.getInstance(getActivity().getApplicationContext(), "5cbb4a097c852a733dd1836f865b082d");
		people = new ArrayList<String>();
		people.add("kwazs");
		people.add("boutmabenjamins");
		adapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_list_item_1, people);
		setListAdapter(adapter);
		
		
		ParseQuery<ParseObject> query = ParseQuery
				.getQuery("PopularSuggestions");
		query.orderByDescending("createdAt");
		FindCallback<ParseObject> fc =new FindCallback<ParseObject>() {
			@Override
			public void done(List<ParseObject> scoreList, ParseException e) {
				System.out.println("QUERYING  COMPLETE");
				if (e == null) {
					Log.d("score", "Retrieved " + scoreList.size() + " scores");
					for (int i = 0; i < scoreList.size(); i++) {
						people.add(scoreList.get(i).getString("name"));
					}setListAdapter(adapter);
				}
			}
		};
		query.findInBackground(fc);
		
		
		Button btn = (Button)getActivity().findViewById(R.id.addButton);
		btn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				
				
				if(((EditText)getActivity().findViewById(R.id.addFriendEditText)).getText().toString().equals("") || ((EditText)getActivity().findViewById(R.id.addFriendEditText)).getText()==null){
					Toast.makeText(getActivity().getApplicationContext(),
							"Enter your username for other people to see", Toast.LENGTH_SHORT).show();
				}else{
				final ParseObject testObject = new ParseObject("PopularSuggestions");
				testObject.put("name",
						((EditText) getActivity().findViewById(R.id.addFriendEditText)).getText()
								.toString());
				testObject.saveInBackground(new SaveCallback() {
					@Override
					public void done(ParseException arg0) {
						Toast.makeText(getActivity().getApplicationContext(),
								"Added to others list!", Toast.LENGTH_SHORT).show();
						Button btn = (Button) getActivity().findViewById(R.id.addButton);
						btn.setClickable(false);
						try{
				    	updateList(((EditText)getActivity().findViewById(R.id.addFriendEditText)).getText()
								.toString());
						}catch(Exception e){
							e.printStackTrace();
						}
					}
				});	
			}
			}
		});
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
		JSONObject props = new JSONObject();
		try {
			props.put("added friend", people.get(position));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		mMixpanel.track("AddedFriend", props);
		new AddFriendTask().execute(people.get(position));

	}
	
	public void updateList(String username){
		people.add(username);
		getListView().notifyAll();
		adapter.notifyDataSetChanged();
	}

	public class AddFriendTask extends AsyncTask<String, Void, Boolean> {
		@Override
		protected Boolean doInBackground(String... params) {
			return MyApplication.snapchat.addFriend(params[0]);
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if (result)
				Toast.makeText(getActivity(), "Added to friends!", Toast.LENGTH_SHORT)
						.show();
			else
				Toast.makeText(getActivity(), "error!", Toast.LENGTH_SHORT)
						.show();
		}
	}
}