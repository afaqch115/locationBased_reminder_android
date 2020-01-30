package com.example.reminder.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;

import com.example.reminder.models.PlaceInfo;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper
{

    private static final String db_name="reminder.db";
    private static final String db_table="Task";
    private  static final int db_version=1;
    private static final String col1="id";
    private static final String col2="name";
    private static final String col3="detail";
    private static final String col4="latitude";
    private static final String col5="longitude";

    public DatabaseHelper(Context context)
    {
        super(context, db_name,null, db_version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query=String.format("CREATE TABLE %s (id INTEGER PRIMARY KEY AUTOINCREMENT,%s TEXT,%s TEXT,%s DOUBLE, %s DOUBLE);",db_table,col2,col3,col4,col5);
        db.execSQL(query);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query=String.format("DROP TABLE IF EXISTS %s",db_table);
        db.execSQL(query);
        onCreate(db);

    }

    public void insert(String name, String detail, PlaceInfo p)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(col2,name);
        contentValues.put(col3,detail);
        contentValues.put(col4,p.getLatLng().latitude);
        contentValues.put(col5,p.getLatLng().longitude);

        db.insertWithOnConflict(db_table,null,contentValues,SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }
    public Integer delete(String name,String detail)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        int count=db.delete(db_table,"name=? and detail=?",new String[]{name,detail});
        db.close();
        return count;
    }

    public ArrayList<String> getName()
    {
        ArrayList<String> name=new ArrayList<String>();
        SQLiteDatabase db=this.getReadableDatabase();

        Cursor cursor=db.query(db_table,new String[]{col2},null,null,null,null,null);
        if(cursor!=null && cursor.getCount()>0)
        {
            while (cursor.moveToNext())
            {
                int index=cursor.getColumnIndex(col2);
                name.add(cursor.getString(index));
            }
        }

        cursor.close();
        db.close();
        return name;
    }
    public ArrayList<String> getDetail()
    {
        ArrayList<String> detail=new ArrayList<String>();
        SQLiteDatabase db=this.getReadableDatabase();

        Cursor cursor=db.query(db_table,new String[]{col3},null,null,null,null,null);
        if(cursor!=null && cursor.getCount()>0)
        {
            while (cursor.moveToNext())
            {
                int index=cursor.getColumnIndex(col3);
                detail.add(cursor.getString(index));
            }
        }
        cursor.close();
        db.close();
        return detail;
    }
    public  ArrayList<Double> getLat()
    {
        ArrayList<Double> lat=new ArrayList<>();

        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.query(db_table,new String[]{col4},null,null,null,null,null);
        if(cursor!=null && cursor.getCount()>0)
        {
            while (cursor.moveToNext())
            {
                int index=cursor.getColumnIndex(col4);
                lat.add(cursor.getDouble(index));
            }
        }
        cursor.close();
        db.close();
        return lat;


    }
    public  ArrayList<Double> getLng()
    {
        ArrayList<Double> lng=new ArrayList<>();

        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.query(db_table,new String[]{col5},null,null,null,null,null);
        if(cursor!=null && cursor.getCount()>0)
        {
            while (cursor.moveToNext())
            {
                int index=cursor.getColumnIndex(col5);
                lng.add(cursor.getDouble(index));
            }
        }
        cursor.close();
        db.close();
        return lng;


    }

}
