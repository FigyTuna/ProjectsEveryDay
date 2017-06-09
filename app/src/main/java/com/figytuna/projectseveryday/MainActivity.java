package com.figytuna.projectseveryday;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

  private DatabaseHandler mDb;
  private Calendar mCalendar;
  private Button mDateButton;
  private EntryListAdapter mAdapter;
  private ListView mListView;

  private Context mContext;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
  }

  @Override
  public void onStart ()
  {
    super.onStart();
    mDb = new DatabaseHandler(this);
    mCalendar = Calendar.getInstance();

    NotificationHandler.setNotification(getApplicationContext());
    mDateButton = (Button) findViewById(R.id.btnDate);

    mDateButton.setText(DatabaseHandler.getDateFormat(mCalendar));

    mListView = (ListView) findViewById(R.id.list_entries);
    mAdapter = new EntryListAdapter(this, mDb.getEntries(mCalendar));
    mListView.setAdapter(mAdapter);

    mContext = this;
  }

  @Override
  public boolean onCreateOptionsMenu (Menu menu)
  {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate (R.menu.main_activity_menu, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected (MenuItem item)
  {
    switch (item.getItemId()) {
      case R.id.settings:
        Intent settingsActivity = new Intent (this, SettingsActivity.class);
        startActivity(settingsActivity);
        return true;
      case R.id.editProjects:
        Intent editProjectsActivity = new Intent (this, ProjectsActivity.class);
        startActivity(editProjectsActivity);
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  public void onClickDate (View view)
  {
    AlertDialog.Builder dateDialog = new AlertDialog.Builder(this);
    final DatePicker datePicker = new DatePicker(this);

    datePicker.init(mCalendar.get(Calendar.YEAR),
        mCalendar.get(Calendar.MONTH),
        mCalendar.get(Calendar.DAY_OF_MONTH),
        new DatePicker.OnDateChangedListener(){

      @Override
      public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        //Do Nothing
      }
    });

    dateDialog.setView(datePicker);

    dateDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        mCalendar.set (Calendar.YEAR, datePicker.getYear());
        mCalendar.set (Calendar.MONTH, datePicker.getMonth());
        mCalendar.set (Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());
        mDateButton.setText(DatabaseHandler.getDateFormat(mCalendar));
        mAdapter = new EntryListAdapter(mContext, mDb.getEntries(mCalendar));
        mListView.setAdapter(mAdapter);
      }
    });

    dateDialog.setNegativeButton("Today", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        mCalendar = Calendar.getInstance();
        mDateButton.setText(DatabaseHandler.getDateFormat(mCalendar));
        mAdapter = new EntryListAdapter(mContext, mDb.getEntries(mCalendar));
        mListView.setAdapter(mAdapter);
      }
    });

    dateDialog.show();
  }

  public void onClickAdd (View view)
  {
    if (mDb.getProjects().size() > 0) {
      DBEntry entry = mDb.getEmptyEntry(mCalendar);
      mAdapter.add(entry);
    }
    else
    {
      Toast error = new Toast (this);
      error.makeText(this, "You must add a project first.", Toast.LENGTH_SHORT).show();
    }
  }
}
