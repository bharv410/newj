package com.kidgeniushq.susd.mainfragments;
//
//import java.io.FileNotFoundException;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.List;
//
//import android.content.Context;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemClickListener;
//import android.widget.ArrayAdapter;
//import android.widget.CheckBox;
//import android.widget.ListView;
//import android.widget.SeekBar;
//import android.widget.SeekBar.OnSeekBarChangeListener;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.habosa.javasnap.Friend;
//import com.habosa.javasnap.Snapchat;
//import com.kidgeniushq.susd.R;
//import com.kidgeniushq.susd.SendImageActivity;
//import com.kidgeniushq.susd.utility.MyApplication;
//
//public class SendToFriendsFragment extends Fragment implements OnSeekBarChangeListener{
//	private SeekBar seekbar; 
//	private TextView textProgress;
//	Boolean sentOrNah;
//	List<String> recipients;
//	int viewTime;
//	HashMap<String,Integer> friendIndex;
//	MyCustomAdapter dataAdapter;
//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container,
//			Bundle savedInstanceState) {
//		View rootView = inflater.inflate(R.layout.fragment_send_image,
//				container, false);
//		return rootView;
//	}
//	@Override
//	public void onActivityCreated(Bundle savedInstanceState) {
//		super.onActivityCreated(savedInstanceState);
//		seekbar = (SeekBar)getActivity().findViewById(R.id.seekBar1); // make seekbar object
//		seekbar.setOnSeekBarChangeListener(this);
//		textProgress = (TextView)getActivity().findViewById(R.id.time);
//		recipients = new ArrayList<String>();
//		viewTime=10;
//		friendIndex= new HashMap<String,Integer>();
//		
//		//add names to list and then sort alphabetically
//		//add to hashmap so you can find out correct index later
//		int i=0;
//		for(Friend fr: MyApplication.myFriends){
//			friendIndex.put(fr.getUsername(), i);
//			MyApplication.friendsNames.add(fr.getUsername());
//			i++;
//		}
//		
//		Collections.sort(MyApplication.friendsNames);
//		
//		//create an ArrayAdaptar from the String Array
//		  dataAdapter = new MyCustomAdapter(getActivity().getApplicationContext(),
//		    R.layout.checkbox_listitem);
//		  ListView listView = (ListView)getActivity().findViewById(R.id.friendsLV);
//		  // Assign adapter to ListView
//		  listView.setAdapter(dataAdapter);
//		  listView.setOnItemClickListener(new OnItemClickListener() {
//			   public void onItemClick(AdapterView<?> parent, View view,
//			     int position, long id) {
//			    Toast.makeText(getActivity().getApplicationContext(),
//			      "Click the checkbox", 
//			      Toast.LENGTH_LONG).show();
//			   }
//			  });
//			
//		}
//		@Override
//	    public void onProgressChanged(SeekBar seekBar, int progress,
//	    		boolean fromUser) {
//	    	textProgress.setText("Send for "+progress+" secs");
//	    	viewTime=progress;
//	    }
//		@Override
//	    public void onStartTrackingTouch(SeekBar seekBar) {
//	    	
//	    }
//		@Override
//	    public void onStopTrackingTouch(SeekBar seekBar) {
//	    	seekBar.setSecondaryProgress(seekBar.getProgress()); // set the shade of the previous value.
//	    }
//		private class SendSnap extends AsyncTask<String, Void, String> {
//			@Override
//			protected String doInBackground(String... params) {
//				try {
//					boolean video = false; //whether or not 'file' is a video or not.
//					boolean story = false; //whether or not add this to your story.
//					int time = 10; //How long will the snap last. Max = 10.
//					String mediaId = Snapchat.upload(SendImageActivity.currentImageFile, recipients, video, story, time);
//				} catch (FileNotFoundException e) {
//					e.printStackTrace();
//				}
//				return "Executed";
//			}
//
//			@Override
//			protected void onPostExecute(String result) {
//				if (sentOrNah){
//					Toast tst= 
//					Toast.makeText(getApplicationContext(),
//							"Snap was sent", Toast.LENGTH_SHORT);
//					tst.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);
//							tst.show();
//							finish();
//				}else{
//					Toast tst=Toast.makeText(getApplicationContext(),
//							"Error occured please try again later", Toast.LENGTH_SHORT);
//					tst.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);
//					tst.show();
//				}
//				finish();
//			}
//		}
//	private class MyCustomAdapter extends ArrayAdapter<String> {
//		 
//		  public MyCustomAdapter(Context context, int resource) {
//			super(context, resource);
//		}
//
//		private class ViewHolder {
//		   TextView code;
//		   CheckBox name;
//		  }
//		 
//		  @Override
//		  public View getView(int position, View convertView, ViewGroup parent) {
//		 
//		   ViewHolder holder = null;
//		   Log.v("ConvertView", String.valueOf(position));
//		 
//		   if (convertView == null) {
//		   LayoutInflater vi = (LayoutInflater)getActivity().getSystemService(
//		     Context.LAYOUT_INFLATER_SERVICE);
//		   convertView = vi.inflate(R.layout.checkbox_listitem, null);
//		 
//		   holder = new ViewHolder();
//		   holder.code = (TextView) convertView.findViewById(R.id.code);
//		   holder.name = (CheckBox) convertView.findViewById(R.id.checkBox1);
//		   convertView.setTag(holder);
//		 
//		    holder.name.setOnClickListener( new View.OnClickListener() {  
//		     public void onClick(View v) {  
//		      CheckBox cb = (CheckBox) v ;  
//		      FriendBox country = (FriendBox) cb.getTag();  
//		      country.setSelected(cb.isChecked());
//		     }  
//		    });  
//		   } 
//		   else {
//		    holder = (ViewHolder) convertView.getTag();
//		   }
//		   holder.name.setText(MyApplication.friendsNames.get(position));		 
//		   return convertView;
//		  }
//		 
//		 }
//	public class FriendBox {
//		  
//		 String code = null;
//		 String name = null;
//		 boolean selected = false;
//		  
//		 public FriendBox(String code, String name, boolean selected) {
//		  super();
//		  this.code = code;
//		  this.name = name;
//		  this.selected = selected;
//		 }
//		  
//		 public String getCode() {
//		  return code;
//		 }
//		 public void setCode(String code) {
//		  this.code = code;
//		 }
//		 public String getName() {
//		  return name;
//		 }
//		 public void setName(String name) {
//		  this.name = name;
//		 }
//		 
//		 public boolean isSelected() {
//		  return selected;
//		 }
//		 public void setSelected(boolean selected) {
//		  this.selected = selected;
//		 }
//		  
//		}
//	
//}
