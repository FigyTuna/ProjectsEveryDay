package com.figytuna.projectseveryday;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class ProjectsListAdapter extends ArrayAdapter<DBProject> {

  private ProjectsActivity mProjectsActivity;

  public ProjectsListAdapter (Context context, ArrayList<DBProject> projects, ProjectsActivity projectsActivity)
  {
    super(context, 0, projects);
    mProjectsActivity = projectsActivity;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent)
  {
    final DBProject project = getItem(position);

    if (convertView == null) {
      convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_project, parent, false);
    }

    TextView titleView = (TextView) convertView.findViewById(R.id.project_item_title);

    titleView.setText(project.getTitle());

    Button renameButton = (Button) convertView.findViewById(R.id.project_item_rename);
    Button deleteButton = (Button) convertView.findViewById(R.id.project_item_delete);

    renameButton.setOnClickListener(new View.OnClickListener(){

      @Override
      public void onClick(View v) {
        showRenameDialog(project);
      }
    });

    deleteButton.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {
        showDeleteDialog(project);
      }
    });

    return convertView;
  }

  public void showRenameDialog (final DBProject project)
  {
    AlertDialog.Builder renameDialog = new AlertDialog.Builder(mProjectsActivity);
    final EditText input = new EditText(mProjectsActivity);
    input.setFilters (new InputFilter[] { new InputFilter.LengthFilter (
        DatabaseHandler.DEFAULT_MAX_LENGTH) });
    input.setSingleLine();
    input.setText (project.getTitle());

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
    AlertDialog.Builder deleteDialog = new AlertDialog.Builder(mProjectsActivity);
    deleteDialog.setTitle(R.string.sDeleteProjectAlertTitle);
    deleteDialog.setMessage(R.string.sDeleteProjectAlertBody);

    deleteDialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        remove(project);
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
