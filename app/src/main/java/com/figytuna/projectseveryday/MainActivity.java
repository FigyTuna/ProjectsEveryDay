package com.figytuna.projectseveryday;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TableLayout projectListLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        projectListLayout = (TableLayout) findViewById(R.id.projectListLayout);

        //Test addListItem
        for (int i = 0; i < 100; ++i)
        {
            addListItem ("List Item #" + i);
        }

    }

    public boolean onCreateOptionsMenu (Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate (R.menu.main_activity_menu, menu);
        return true;
    }

    private void notifyMissed ()
    {
        NotificationCompat.Builder missedNotif = new NotificationCompat.Builder(this);
        missedNotif.setSmallIcon(R.drawable.ic_stat_name);
        missedNotif.setContentTitle ("Projects");
        missedNotif.setContentText ("Notification");

        Intent missedIntent = new Intent (this, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);

        stackBuilder.addNextIntent(missedIntent);
        PendingIntent missedPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        missedNotif.setContentIntent (missedPendingIntent);

        NotificationManager notifManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notifManager.notify (0, missedNotif.build ());
    }

    private void addListItem (String title)
    {
        TableRow addTableRow = new TableRow(this);
        addTableRow.setOrientation (TableRow.VERTICAL);
        TableRow.LayoutParams rowContainerParams
                = new TableRow.LayoutParams (ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f);
        addTableRow.setLayoutParams (rowContainerParams);
        projectListLayout.addView(addTableRow);

        TextView addTextView = new TextView (this);
        addTextView.setText(title);
        addTextView.setGravity(Gravity.CENTER);
        TableRow.LayoutParams rowWidgetParams
                = new TableRow.LayoutParams (ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f);
        addTextView.setLayoutParams(rowWidgetParams);
        addTableRow.addView(addTextView);
    }
}
