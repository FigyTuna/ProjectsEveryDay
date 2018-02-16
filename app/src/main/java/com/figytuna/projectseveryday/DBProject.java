package com.figytuna.projectseveryday;

public class DBProject {

  private DatabaseHandler mDb;
  private long mId;
  private String mTitle;
  private int mTotalHours;
  private int mTotalMinutes;

  public DBProject(DatabaseHandler db, long id, String title, int totalHours, int totalMinutes)
  {
    mDb = db;
    mId = id;
    mTitle = title;
    mTotalHours = totalHours;
    mTotalMinutes = totalMinutes;
  }

  public long getId()
  {
    return mId;
  }

  public String getTitle()
  {
    return mTitle;
  }

  public int getTotalHours() { return mTotalHours + (mTotalMinutes / 60); }

  public int getTotalMinutes() { return mTotalMinutes % 60; }

  public void rename (String title)
  {
    mTitle = title;
    mDb.pushChanges(this);
  }

  public void delete ()
  {
    mDb.delete(this);
    mId = -1;
    mTitle = "";
  }
}
