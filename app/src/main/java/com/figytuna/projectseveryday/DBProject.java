package com.figytuna.projectseveryday;

import android.util.Log;

public class DBProject {

  private DatabaseHandler mDb;
  private long mId;
  private String mTitle;

  public DBProject(DatabaseHandler db, long id, String title)
  {
    mDb = db;
    mId = id;
    mTitle = title;
  }

  public long getId()
  {
    return mId;
  }

  public String getTitle()
  {
    return mTitle;
  }

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
