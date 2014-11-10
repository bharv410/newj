package com.kidgeniushq.susd;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.kidgeniushq.susd.utility.MyApplication;

public class SearchFriendsActivity extends ListActivity {
	ArrayAdapter<String> adapter;
	EditText inputSearch;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_friends);
		int currentAPIVersion = android.os.Build.VERSION.SDK_INT;
		if (currentAPIVersion >= android.os.Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().hide();
		}
		inputSearch = (EditText) findViewById(R.id.searchEditText);

		adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,
				MyApplication.friendsNames);
		setListAdapter(adapter);

		inputSearch.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence cs, int arg1, int arg2,
					int arg3) {
				SearchFriendsActivity.this.adapter.getFilter().filter(cs);
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
			}

			@Override
			public void afterTextChanged(Editable arg0) {
			}
		});
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		String userNameClicked = (String) getListAdapter().getItem(position);
				Intent i = new Intent(this, FriendsSnapActivity.class);
				MyApplication.currentFriend=userNameClicked;
				startActivity(i);
	}

	public void scrollDown(View v){
		getListView().smoothScrollToPosition(MyApplication.friendsNames.size()-1);
	}

	public void goToTop(View v) {
		getListView().smoothScrollToPosition(0);
	}
}