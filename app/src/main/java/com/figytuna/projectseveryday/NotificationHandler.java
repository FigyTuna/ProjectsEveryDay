package com.figytuna.projectseveryday;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import java.util.Calendar;

public class NotificationHandler {

  public static void setNotification (Context context)
  {
    DatabaseHandler database = new DatabaseHandler(context);

    //Cancel existing notification alarms
    Intent alarmIntent = new Intent (context, MissedNotificationReciever.class);
    PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);
    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    alarmManager.cancel(pendingIntent);

    Calendar current_cal = Calendar.getInstance();
    Calendar notif_cal = database.getNotifTime();

    if (notif_cal.get(Calendar.HOUR_OF_DAY) <= current_cal.get(Calendar.HOUR_OF_DAY)
        && notif_cal.get (Calendar.MINUTE) <= current_cal.get (Calendar.MINUTE)) {
      notif_cal.add(Calendar.DATE, 1);
    }

    Log.v ("Setting alarm", "Hour: " + notif_cal.get(Calendar.HOUR_OF_DAY) + " Minute: " + notif_cal.get (Calendar.MINUTE) + " Date: " + notif_cal.get (Calendar.DATE) + " Today: " + current_cal.get (Calendar.DATE));

    alarmManager.set (AlarmManager.RTC_WAKEUP, notif_cal.getTimeInMillis(), pendingIntent);
  }

  public static void presentNotification (Context context)
  {
    DatabaseHandler database = new DatabaseHandler(context);

    if (database.getNotifEnabled() && database.getEntries(Calendar.getInstance()).size() == 0) {
      NotificationCompat.Builder missedNotif = new NotificationCompat.Builder(context);
      missedNotif.setSmallIcon(R.drawable.ic_stat_name)
        .setContentTitle (context.getString(R.string.sNotificationTitle))
        .setContentText (context.getString(R.string.sNotificationBody))
        .setAutoCancel(true)
        .setVibrate (new long[] {300, 300});

      Intent missedIntent = new Intent (context, MainActivity.class);
      TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
      stackBuilder.addParentStack(MainActivity.class);

      stackBuilder.addNextIntent(missedIntent);
      PendingIntent missedPendingIntent = stackBuilder.getPendingIntent((int) System.currentTimeMillis(), PendingIntent.FLAG_UPDATE_CURRENT);
      missedNotif.setContentIntent (missedPendingIntent);

      NotificationManager notifManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
      notifManager.notify (0, missedNotif.build ());
    }
  }
}
