package com.figytuna.projectseveryday;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class DatabaseHandler {

  public static final int DEFAULT_MAX_LENGTH = 45;

  private static final String DATABASE_NAME = "ped_db";

  private static final String SINGLE_VALUE_TABLE = "single_value_table";
  private static final String ID_COLUMN = "id_col";
  private static final int ID_COL_INDEX = 0;
  private static final String MINUTE_COLUMN = "minute_col";
  private static final int MINUTE_COL_INDEX = 1;
  private static final String HOUR_COLUMN = "hour_col";
  private static final int HOUR_COL_INDEX = 2;
  private static final String NOTIF_COLUMN = "notification_col";
  private static final int NOTIF_COL_INDEX = 3;

  private static final int DEFAULT_ID = 1;
  private static final int DEFAULT_MINUTE = 0;
  private static final int DEFAULT_HOUR = 12;

  private static final String PROJECTS_TABLE = "projects_table";
  private static final String PROJECT_TITLE_COLUM = "project_title_col";
  private static final int PROJECT_COL_INDEX = 1;

  private static final String ENTRY_TABLE = "entry_table";
  private static final String ENTRY_DATE_COLUMN = "date_col";
  private static final int ENTRY_DATE_INDEX = 1;
  private static final String ENTRY_PROJECT_COLUMN = "project_id_col";
  private static final int ENTRY_PROJECT_INDEX = 2;
  private static final String ENTRY_HOUR_COLUMN = "hour_col";
  private static final int ENTRY_HOUR_INDEX = 3;
  private static final String ENTRY_MINUTE_COLUMN = "minute_col";
  private static final int ENTRY_MINUTE_INDEX = 4;

  private SQLiteDatabase db;

  public DatabaseHandler (Context context)
  {
    db = context.openOrCreateDatabase(DATABASE_NAME, Context.MODE_PRIVATE, null);
  }

  private void singleValuesTable ()
  {
    db.execSQL ("CREATE TABLE IF NOT EXISTS "
        + SINGLE_VALUE_TABLE + "("
        + ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT, "
        + MINUTE_COLUMN + " INT, "
        + HOUR_COLUMN + " INT, "
        + NOTIF_COLUMN + " INT"
        + ");");

    Cursor resultSet = db.rawQuery ("SELECT * from " + SINGLE_VALUE_TABLE + ";", null);

    if (resultSet.getCount () < 1)
    {
      db.execSQL ("INSERT INTO " + SINGLE_VALUE_TABLE + " VALUES(" + DEFAULT_ID + ", "
          + DEFAULT_MINUTE + ", " + DEFAULT_HOUR + ", 1);");
    }
  }

  private void projectsTable ()
  {
    db.execSQL ("CREATE TABLE IF NOT EXISTS "
        + PROJECTS_TABLE + "("
        + ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT, "
        + PROJECT_TITLE_COLUM + " VARCHAR (" + DEFAULT_MAX_LENGTH + ")"
        + ");");
  }

  private void entryTable ()
  {
    projectsTable();

    db.execSQL ("CREATE TABLE IF NOT EXISTS "
        + ENTRY_TABLE + "("
        + ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT, "
        + ENTRY_DATE_COLUMN + " VARCHAR,"
        + ENTRY_PROJECT_COLUMN + " INT,"
        + ENTRY_HOUR_COLUMN + " INT,"
        + ENTRY_MINUTE_COLUMN + " INT,"
        + " FOREIGN KEY (" + ENTRY_PROJECT_COLUMN + ") REFERENCES " + PROJECTS_TABLE + " (" + ID_COLUMN + ")"
        + ");");
  }

  public void updateNotifTime (int minute, int hour)
  {
    singleValuesTable();

    ContentValues contentValues = new ContentValues ();
    contentValues.put (MINUTE_COLUMN, minute);
    contentValues.put (HOUR_COLUMN, hour);

    db.update (SINGLE_VALUE_TABLE, contentValues, ID_COLUMN + " = " + DEFAULT_ID, null);
  }

  public Calendar getNotifTime ()
  {
    singleValuesTable();

    Calendar cal = Calendar.getInstance();

    Cursor resultSet = db.rawQuery ("SELECT * from " + SINGLE_VALUE_TABLE + ";", null);

    resultSet.moveToFirst();

    cal.set (Calendar.SECOND, 0);
    cal.set (Calendar.MINUTE, resultSet.getInt (MINUTE_COL_INDEX));
    cal.set (Calendar.HOUR_OF_DAY, resultSet.getInt (HOUR_COL_INDEX));

    return cal;
  }

  public boolean getNotifEnabled ()
  {
    singleValuesTable();

    boolean ret = false;

    Cursor resultSet = db.rawQuery ("SELECT * from " + SINGLE_VALUE_TABLE + ";", null);

    resultSet.moveToFirst();

    if (resultSet.getInt (NOTIF_COL_INDEX) > 0)
    {
      ret = true;
    }

    return ret;
  }

  public void setNotifEnabled (boolean enabled)
  {
    int enabledInt = enabled ? 1 : 0;

    singleValuesTable();

    ContentValues contentValues = new ContentValues ();
    contentValues.put (NOTIF_COLUMN, enabledInt);

    db.update (SINGLE_VALUE_TABLE, contentValues, ID_COLUMN + " = " + DEFAULT_ID, null);
  }

  public void pushChanges (DBProject project)
  {
    String filter = ID_COLUMN + "=" + project.getId();
    ContentValues values = new ContentValues();
    values.put (PROJECT_TITLE_COLUM, project.getTitle());
    db.update(PROJECTS_TABLE, values, filter, null);
  }

  public void pushChanges (DBEntry entry)
  {
    String filter = ID_COLUMN + "=" + entry.getId ();
    ContentValues values = new ContentValues();
    values.put (ENTRY_HOUR_COLUMN, entry.getHour());
    values.put (ENTRY_MINUTE_COLUMN, entry.getMinute());
    values.put (ENTRY_PROJECT_COLUMN, entry.getProject().getId());
    db.update (ENTRY_TABLE, values, filter, null);
  }

  public void delete (DBProject project)
  {
    String deleteSQL;
    SQLiteStatement stmt;

    entryTable();

    deleteSQL = "DELETE FROM " + ENTRY_TABLE + " WHERE " + ENTRY_PROJECT_COLUMN + " = ?;";
    stmt = db.compileStatement(deleteSQL);
    stmt.bindLong (1, project.getId());
    stmt.executeUpdateDelete();

    deleteSQL = "DELETE FROM " + PROJECTS_TABLE + " WHERE " + ID_COLUMN + " = ?;";
    stmt = db.compileStatement(deleteSQL);
    stmt.bindLong (1, project.getId());
    stmt.executeUpdateDelete();
  }

  public void delete (DBEntry entry)
  {
    String deleteSQL;
    SQLiteStatement stmt;

    projectsTable();

    deleteSQL = "DELETE FROM " + ENTRY_TABLE + " WHERE " + ID_COLUMN + " = ?;";
    stmt = db.compileStatement(deleteSQL);

    stmt.bindLong (1, entry.getId());

    stmt.executeUpdateDelete();
  }

  public DBProject getEmptyProject()
  {
    projectsTable();
    ContentValues values = new ContentValues();
    values.put(PROJECT_TITLE_COLUM, "");
    long id = db.insert(PROJECTS_TABLE, null, values);

    DBProject project = new DBProject(this, id, "", 0, 0);

    return project;
  }

  private DBProject getProject (int id)
  {
    projectsTable();
    DBProject project;

    Cursor resultSet = db.rawQuery("SELECT * FROM " + PROJECTS_TABLE + " WHERE " + ID_COLUMN + " = " + id + ";" ,null);
    resultSet.moveToFirst();

    if (resultSet.getCount () > 0) {
      project = new DBProject(
          this,
          resultSet.getInt(ID_COL_INDEX),
          resultSet.getString(PROJECT_COL_INDEX),
          getTotalHours(resultSet.getInt(ID_COL_INDEX)),
          getTotalMinutes(resultSet.getInt(ID_COL_INDEX))
      );
    }
    else
    {
      project = getEmptyProject();
      project.rename ("ERROR: Missing Project");
    }

    return project;
  }

  public ArrayList<DBProject> getProjects()
  {
    ArrayList<DBProject> list = new ArrayList<DBProject>();
    projectsTable();

    Cursor resultSet = db.rawQuery("SELECT * FROM " + PROJECTS_TABLE + ";", null);
    resultSet.moveToFirst();

    for (int i = 0; i < resultSet.getCount(); ++i)
    {
      list.add(new DBProject(
          this,
          resultSet.getInt(ID_COL_INDEX),
          resultSet.getString(PROJECT_COL_INDEX),
          getTotalHours(resultSet.getInt(ID_COL_INDEX)),
          getTotalMinutes(resultSet.getInt(ID_COL_INDEX)))
      );
      resultSet.move(1);
    }

    return list;
  }

  private int getTotalHours (int projectId)
  {
    Cursor resultSet = db.rawQuery("SELECT SUM(" + ENTRY_HOUR_COLUMN + ") FROM " + ENTRY_TABLE + " WHERE " + ENTRY_PROJECT_COLUMN + " = " + projectId + ";", null);
    resultSet.moveToFirst();
    return resultSet.getInt(0);
  }

  private int getTotalMinutes (int projectId)
  {
    Cursor resultSet = db.rawQuery("SELECT SUM(" + ENTRY_MINUTE_COLUMN + ") FROM " + ENTRY_TABLE + " WHERE " + ENTRY_PROJECT_COLUMN + " = " + projectId + ";", null);
    resultSet.moveToFirst();
    return resultSet.getInt(0);
  }

  public DBEntry getEmptyEntry (Calendar cal)
  {
    entryTable();
    ContentValues values = new ContentValues();

    String date = getDateFormat(cal);
    DBProject project = getProjects().get(0);//Make sure projects aren't empty

    values.put (ENTRY_DATE_COLUMN, date);
    values.put (ENTRY_PROJECT_COLUMN, project.getId());
    values.put (ENTRY_HOUR_COLUMN, 0);
    values.put (ENTRY_MINUTE_COLUMN, 0);
    long id = db.insert (ENTRY_TABLE, null, values);

    DBEntry entry = new DBEntry(this, id, cal, project, 0, 0);

    return entry;
  }

  public ArrayList<DBEntry> getEntries (Calendar cal)
  {
    ArrayList<DBEntry> entries = new ArrayList<DBEntry>();
    entryTable();

    Cursor resultSet = db.rawQuery ("SELECT * FROM " + ENTRY_TABLE + " WHERE " + ENTRY_DATE_COLUMN + " = \"" + getDateFormat(cal) + "\";", null);
    resultSet.moveToFirst();

    for (int i = 0; i < resultSet.getCount(); ++i)
    {
      entries.add(new DBEntry(this,
          resultSet.getInt(ID_COL_INDEX),
          cal,
          getProject(resultSet.getInt(ENTRY_PROJECT_INDEX)),
          resultSet.getInt(ENTRY_HOUR_INDEX),
          resultSet.getInt(ENTRY_MINUTE_INDEX)));
      resultSet.move(1);
    }

    return entries;
  }

  public static String getDateFormat (Calendar cal)
  {
    return cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.US) + " "
        + cal.get(Calendar.DAY_OF_MONTH) + " "
        + cal.get(Calendar.YEAR);
  }

  public void resetDatabase ()
  {
    db.execSQL("DROP TABLE IF EXISTS " + SINGLE_VALUE_TABLE + ";");
    db.execSQL("DROP TABLE IF EXISTS " + ENTRY_TABLE + ";");
    db.execSQL("DROP TABLE IF EXISTS " + PROJECTS_TABLE + ";");
  }
}
