package com.xiaoshq.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ivan on 2018/1/2.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "data.db";
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME_USER = "user";
    private static final String TABLE_NAME_SOL = "solution";

    private static final String SQL_CREATE_TABLE_USER = "create table " + TABLE_NAME_USER
            + " (userId integer primary key autoincrement,"
            + " userName text,"
            + " password text);";
    private static final String SQL_CREATE_TABLE_SOL = "create table " + TABLE_NAME_SOL
            + " (solId integer primary key autoincrement,"
            + " userId integer,"
            + " solName text,"
            + " detail text);";

    public DatabaseHelper(Context context) {
        // 创建数据库访问对象，实际上没有创建数据库，马上返回。
        // 只有调用getWritableDatabase()或getReadableDatabase()时才会创建数据库
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // 第一次调用getWritableDatabase()或getReadableDatabase()时调用
        //创建数据库表
        // 用户（ID，账户名，密码）
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_USER);
        // 控制（solution ID，用户（拥有者）ID，solution名，solution内容）
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_SOL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // DB_VERSION变化时调用
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_USER);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_SOL);
        onCreate(sqLiteDatabase);
    }
}