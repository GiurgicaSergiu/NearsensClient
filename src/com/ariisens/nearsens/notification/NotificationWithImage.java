package com.ariisens.nearsens.notification;

import java.util.Random;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.ariisens.nearsens.MainActivity;
import com.ariisens.nearsens.R;


public class NotificationWithImage {

	public static void putNotification(Context c, String title, String placeName, int icon){
		Bitmap remote_picture = null;
		 
		NotificationCompat.BigPictureStyle notiStyle = new 
		        NotificationCompat.BigPictureStyle();
		notiStyle.setBigContentTitle(placeName);
		notiStyle.setSummaryText("Offerta imperdibile! Valida solo per oggi");
		
		//try {
	        /*remote_picture = BitmapFactory.decodeStream(
	                (InputStream) new URL(sample_url).getContent());*/
			remote_picture = BitmapFactory.decodeResource(c.getResources(), icon);
	/*} catch (IOException e) {
	        e.printStackTrace();
	}*/
	 
	notiStyle.bigPicture(remote_picture);
	 
	Intent resultIntent = new Intent(c, MainActivity.class);
	 

	TaskStackBuilder stackBuilder = TaskStackBuilder.create(c);
	 
	stackBuilder.addParentStack(MainActivity.class);
	 
	// Adds the Intent that starts the Activity to the top of the stack.
	stackBuilder.addNextIntent(resultIntent);
	PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
	        0, PendingIntent.FLAG_UPDATE_CURRENT);
	 
	Notification myNotification = new NotificationCompat.Builder(c)
	        .setSmallIcon(R.drawable.ic_launcher)
	        .setAutoCancel(true)
	        .setLargeIcon(BitmapFactory.decodeResource(c.getResources(), R.drawable.ic_launcher))
	        .setContentIntent(resultPendingIntent)
	        .setContentTitle(placeName)
	        .setContentText(title)
	        .setStyle(notiStyle).build();
	
	
	
	NotificationManagerCompat notificationManager =
	        NotificationManagerCompat.from(c);
	
	notificationManager.notify(new Random().nextInt(2000), myNotification);

	}
}
