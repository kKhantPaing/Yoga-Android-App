package com.example.coursework1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    // Declare Variables
    private static final String DATABASE_NAME = "yogaClass.db";
    private static final int DATABASE_VERSION = 1;
    private String res;
    SQLiteDatabase db = this.getReadableDatabase();

    // Table Setting Creation
    public static final String TABLE_SETTING = "setting";
    public static final String COLUMN_SETTING_USERNAME = "username";
    public static final String COLUMN_SETTING_PASSWORD = "password";
    public static final String COLUMN_SETTING_SAVED_PASSWORD = "save_password";
    public static final String COLUMN_SETTING_BASE_URL = "url";

    // Table Creation
    public static final String TABLE_TEACHERS = "teachers";

    // Table Creation
    public static final String TABLE_Classes = "classes";



    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    private String query;
    @Override
    public void onCreate(SQLiteDatabase db) {

        // Create Setting Table
        query = "CREATE TABLE IF NOT EXISTS " + TABLE_SETTING +
                "(" +
                COLUMN_SETTING_USERNAME + " TEXT, " +
                COLUMN_SETTING_PASSWORD + " TEXT, " +
                COLUMN_SETTING_SAVED_PASSWORD + " INT," +
                COLUMN_SETTING_BASE_URL + " TEXT " +
                ")";
        db.execSQL(query);

        // Insert default admin account
        ContentValues values = new ContentValues();
        values.put(COLUMN_SETTING_USERNAME, "admin");
        values.put(COLUMN_SETTING_PASSWORD, Helper.getMD5Hash("0000"));
        values.put(COLUMN_SETTING_SAVED_PASSWORD, 0); // password unsaved
        db.insert(TABLE_SETTING, null, values);

        // Create Teacher Table
        db.execSQL(query);

        // Create Class Table
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        query = "DROP TABLE IF EXISTS " + TABLE_SETTING;
        db.execSQL(query);
        query = "DROP TABLE IF EXISTS " + TABLE_Classes;
        db.execSQL(query);
        query = "DROP TABLE IF EXISTS " + TABLE_TEACHERS;
        db.execSQL(query);
        onCreate(db);
    }

    public String getBaseURL(){
        SQLiteDatabase db = this.getReadableDatabase();
        res = "";
        query = "Select " + COLUMN_SETTING_BASE_URL + " From " + TABLE_SETTING;
        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            res = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SETTING_BASE_URL));
        }
        cursor.close();
        db.close();
        return res;
    }

    // Check Username and Password
    public int login(String username, String password){
        SQLiteDatabase db = this.getReadableDatabase();
        query = "SELECT COUNT(*) FROM " + TABLE_SETTING + " WHERE " + COLUMN_SETTING_USERNAME + " = " + username +
                " AND " + COLUMN_SETTING_PASSWORD + " = " + Helper.getMD5Hash(password);
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        db.close();
        return count;
    }

    public boolean checkSavedLogin(){
        SQLiteDatabase db = this.getReadableDatabase();
        query = "SELECT " + COLUMN_SETTING_SAVED_PASSWORD + " FROM " + TABLE_SETTING;
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        db.close();
        return count > 1;
    }
}
