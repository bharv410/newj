package com.kidgeniushq.susd.asynctasks;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.habosa.javasnap.Snapchat;
import com.kidgeniushq.susd.MainActivity;
import com.kidgeniushq.susd.R;
import com.kidgeniushq.susd.mainfragments.FeedFragment;
import com.kidgeniushq.susd.mainfragments.MyStoryGridFragment;
import com.kidgeniushq.susd.utility.MyApplication;
import com.kidgeniushq.susd.utility.MyStorysAlarmReciever;

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
    		 //save username to file for auto login
   		 saveToFile(MyApplication.username,MyApplication.password);
   		 ((MainActivity)activity).dropKeyboard();
    	GetStoriesAsyncTask gsat= new GetStoriesAsyncTask(context,activity);
    	gsat.execute();
    	
    	//save storys everyda
    	MyStorysAlarmReciever alarm = new MyStorysAlarmReciever();
    	alarm.setAlarm(context);
    	
    	//set mystorysgrid photos
    	MyStoryGridFragment mystorysfrag = (MyStoryGridFragment)((MainActivity)activity).getSupportFragmentManager().findFragmentByTag(
                "android:switcher:"+R.id.pager+":3");
    	mystorysfrag.setStoryAdapter();
    	
    	}else{
    		Toast.makeText(context, "wrong username or passwoord", Toast.LENGTH_SHORT).show();
    	((MainActivity)activity).login();
    	}
    }    

    @Override
    protected void onProgressUpdate(Void... values) {}
    
    public void saveToFile(String un, String pw) {
		try {
			FileWriter out = new FileWriter(new File(context
					.getFilesDir(), "username.txt"));
			out.write(un);
			out.close();
			
			out = new FileWriter(new File(context
					.getFilesDir(), "password.txt"));
			out.write(pw);
			out.close();
			
		} catch (IOException e) {
			System.out.print(e);
		}
	}
}
