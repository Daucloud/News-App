package com.java.zhangxinyuan.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class HistoryManager {
    private static DBHelper dbHelper;

    public HistoryManager(Context context) {
        dbHelper = new DBHelper(context);
    }

    // Insert a history record
    public void insertHistory(String unikey, String json) {
        if (!isHistoryExists(unikey)) {

            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(DBHelper.COLUMN_HISTORY_UNIKEY, unikey);
            values.put(DBHelper.COLUMN_HISTORY_JSON, json);
            db.insert(DBHelper.TABLE_HISTORY, null, values);
            db.close();
        }
    }

public List<NewsInfo.DataDTO> getAllHistoryJson() {
        List<NewsInfo.DataDTO> jsonList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // 检查是否成功打开数据库
        if (db == null || !db.isOpen()) {
            throw new IllegalStateException("Database not open");
        }

        Cursor cursor = db.query(DBHelper.TABLE_HISTORY,
                                 new String[]{DBHelper.COLUMN_HISTORY_JSON},
                                 null,
                                 null,
                                 null,
                                 null,
                                 DBHelper.COLUMN_HISTORY_ID + " DESC");

        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    do {
                        String json = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_HISTORY_JSON));
                        jsonList.add(new Gson().fromJson(json, NewsInfo.DataDTO.class));
                    } while (cursor.moveToNext());
                }
            } finally {
                cursor.close();
            }
        }

        return jsonList;
    }


    // Delete a specific history record
    public void deleteHistory(String unikey) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(DBHelper.TABLE_HISTORY, DBHelper.COLUMN_HISTORY_UNIKEY + " = ?", new String[]{unikey});
        db.close();
    }

    // Delete all history records
    public void deleteAllHistory() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(DBHelper.TABLE_HISTORY, null, null);
        db.close();
    }

    // Update a specific history record
    public void updateHistory(String unikey, String newJson) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_HISTORY_JSON, newJson);
        db.update(DBHelper.TABLE_HISTORY, values, DBHelper.COLUMN_HISTORY_UNIKEY + " = ?", new String[]{unikey});
        db.close();
    }

    // Check if a specific history record exists
    public boolean isHistoryExists(String unikey) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DBHelper.TABLE_HISTORY, null, DBHelper.COLUMN_HISTORY_UNIKEY + " = ?", new String[]{unikey}, null, null, null);
        boolean exists = cursor.moveToFirst();
        cursor.close();
        db.close();
        return exists;
    }

}
