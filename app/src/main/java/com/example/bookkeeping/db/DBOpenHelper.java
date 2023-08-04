package com.example.bookkeeping.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBOpenHelper extends SQLiteOpenHelper {
    public DBOpenHelper(@Nullable Context context) {
        super(context, "tally.db", null, 1);
    }

    // 项目第一次允许时才会被调用
    @Override
    public void onCreate(SQLiteDatabase db) {
        // 建表
        String sql = "create table type_tb(" +
                "id integer primary key autoincrement," +
                "typename varchar(10)," +
                "imageId integer," +
                "sImageId integer," +
                "kind integer)";
        db.execSQL(sql);
    }

    // 数据库版本发生改变时才会被调用
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
