package com.figytuna.projectseveryday;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

public class ProjectsActivity extends AppCompatActivity {

  private ProjectsListAdapter mAdapter;
  DatabaseHandler mDb;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_projects);
  }

  @Override
  public void onStart()
  {
    super.onStart();

    mDb = new DatabaseHandler(getApplicationContext());

    ListView listView = (ListView) findViewById(R.id.list_projects);
    mAdapter = new ProjectsListAdapter(getApplicationContext(), mDb.getProjects(), this);
    listView.setAdapter(mAdapter);
  }

  public void onClickAdd (View view)
  {
    DBProject project = mDb.getEmptyProject();
    mAdapter.add(project);
    mAdapter.showRenameDialog(project);
  }
}
