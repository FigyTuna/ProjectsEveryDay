package com.figytuna.projectseveryday;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TimePicker;

import java.util.Calendar;

public class SettingsActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_settings);

    final DatabaseHandler db = new DatabaseHandler(getApplicationContext());
    Switch notificationSwitch = (Switch) findViewById(R.id.switchNotification);

    notificationSwitch.setChecked(db.getNotifEnabled());

    notificationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
    {
      public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
      {
        db.setNotifEnabled(isChecked);
      }
    });
  }

  public void onClickChooseTime (View view)
  {
    chooseTime();
  }

  public void onClickReset (View view)
  {
    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
    alertDialogBuilder.setTitle(R.string.sDatabaseResetConfirmation);
    alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        new DatabaseHandler(getApplicationContext()).resetDatabase();
        finish();
      }
    });
    alertDialogBuilder.setNegativeButton("No", null);
    alertDialogBuilder.create().show();
  }

  private void chooseTime ()
  {
    Calendar saved_time = new DatabaseHandler(getApplicationContext()).getNotifTime();
    int hour = saved_time.get(Calendar.HOUR_OF_DAY);
    int minute = saved_time.get(Calendar.MINUTE);
    TimePickerDialog timePicker;
    timePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
      @Override
      public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
        new DatabaseHandler(getApplicationContext()).updateNotifTime(selectedMinute, selectedHour);
        NotificationHandler.setNotification(getApplicationContext());
      }
    }, hour, minute, false);
    timePicker.setTitle("Select notification time.");
    timePicker.show();
  }
}
