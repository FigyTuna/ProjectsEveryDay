package com.figytuna.projectseveryday;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

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

        //Test Notification
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Calendar alarm_cal = Calendar.getInstance();
        alarm_cal.setTime (new Date());
        alarm_cal.add (Calendar.SECOND, 10);

        Intent alarmIntent = new Intent (this, MissedNotificationReciever.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);

        alarmManager.set (AlarmManager.RTC_WAKEUP, alarm_cal.getTimeInMillis(), pendingIntent);

    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate (R.menu.main_activity_menu, menu);
        return true;
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
