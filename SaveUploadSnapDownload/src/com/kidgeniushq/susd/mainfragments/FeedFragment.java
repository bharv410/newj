package com.kidgeniushq.susd.mainfragments;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import wseemann.media.FFmpegMediaMetadataRetriever;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.habosa.javasnap.Snapchat;
import com.habosa.javasnap.Story;
import com.kidgeniushq.susd.BigView;
import com.kidgeniushq.susd.R;
import com.kidgeniushq.susd.VideoViewActivity;
import com.kidgeniushq.susd.adapters.SnapAdapter;
import com.kidgeniushq.susd.utility.MyApplication;
import com.kidgeniushq.susd.utility.Utility;

public class FeedFragment extends Fragment {
	GridView gv; //for images
	int gridViewNum=0;
	SnapAdapter sa;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.activity_feed, container,
				false);
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}
	public class GetSnap extends AsyncTask<Object, String, Bitmap> {
		int curr;

		public GetSnap( int cur) {
			this.curr=cur;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			MyApplication.requestInProgress = true;
		}

		@Override
		protected Bitmap doInBackground(Object... parameters) {
			
			if (curr < MyApplication.stories.length - 1) {
				Bitmap bitmap = null;
				Story current = MyApplication.stories[curr];
				byte[] imageBytes = Snapchat.getStory(current);

				if (current.isImage()) {
					bitmap = Utility.getPhoto(imageBytes);

				} else {
					byte[] snapBytes=Snapchat.getStory(current);
					File vidFile = new File(getActivity().getApplicationContext().getFilesDir()
							+ "/"+curr+"video.mp4");
					System.out.println(vidFile.getAbsolutePath());
					System.out.println(vidFile.getAbsolutePath());
					System.out.println(vidFile.getAbsolutePath());
					
					System.out.println(vidFile.getAbsolutePath());
					System.out.println(vidFile.getAbsolutePath());
					System.out.println(vidFile.getAbsolutePath());
					System.out.println(vidFile.getAbsolutePath());
					System.out.println(vidFile.getAbsolutePath());
					System.out.println(vidFile.getAbsolutePath());
					System.out.println(vidFile.getAbsolutePath());
					System.out.println(vidFile.getAbsolutePath());
					System.out.println(vidFile.getAbsolutePath());
					
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
					FFmpegMediaMetadataRetriever mmr = new FFmpegMediaMetadataRetriever();
					mmr.setDataSource(Uri.fromFile(vidFile).toString());
					bitmap=drawTextToBitmap(getActivity().getApplicationContext(),mmr.getFrameAtTime(1000000, FFmpegMediaMetadataRetriever.OPTION_CLOSEST),"Press to play video");
					mmr.release();
				}
				return bitmap;
			} else
				return null;
		}

		@Override
		protected void onPostExecute(Bitmap bitmap) {
			if (MyApplication.imageList==null) {
				MyApplication.imageList = new ArrayList<Bitmap>();
				gv = (GridView) getActivity().findViewById(R.id.gridview);
				gv.setOnItemClickListener(new OnItemClickListener() {
			        @Override
			        public void onItemClick(AdapterView<?> parent, View v,
			                int position, long id) {
			        	//set current story & set flag to stop loadgiin
			        	MyApplication.requestInProgress=true;
			        	MyApplication.currentStory=MyApplication.stories[position];
			            if(MyApplication.currentStory.isImage()){
			            	MyApplication.currentBitmap=MyApplication.imageList.get(position);
			            	startActivity(new Intent(getActivity(),BigView.class));
			            }else{
			            	Intent i=new Intent(getActivity(),VideoViewActivity.class);
			            	MyApplication.vidIndex=position;
			            	startActivity(i);
			            }
			            	
			            	
			        }
			    });
				MyApplication.imageList.add(bitmap);
				sa = new SnapAdapter(getActivity().getApplicationContext(),
						getActivity());
				gv.setAdapter(sa);
				gv.setOnScrollListener(new GVOnScrollListener());
			}else{
				MyApplication.imageList.add(bitmap);
				sa.notifyDataSetInvalidated();
				gv.invalidateViews();
			}
			MyApplication.requestInProgress = false;
		}
	}
	
	public void addImagesToScreen(){
			for(int i=0; i<10;i++){
				GetSnap gs = new GetSnap(gridViewNum);
				gs.execute();
				gridViewNum++;
			}
	}
	
	public Bitmap drawTextToBitmap(Context mContext,  Bitmap bitmap,  String mText) {
	    try {
	         Resources resources = mContext.getResources();
	            float scale = resources.getDisplayMetrics().density;

	            android.graphics.Bitmap.Config bitmapConfig =   bitmap.getConfig();
	            // set default bitmap config if none
	            if(bitmapConfig == null) {
	              bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;
	            }
	            bitmap = bitmap.copy(bitmapConfig, true);

	            Canvas canvas = new Canvas(bitmap);
	            // new antialised Paint
	            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
	            // text color - #3D3D3D
	            paint.setColor(Color.rgb(255,255, 255));
	            // text size in pixels
	            paint.setTextSize((int) (28 * scale));
	            // text shadow
	            paint.setShadowLayer(1f, 0f, 1f, Color.DKGRAY);

	            // draw text to the Canvas center
	            Rect bounds = new Rect();
	            paint.getTextBounds(mText, 0, mText.length(), bounds);
	            int x = (bitmap.getWidth() - bounds.width())/6;
	            int y = (bitmap.getHeight() + bounds.height())/5;

	          canvas.drawText(mText, x * scale, y * scale, paint);

	            return bitmap;
	    } catch (Exception e) {
	        return null;
	    }

	  }
	public final class GVOnScrollListener implements AbsListView.OnScrollListener {
	       @Override 
	       public void onScroll(AbsListView view, int firstVisibleItem,
	                int visibleItemCount, int totalItemCount) {
	    	   System.out.print(""+totalItemCount);
	    	   if(firstVisibleItem + visibleItemCount >= totalItemCount-2){
	               // End has been reached
	    		   GetSnap gs = new GetSnap(gridViewNum);
	   				gs.execute();
	   				gridViewNum++;
	    	   }
	        }
		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			// TODO Auto-generated method stub
		}
	}
}
