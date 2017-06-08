package com.figytuna.projectseveryday;

import java.util.Calendar;

public class DBEntry {

  DatabaseHandler mDb;
  private int mId;
  Calendar mCalendar;
  private DBProject mProject;
  private int mHour;
  private int mMinute;

  public DBEntry (DatabaseHandler db, int id, Calendar calendar,
                  DBProject project, int hour, int minute)
  {
    mDb = db;
    mId = id;
    mCalendar = calendar;
    mProject = project;
    mHour = hour;
    mMinute = minute;
  }
}
