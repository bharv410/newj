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

public class MyStoryGridFragment extends Fragment {

    Boolean isDualPane;
    GridView gridView;
    ListView listView;
    int position;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_feed, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
		
    public void setStoryAdapter(){
    	gridView = (GridView) getActivity().findViewById(R.id.gridview);
        gridView.setAdapter(new MyStoryAdapter(getActivity().getApplicationContext()));
    }
}