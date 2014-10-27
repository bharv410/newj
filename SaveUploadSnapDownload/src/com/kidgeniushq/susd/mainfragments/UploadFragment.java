package com.kidgeniushq.susd.mainfragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.kidgeniushq.susd.R;


public class UploadFragment extends Fragment {
	ImageView picture;
	EditText captionEditText;
	public UploadFragment() {
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_upload, container,
				false);
//		picture=(ImageView)rootView.findViewById(R.id.uploadImageView);
//		captionEditText=(EditText)rootView.findViewById(R.id.captionEditText);
		return rootView;
	}
}