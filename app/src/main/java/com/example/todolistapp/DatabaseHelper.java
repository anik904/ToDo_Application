package com.example.todolistapp;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.icu.text.AlphabeticIndex;

import java.io.IOException;
import java.util.ArrayList;


public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "TodoDatabase";
    public static final String TODO_TABLE_NAME = "TodoItem";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(
                    "create table " + TODO_TABLE_NAME + "(id INTEGER PRIMARY KEY,task)"
            );

        } catch (SQLException e) {
            try {
                throw new IOException(e);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TODO_TABLE_NAME);
        onCreate(db);
    }


    public void insert(String s) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("task", s);

        db.replace(TODO_TABLE_NAME, null, contentValues);

    }

    @SuppressLint("Range")
    public ArrayList<String> getAllTask() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> array_list = new ArrayList<String>();
        Cursor res = db.rawQuery("select*from " + TODO_TABLE_NAME, null);
        res.moveToFirst();
        while (!res.isAfterLast()) {
            array_list.add(res.getString(res.getColumnIndex("task")));
            res.moveToNext();
        }
        return array_list;

    }

    public void updateTask(String oldTask, String UpdatedTask){
        SQLiteDatabase sqLiteDB = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("task", UpdatedTask);

        sqLiteDB.update(TODO_TABLE_NAME, contentValues,"task=?", new String[]{oldTask});
        sqLiteDB.close();
    }

    public void deleteInfo(String taskItem){
        SQLiteDatabase sqLiteDB = this.getWritableDatabase();
        sqLiteDB.delete(TODO_TABLE_NAME,"task=?",new String[]{taskItem});
        sqLiteDB.close();
    }
}
