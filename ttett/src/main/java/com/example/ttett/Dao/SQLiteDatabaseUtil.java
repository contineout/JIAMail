package com.example.ttett.Dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.ttett.MailSqlite.MyDatabaseHelper;

public class SQLiteDatabaseUtil {
    private static MyDatabaseHelper mHelper = null;
    private SQLiteDatabase db;

    public static synchronized MyDatabaseHelper getInstance(Context context){
        if(mHelper == null){
            mHelper = new MyDatabaseHelper(context);
        }
        return mHelper;
    }
}
