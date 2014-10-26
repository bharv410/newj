package com.kidgeniushq.susd.asynctasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.habosa.javasnap.Snapchat;
import com.kidgeniushq.susd.utility.MyApplication;

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
		welcomeMessages[0]="Please rate nicely in app store & leave suggestions for next week's improvements : )";
	}
	
	@Override
    protected void onPreExecute() {
		progress = ProgressDialog.show(this.activity, "Logging in...",
			    welcomeMessages[0], true);
    }
    @Override
    protected String doInBackground(String... params) {
    	MyApplication.snapchat = Snapchat.login(params[0], params[1]);
        return "Executed";
    }

    @Override
    protected void onPostExecute(String result) {
    	progress.dismiss();    			
    	if(MyApplication.snapchat!=null){
    	GetStoriesAsyncTask gsat= new GetStoriesAsyncTask(context,activity);
    	gsat.execute();
    	}else
    		Toast.makeText(context, "wrong username or passwoord", Toast.LENGTH_SHORT).show();
    }    

    @Override
    protected void onProgressUpdate(Void... values) {}
}
