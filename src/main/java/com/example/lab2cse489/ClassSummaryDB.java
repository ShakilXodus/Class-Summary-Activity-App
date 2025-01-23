package com.example.lab2cse489;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ClassSummaryDB extends SQLiteOpenHelper {

    public ClassSummaryDB(Context context) {
        super(context, "ClassSummaryDB.db", null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        System.out.println("DB@OnCreate");
        String sql = "CREATE TABLE lectures  ("
                + "ID TEXT PRIMARY KEY,"

                + "course TEXT,"
                + "type TEXT,"
                + "datetime INT,"
                + "lecture TEXT,"
                + "topic TEXT,"
                + "summary TEXT"
                + ")";
        db.execSQL(sql);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        System.out.println("Write code to modify database schema here");

    }
    public void insertLecture( String ID,String userCourse, long userDate, String userLecture, String userTopic,String userSummary, String userType) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cols = new ContentValues();

        cols.put("ID", ID);

        cols.put("course", userCourse);
        cols.put("type", userType);
        cols.put("datetime", userDate);
        cols.put("lecture", userLecture);
		cols.put("topic",userTopic);
        cols.put("summary", userSummary);
        db.insert("lectures", null ,  cols);
        db.close();
    }
    public void updateLecture(String ID,String userCourse, long userDate, String userLecture, String userTopic,String userSummary, String userType) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cols = new ContentValues();

        cols.put("ID", ID);
        cols.put("course", userCourse);
        cols.put("type", userType);
        cols.put("datetime", userDate);
        cols.put("lecture", userLecture);
        cols.put("topic",userTopic);
        cols.put("summary", userSummary);

        db.update("lectures", cols, "ID=?", new String[ ] {ID} );
        db.close();
    }
    public void deleteLecture(String ID) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("lectures", "ID=?", new String[ ] {ID} );
        db.close();
    }
    public Cursor selectLectures(String query) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res= null;
        try {
            res = db.rawQuery(query, null);
        } catch (Exception e){
            e.printStackTrace();
        }
        return res;
    }
}
