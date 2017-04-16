package com.figytuna.projectseveryday;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.NotificationCompat;

public class MissedNotificationReciever extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        notifyMissed(context);
    }

    private void notifyMissed (Context context)
    {
        NotificationCompat.Builder missedNotif = new NotificationCompat.Builder(context);
        missedNotif.setSmallIcon(R.drawable.ic_stat_name)
                .setContentTitle ("Projects")
                .setContentText ("Notification")
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
