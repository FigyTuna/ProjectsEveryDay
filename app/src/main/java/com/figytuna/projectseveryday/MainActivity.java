package com.figytuna.projectseveryday;

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
import android.widget.TableLayout;

import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

  private TableLayout projectListLayout;
  private Calendar mCalendar;
  private Button mDateButton;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);


    mCalendar = Calendar.getInstance();

    //projectListLayout = (TableLayout) findViewById(R.id.projectListLayout);

    NotificationHandler.setNotification(getApplicationContext());
    mDateButton = (Button) findViewById(R.id.btnDate);

    mDateButton.setText(DatabaseHandler.getDateFormat(mCalendar));

    //Test addListItem
    /*for (int i = 0; i < 100; ++i)
    {
      addListItem ("List Item #" + i);
    }*/
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
      }
    });

    dateDialog.setNegativeButton("Today", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        mCalendar = Calendar.getInstance();
        mDateButton.setText(DatabaseHandler.getDateFormat(mCalendar));
      }
    });

    dateDialog.show();
  }

  /*private void addListItem (String title)
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
  }*/
}
