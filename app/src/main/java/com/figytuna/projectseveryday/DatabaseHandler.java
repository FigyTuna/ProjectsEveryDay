package com.figytuna.projectseveryday;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.Calendar;

public class DatabaseHandler {

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
    private static final String ENTRY_PROJECT_COLUMN = "project_id_col";
    private static final int ENTRY_PROJECT_INDEX = 1;
    private static final String ENTRY_DATE_COLUMN = "date_col";
    private static final int ENTRY_DATE_INDEX = 2;

    private SQLiteDatabase db;

    public DatabaseHandler (Context context)
    {
        db = context.openOrCreateDatabase(DATABASE_NAME, Context.MODE_PRIVATE, null);
    }

    private void singleValuesTable ()
    {
        db.execSQL ("CREATE TABLE IF NOT EXISTS "
                + SINGLE_VALUE_TABLE + "("
                + ID_COLUMN + " INT, "
                + MINUTE_COLUMN + " INT, "
                + HOUR_COLUMN + " INT, "
                + NOTIF_COLUMN + " INT"
                + ");");

        Cursor resultSet = db.rawQuery ("SELECT * from " + SINGLE_VALUE_TABLE + ";", null);

        if (resultSet.getCount () < 1)
        {
            db.execSQL ("INSERT INTO " + SINGLE_VALUE_TABLE + " VALUES(" + DEFAULT_ID + ", "
                    + DEFAULT_MINUTE + ", " + DEFAULT_HOUR + ", " + 1 + ");");
        }
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

    public void resetDatabase ()
    {
        db.execSQL("DROP TABLE IF EXISTS " + SINGLE_VALUE_TABLE + ";");
    }
}
