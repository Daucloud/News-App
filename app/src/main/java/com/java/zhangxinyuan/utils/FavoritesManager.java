package com.java.zhangxinyuan.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class FavoritesManager {
    private DBHelper dbHelper;

    public FavoritesManager(Context context) {
        dbHelper = new DBHelper(context);
    }

    // Insert a favorite record
    public void insertFavorite(String unikey, String json) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_FAVORITES_UNIKEY, unikey);
        values.put(DBHelper.COLUMN_FAVORITES_JSON, json);
        db.insert(DBHelper.TABLE_FAVORITES, null, values);
        db.close();
    }
    // Delete a specific favorite record
    public void deleteFavorite(String unikey) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(DBHelper.TABLE_FAVORITES, DBHelper.COLUMN_FAVORITES_UNIKEY + " = ?", new String[]{unikey});
        db.close();
    }

    // Delete all favorite records
    public void deleteAllFavorites() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(DBHelper.TABLE_FAVORITES, null, null);
        db.close();
    }

    // Update a specific favorite record
    public void updateFavorite(String unikey, String newJson) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_FAVORITES_JSON, newJson);
        db.update(DBHelper.TABLE_FAVORITES, values, DBHelper.COLUMN_FAVORITES_UNIKEY + " = ?", new String[]{unikey});
        db.close();
    }

    // Check if a specific favorite record exists
    public boolean isFavoriteExists(String unikey) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DBHelper.TABLE_FAVORITES, null, DBHelper.COLUMN_FAVORITES_UNIKEY + " = ?", new String[]{unikey}, null, null, null);
        boolean exists = cursor.moveToFirst();
        cursor.close();
        db.close();
        return exists;
    }

    public List<NewsInfo.DataDTO> getAllFavoritesJSON() {
        List<NewsInfo.DataDTO> jsonList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // 检查是否成功打开数据库
        if (db == null || !db.isOpen()) {
            throw new IllegalStateException("Database not open");
        }

        Cursor cursor = db.query(DBHelper.TABLE_FAVORITES,
                new String[]{DBHelper.COLUMN_FAVORITES_JSON},
                null,
                null,
                null,
                null,
                DBHelper.COLUMN_FAVORITES_ID+ " DESC");

        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    do {
                        String json = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_FAVORITES_JSON));
                        jsonList.add(new Gson().fromJson(json, NewsInfo.DataDTO.class));
                    } while (cursor.moveToNext());
                }
            } finally {
                cursor.close();
            }
        }

        return jsonList;
    }
}
