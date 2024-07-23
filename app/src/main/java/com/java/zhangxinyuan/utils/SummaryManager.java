package com.java.zhangxinyuan.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class SummaryManager {
    private static DBHelper dbHelper;

    public SummaryManager(Context context) {
        dbHelper = new DBHelper(context);
    }

    // Insert a summary record
    public void insertSummary(String unikey, String json) {
        if (!isSummaryExists(unikey)) {

            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(DBHelper.COLUMN_SUMMARY_UNIKEY, unikey);
            values.put(DBHelper.COLUMN_SUMMARY_SUMMARY, json);
            db.insert(DBHelper.TABLE_SUMMARY, null, values);
            db.close();
        }
    }

    // Delete a specific summary record
    public void deleteSummary(String unikey) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(DBHelper.TABLE_SUMMARY, DBHelper.COLUMN_HISTORY_UNIKEY + " = ?", new String[]{unikey});
        db.close();
    }

    // Delete all summary records
    public void deleteAllSummary() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(DBHelper.TABLE_SUMMARY, null, null);
        db.close();
    }

    // Update a specific summary record
    public void updateSummary(String unikey, String newJson) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_SUMMARY_SUMMARY, newJson);
        db.update(DBHelper.TABLE_SUMMARY, values, DBHelper.COLUMN_HISTORY_UNIKEY + " = ?", new String[]{unikey});
        db.close();
    }


    // Check if a specific summary record exists
    public boolean isSummaryExists(String unikey) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DBHelper.TABLE_SUMMARY, null, DBHelper.COLUMN_HISTORY_UNIKEY + " = ?", new String[]{unikey}, null, null, null);
        boolean exists = cursor.moveToFirst();
        cursor.close();
        db.close();
        return exists;
    }

    public String getSummary(String unikey) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DBHelper.TABLE_SUMMARY, null, DBHelper.COLUMN_HISTORY_UNIKEY + " = ?", new String[]{unikey}, null, null, null);
        cursor.moveToFirst();
        String summary= cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_SUMMARY_SUMMARY));
        cursor.close();
        db.close();
        return summary;
    }

}
