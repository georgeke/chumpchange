package com.example.chumpchange;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

public class IntervalEndReciever extends BroadcastReceiver {

	@SuppressLint("NewApi")
	@Override
	public void onReceive(Context c, Intent i) {
		Message.message(c, "sending notif!!!");
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(c);
		mBuilder.setSmallIcon(R.drawable.ic_notif);
		mBuilder.setContentTitle("Youre budget interval has ended!");
		mBuilder.setContentText("Click to view summary...");
		
		Intent toMainApp = new Intent(c, MainActivity.class);
		TaskStackBuilder builder = TaskStackBuilder.create(c);
		builder.addParentStack(MainActivity.class);
		builder.addNextIntent(toMainApp);		
		PendingIntent pi = builder.getPendingIntent(0, PendingIntent.FLAG_CANCEL_CURRENT);
		
		mBuilder.setContentIntent(pi);
		
		NotificationManager notifM = (NotificationManager) c.getSystemService(Context.NOTIFICATION_SERVICE);	
		notifM.notify(6767, mBuilder.build());
	}

}

