package com.example.arvind.shortfi;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import android.content.Context;
import android.content.ContentValues;

public class MyDBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "detail.db";
    public static final String TABLE_NAME = "userdetail";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_USERNAME = "user";
    public static final String COLUMN_PASSWORD = "pass";

    //We need to pass database information along to superclass
    public MyDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USERNAME + " TEXT, " +COLUMN_PASSWORD + " TEXT " +
                ");";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    //Add a new row to the database
    public void addProduct(String usern,String passw){
        ContentValues values = new ContentValues();
        SQLiteDatabase db = getWritableDatabase();
        values.put(COLUMN_USERNAME, usern);
        values.put(COLUMN_PASSWORD, passw);
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public Boolean UserDetail(String usern,String passw){
        String dbString = "";
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE "+COLUMN_USERNAME+"="
                +"'"+usern+"'"+" AND "+COLUMN_PASSWORD+"="+"'"+passw+"'";

        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        if(c.getCount()>0)
        {
            return true;
        }
        c.close();
        db.close();
        return false;
    }

}
