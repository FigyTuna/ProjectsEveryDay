package com.figytuna.projectseveryday;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.Calendar;

public class DatabaseHandler {

    private static final String DATABASE_NAME = "ped_db";
    private static final String SINGLE_VALUE_TABLE = "single_value_table";

    private static final String ID_COLUMN = "id_col";
    private static final String MINUTE_COLUMN = "minute_col";
    private static final String HOUR_COLUMN = "hour_col";
    private static final String NOTIF_COLUMN = "notification_col";

    private static final int DEFAULT_ID = 1;
    private static final int DEFAULT_MINUTE = 0;
    private static final int DEFAULT_HOUR = 12;

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
            Log.v ("Reset", "Insert");
            db.execSQL ("INSERT INTO " + SINGLE_VALUE_TABLE + " VALUES(" + DEFAULT_ID + ", "
                    + DEFAULT_MINUTE + ", " + DEFAULT_HOUR + ", " + 1 + ");");
        }
    }

    public void updateNotifTime (int minute, int hour)
    {
        singleValuesTable();

        Log.v ("set", "m: " + minute + " h: " + hour);

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
        cal.set (Calendar.MINUTE, resultSet.getInt (1));
        cal.set (Calendar.HOUR_OF_DAY, resultSet.getInt (2));

        Log.v ("get", "m: " + cal.get(Calendar.MINUTE) + " h: " + cal.get(Calendar.HOUR));

        return cal;
    }

    public void resetDatabase ()
    {
        db.execSQL("DROP TABLE IF EXISTS " + SINGLE_VALUE_TABLE + ";");
    }
}
