package com.kidgeniushq.susd.mainfragments;

import java.util.ArrayList;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.kidgeniushq.susd.R;
import com.kidgeniushq.susd.R.layout;
import com.kidgeniushq.susd.utility.MyApplication;

public class AddPopularFragment extends ListFragment {
	ArrayList<String> populars;
	ArrayAdapter<String> adapter;
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		populars = new ArrayList<String>();
		populars.add("VSpink");
		populars.add("Katyperry");
		populars.add("mileyxxcyrus");
		populars.add("RyanSeacrest");
		populars.add("canadiangoose");
		populars.add("kimkardash");
		populars.add("badgalrihanna");
		populars.add("h.estyles");
		populars.add("areanuh");
		populars.add("JGL1981");
		populars.add("boutmabenjamins");
		populars.add("meekmillyMMG");
		populars.add("GreeneyGirl");
		populars.add("LivelyBK");
		populars.add("leto30s2m");
		populars.add("kendalljenner");
		populars.add("Chamclouder");
		populars.add("wesandkeats");
		populars.add("kumbayachadwick");
		populars.add("jmillermusic");
		populars.add("imchristianlnrd");
		populars.add("craejepsen");
		populars.add("camerondallaaas");
		populars.add("loganpaul");
		populars.add("zacefron87");
		populars.add("themrwest");
		populars.add("ChrisMBrown89");
		populars.add("R.Patterson86");
		populars.add("JackReynor");
		populars.add("mtv");
		populars.add("easports");
		populars.add("grubhub");
		populars.add("tacobell");
		populars.add("mcdonalds");
		populars.add("nba");
		adapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_list_item_1, populars);
		setListAdapter(adapter);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.popular_fragment,
				container, false);
		return rootView;
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		
		if(!MyApplication.friendsNames.contains(populars.get(position)))
			new AddFriendTask().execute(populars.get(position));
		else
			Toast.makeText(getActivity(), "Already added", Toast.LENGTH_LONG).show();

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