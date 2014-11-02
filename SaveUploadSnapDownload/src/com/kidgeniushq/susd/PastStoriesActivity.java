package com.kidgeniushq.susd;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

import com.kidgeniushq.susd.adapters.MyStoryAdapter;
import com.kidgeniushq.susd.utility.MyApplication;
import com.kidgeniushq.susd.utility.Utility;

public class PastStoriesActivity extends Activity {
	 Boolean isDualPane;
	    public GridView gridView;
	    int position;
	    public MyStoryAdapter mAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_past_stories);
		
		
		int currentAPIVersion = android.os.Build.VERSION.SDK_INT;
		if (currentAPIVersion >= android.os.Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().hide();
		}
if(MyApplication.allMyStories.size()>0){
		setStoryAdapter();
}else{
	Toast.makeText(getApplicationContext(), "This is for your stories", Toast.LENGTH_SHORT).show();
}
	}
	
	public void setStoryAdapter(){
    	gridView = (GridView) findViewById(R.id.gridview2);
    	mAdapter=new MyStoryAdapter(getApplicationContext(),this);
        gridView.setAdapter(mAdapter);
        gridView.setOnItemClickListener(new OnItemClickListener() { 
            @Override 
            public void onItemClick(AdapterView<?> parent, View v,
                    int position, long id) {
            	MyApplication.currentBitmap=Utility.getPhoto(MyApplication.allMyStories.get(position).getData());
            	startActivity(new Intent(PastStoriesActivity.this,BigView.class));
            	} 
        });
    }
}
