package com.figytuna.projectseveryday;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.widget.EditText;
import android.widget.ListView;

public class ProjectsActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_projects);
  }

  @Override
  public void onStart()
  {
    super.onStart();

    DatabaseHandler db = new DatabaseHandler(getApplicationContext());
    DBProject project = db.getEmptyProject();
    project.rename("What is happening?");;
    DBProject project2 = db.getEmptyProject();
    project2.rename("Another");

    ListView listView = (ListView) findViewById(R.id.list_projects);
    ProjectsListAdapter adapter = new ProjectsListAdapter(getApplicationContext(), db.getProjects(), this);
    listView.setAdapter(adapter);
  }

  public void showRenameDialog (final DBProject project)
  {
    AlertDialog.Builder renameDialog = new AlertDialog.Builder(getApplicationContext());
    final EditText input = new EditText(getApplicationContext());
    input.setFilters (new InputFilter[] { new InputFilter.LengthFilter (
        DatabaseHandler.DEFAULT_MAX_LENGTH) });
    input.setSingleLine();

    renameDialog.setTitle(R.string.sRenameProjectAlertTitle);
    renameDialog.setView(input);

    renameDialog.setPositiveButton("Rename", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        project.rename(input.getText().toString());
      }
    });

    renameDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        //Do nothing.
      }
    });

    renameDialog.show();
  }

  public void showDeleteDialog (final DBProject project)
  {
    AlertDialog.Builder deleteDialog = new AlertDialog.Builder(getApplicationContext());
    deleteDialog.setTitle(R.string.sDeleteProjectAlertTitle);
    deleteDialog.setMessage(R.string.sDeleteProjectAlertBody);

    deleteDialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        project.delete();
      }
    });

    deleteDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        //Do nothing.
      }
    });

    deleteDialog.show();
  }
}
