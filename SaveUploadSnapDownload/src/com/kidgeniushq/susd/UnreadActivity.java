package com.kidgeniushq.susd;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.kidgeniushq.susd.utility.MyApplication;
import com.kidgeniushq.susd.utility.Utility;

public class UnreadActivity extends ListActivity {
ArrayAdapter<String> adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_unread);
		adapter= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,MyApplication.unreadSenders);
		setListAdapter(adapter);
		
		int currentAPIVersion = android.os.Build.VERSION.SDK_INT;
		if (currentAPIVersion >= android.os.Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().hide();
		}
	}
	
		@Override 
		protected void onListItemClick (ListView l, View v, int position, long id) {
		    Toast.makeText(this, "Clicked row " + position, Toast.LENGTH_SHORT).show();
		    
		    if(MyApplication.unreadSenders.get(position).startsWith("pic")){
		    MyApplication.currentBitmap=Utility.getPhoto(MyApplication.myUnreads.get(MyApplication.unreadSenders.get(position)));
		    startActivity(new Intent(this,BigView.class));
		    
		    
		    }else{
		    	File sdCard = Environment.getExternalStorageDirectory();
				File dir = new File (sdCard.getAbsolutePath() + "/dir1/dir2");
				dir.mkdirs();
				MyApplication.vidIndex=999;
				File vidFile = new File(dir, "/"+MyApplication.vidIndex+"video.mp4");
				byte[] snapBytes=MyApplication.myUnreads.get(MyApplication.unreadSenders.get(position));
				if(snapBytes[0] == 0x50 && snapBytes[1] == 0x4B) {
				    Log.d("snapBytes", "Snap is compressed in a ZIP file");
				    ByteArrayInputStream zipInput = new ByteArrayInputStream(snapBytes);
				    try {
				        ZipInputStream zis = new ZipInputStream(zipInput);
				        ZipEntry ze = zis.getNextEntry();

				        while (ze != null) {
				            String fileName = ze.getName();
				            if (fileName.contains("media")) {
				                FileOutputStream out = new FileOutputStream(vidFile);
				                int leido;
				                byte[] buffer = new byte[1024];
				                while (0 < (leido = zis.read(buffer))) {
				                    out.write(buffer, 0, leido);
				                }
				                out.close();
				            }
				            ze = zis.getNextEntry();
				        }
				    } catch (FileNotFoundException e) {
				        e.printStackTrace();
				    } catch (IOException e) {
				        e.printStackTrace();
				    }
				} else {
				    try {
				        FileOutputStream out = new FileOutputStream(vidFile);
				        out.write(snapBytes, 0, snapBytes.length);
				        out.close();
				    } catch (FileNotFoundException e) {
				        e.printStackTrace();
				    } catch (IOException e) {
				        e.printStackTrace();
				    }
				}
				startActivity(new Intent(UnreadActivity.this,VideoViewActivity.class));
		    }
		    
		    
	}
}
