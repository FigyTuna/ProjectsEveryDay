package com.figytuna.projectseveryday;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
        mProjectsActivity.showRenameDialog(project);
      }
    });

    deleteButton.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {
        mProjectsActivity.showDeleteDialog(project);
      }
    });

    return convertView;
  }


}
