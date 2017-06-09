package com.figytuna.projectseveryday;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class EntryListAdapter extends ArrayAdapter<DBEntry>{

  public EntryListAdapter (Context context, ArrayList<DBEntry> entries)
  {
    super(context, 0, entries);
  }

  @Override
  public View getView (int position, View convertView, ViewGroup parent)
  {
    final DBEntry entry = getItem(position);

    if (convertView == null) {
      convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_entry, parent, false);
    }

    Spinner spinner = (Spinner) convertView.findViewById(R.id.spinnerProject);
    setProjectSpinner(spinner, entry);

    final NumberPicker hourPicker = (NumberPicker) convertView.findViewById(R.id.pickerHour);
    hourPicker.setMinValue(0);
    hourPicker.setMaxValue(23);
    hourPicker.setValue(entry.getHour());

    hourPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
      @Override
      public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        entry.setHour(hourPicker.getValue());
      }
    });

    final NumberPicker minutePicker = (NumberPicker) convertView.findViewById(R.id.pickerMinute);
    minutePicker.setMinValue(0);
    minutePicker.setMaxValue(59);
    minutePicker.setValue(entry.getMinute());

    minutePicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
      @Override
      public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        entry.setMinute(minutePicker.getValue());
      }
    });

    Button deleteButton = (Button) convertView.findViewById(R.id.btnDelete);

    deleteButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        remove(entry);
        entry.delete();
      }
    });

    return convertView;
  }

  private void setProjectSpinner (Spinner spinner, final DBEntry entry)
  {
    DatabaseHandler db = new DatabaseHandler(getContext());
    final ArrayList<DBProject> projects = db.getProjects();
    List<String> options = new ArrayList<String>();

    int count = 0;
    int currentPosition = 0;

    for (DBProject project : projects)
    {
      options.add (project.getTitle());
      if (project.getId() == entry.getProject().getId())
      {
        currentPosition = count;
      }
      ++count;
    }

    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, options);


    adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
    spinner.setAdapter (adapter);

    spinner.setSelection (currentPosition);

    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        entry.setProject(projects.get(position));
      }

      @Override
      public void onNothingSelected(AdapterView<?> parent) {
        //Do Nothing
      }
    });

  }

}
