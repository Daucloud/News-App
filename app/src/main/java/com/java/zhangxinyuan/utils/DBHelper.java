package com.java.zhangxinyuan.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "news.db";
    public static final int DATABASE_VERSION = 1;

    // Table names and columns
    // History table
    public static final String TABLE_HISTORY = "history";
    public static final String COLUMN_HISTORY_ID = "id";
    public static final String COLUMN_HISTORY_UNIKEY = "unikey";
    public static final String COLUMN_HISTORY_JSON= "json";

    // Favorites table
    public static final String TABLE_FAVORITES = "favorites";
    public static final String COLUMN_FAVORITES_ID = "id";
    public static final String COLUMN_FAVORITES_UNIKEY= "unikey";
    public static final String COLUMN_FAVORITES_JSON= "json";

    // SQL statement to create the history table
    private static final String TABLE_HISTORY_CREATE =
            "CREATE TABLE " + TABLE_HISTORY + " (" +
                    COLUMN_HISTORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_HISTORY_UNIKEY+ " TEXT, " +
                    COLUMN_HISTORY_JSON+ " TEXT" +
                    ");";

    // SQL statement to create the favorites table
    private static final String TABLE_FAVORITES_CREATE =
            "CREATE TABLE " + TABLE_FAVORITES + " (" +
                    COLUMN_FAVORITES_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_FAVORITES_UNIKEY+ " TEXT, " +
                    COLUMN_FAVORITES_JSON+ " TEXT" +
                    ");";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_HISTORY_CREATE);
        db.execSQL(TABLE_FAVORITES_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HISTORY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITES);
        onCreate(db);
    }
}
