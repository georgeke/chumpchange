package com.example.chumpchange;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.support.v4.app.NotificationCompat;
import android.view.WindowManager;

public class IntervalEndReciever extends BroadcastReceiver {

	@SuppressLint("NewApi")
	@Override
	public void onReceive(Context c, Intent i) {
		Message.message(c, "sending notif!!!");
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(c);
		mBuilder.setAutoCancel(true);
		mBuilder.setSmallIcon(R.drawable.ic_notif);
		mBuilder.setContentTitle("Budget interval over!");
		mBuilder.setContentText("Click to view summary...");
		mBuilder.setLights(0xffff00, 1000, 1500);
		
		Intent toMainApp = new Intent(c, MainActivity.class);
		TaskStackBuilder builder = TaskStackBuilder.create(c);
		builder.addParentStack(MainActivity.class);
		builder.addNextIntent(toMainApp);		
		PendingIntent pi = builder.getPendingIntent(0, PendingIntent.FLAG_CANCEL_CURRENT);
		
		mBuilder.setContentIntent(pi);
		
		PowerManager pm = (PowerManager) c.getSystemService(Context.POWER_SERVICE);
		WakeLock wl = pm.newWakeLock(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | PowerManager.ACQUIRE_CAUSES_WAKEUP, "jiggy");
		wl.acquire(2*1000);
		
		NotificationManager notifM = (NotificationManager) c.getSystemService(Context.NOTIFICATION_SERVICE);	
		notifM.notify(6767, mBuilder.build());
	}

}

