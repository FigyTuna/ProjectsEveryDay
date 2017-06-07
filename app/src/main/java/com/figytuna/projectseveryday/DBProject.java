package com.figytuna.projectseveryday;

public class DBProject {

  DatabaseHandler mDb;
  long mId;
  String mTitle;

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
    mDb.deleteProject(this);
    mId = -1;
    mTitle = "";
  }
}
