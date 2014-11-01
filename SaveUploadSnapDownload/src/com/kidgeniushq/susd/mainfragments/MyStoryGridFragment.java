package com.kidgeniushq.susd.mainfragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;

import com.kidgeniushq.susd.R;
import com.kidgeniushq.susd.adapters.MyStoryAdapter;
import com.kidgeniushq.susd.utility.MyApplication;

public class MyStoryGridFragment extends Fragment {

    Boolean isDualPane;
    public GridView gridView;
    ListView listView;
    int position;
    MyStoryAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_allmystories, container, false);
        if(MyApplication.allMyStories!=null){
        	if(MyApplication.allMyStories.size()>0){
        		setStoryAdapter();
            }
        }
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(MyApplication.allMyStories!=null){
			if(gridView==null){
	    	setStoryAdapter();
		}
		}
    }
		
    public void setStoryAdapter(){
    	gridView = (GridView) getActivity().findViewById(R.id.gridview2);
    	mAdapter=new MyStoryAdapter(getActivity().getApplicationContext(),getActivity());
        gridView.setAdapter(mAdapter);
    }
    public void refreshGridView(){
    	gridView.invalidateViews();
    	mAdapter.notifyDataSetChanged();
    }
}