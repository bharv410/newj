package com.kidgeniushq.susd.utility;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.kidgeniushq.susd.R;

public class GcmMessageHandler extends IntentService {

    String mes;
    private Handler handler;
   public GcmMessageHandler() {
       super("GcmMessageHandler");
   }

   @Override
   public void onCreate() {
       // TODO Auto-generated method stub
       super.onCreate();
       handler = new Handler();
   }
   @Override
   protected void onHandleIntent(Intent intent) {
       Bundle extras = intent.getExtras();

       GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
       // The getMessageType() intent parameter must be the intent you received
       // in your BroadcastReceiver.
       String messageType = gcm.getMessageType(intent);

      mes = extras.getString("title");
      showNotif();
      mes=extras.getString("message");
      showNotif();
      Log.i("GCM", "Received : (" +messageType+")  "+extras.getString("title"));

       GcmBroadcastReceiver.completeWakefulIntent(intent);

   }

   public void showNotif(){
       handler.post(new Runnable() {
           public void run() {
               Toast.makeText(getApplicationContext(),mes , Toast.LENGTH_LONG).show();
           }
        });

       NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
	    .setSmallIcon(R.drawable.ic_launcher)
	    .setContentTitle(mes)
	    .setContentText("Events received");
	   System.out.print("here");
	   Notification ntf=mBuilder.build();
	   NotificationManager nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
	   nm.notify(0, ntf);
       
   }
}