package com.figytuna.projectseveryday;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MissedNotificationReciever extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        notifyMissed(context);
    }

    private void notifyMissed (Context context)
    {
        NotificationHandler.presentNotification(context);

        NotificationHandler.setNotification(context);
    }
}
