package com.kidgeniushq.susd.asynctasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.habosa.javasnap.Snapchat;
import com.kidgeniushq.susd.MainActivity;
import com.kidgeniushq.susd.R;
import com.kidgeniushq.susd.mainfragments.FeedFragment;
import com.kidgeniushq.susd.mainfragments.MyStoryGridFragment;

public class LoginAsyncTask extends AsyncTask<String, Void, String> {
Context context;
Activity activity;
ProgressDialog progress;
String[] welcomeMessages;
	public LoginAsyncTask(Context ctx, Activity act){
		super();
		context=ctx;
		activity=act;
		welcomeMessages=new String[5];
		welcomeMessages[0]="Just wait a sec";
		
	}
	
	@Override
    protected void onPreExecute() {
		progress = ProgressDialog.show(this.activity, "Logging in...",
			    welcomeMessages[0], true);
    	
    }
    @Override
    protected String doInBackground(String... params) {
    	MainActivity.snapchat = Snapchat.login(params[0], params[1]);
        return "Executed";
    }

    @Override
    protected void onPostExecute(String result) {
    	progress.dismiss();
    	
    	if(MainActivity.snapchat!=null){
    		//succesfully loggedin
    	Toast.makeText(context, "logged in", Toast.LENGTH_SHORT).show();
    	//load images
    	
    	//grab stories
    	GetStoriesAsyncTask gsat= new GetStoriesAsyncTask(context,activity);
    	gsat.execute();
    	}else
    		Toast.makeText(context, "error", Toast.LENGTH_SHORT).show();
                
    }

    

    @Override
    protected void onProgressUpdate(Void... values) {}
}
