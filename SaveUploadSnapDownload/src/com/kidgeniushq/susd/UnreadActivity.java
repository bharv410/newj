package com.kidgeniushq.susd;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.kidgeniushq.susd.utility.MyApplication;

public class UnreadActivity extends ListActivity {
ArrayAdapter<String> adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_unread);
		adapter= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,MyApplication.unreadSenders);
		setListAdapter(adapter);
	}
	
		@Override 
		protected void onListItemClick (ListView l, View v, int position, long id) {
		    Toast.makeText(this, "Clicked row " + position, Toast.LENGTH_SHORT).show();
		    MyApplication.currentBitmap=MyApplication.myUnreads.get(MyApplication.unreadSenders.get(position));
		startActivity(new Intent(this,BigView.class));
	}
}
