package com.figytuna.projectseveryday;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

    NotificationHandler.setNotification(getApplicationContext());

    //Test addListItem
    for (int i = 0; i < 100; ++i)
    {
      addListItem ("List Item #" + i);
    }
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
      /*case R.id.chooseTime:
        chooseTime ();
        return true;
      case R.id.resetDatabase:
        new DatabaseHandler(getApplicationContext()).resetDatabase();
        return true;*/
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
