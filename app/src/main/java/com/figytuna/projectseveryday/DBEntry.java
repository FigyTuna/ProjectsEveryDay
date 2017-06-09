package com.figytuna.projectseveryday;

import java.util.Calendar;

public class DBEntry {

  DatabaseHandler mDb;
  private long mId;
  Calendar mCalendar;
  private DBProject mProject;
  private int mHour;
  private int mMinute;

  public DBEntry (DatabaseHandler db, long id, Calendar calendar,
                  DBProject project, int hour, int minute)
  {
    mDb = db;
    mId = id;
    mCalendar = calendar;
    mProject = project;
    mHour = hour;
    mMinute = minute;
  }

  public long getId ()
  {
    return mId;
  }

  public int getHour ()
  {
    return mHour;
  }

  public int getMinute ()
  {
    return mMinute;
  }

  public DBProject getProject ()
  {
    return mProject;
  }

  public void setHour (int hour)
  {
    mHour = hour;
    mDb.pushChanges(this);
  }

  public void setMinute (int minute)
  {
    mMinute = minute;
    mDb.pushChanges(this);
  }

  public void setProject (DBProject project)
  {
    mProject = project;
    mDb.pushChanges(this);
  }

  public void delete ()
  {
    mDb.delete(this);
    mId = -1;
    mCalendar = null;
    mProject = null;
    mHour = -1;
    mMinute = -1;
  }
}
