package com.example.ttett.Dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.ttett.Entity.User;
import com.example.ttett.MailSqlite.MyDatabaseHelper;

public class UserDao {

    private static final String TAG = "UserDao.this" ;
    private static MyDatabaseHelper mHelper = null;

    private Context mContext;

    public UserDao(Context context) {
        this.mContext = context;
        mHelper = getInstance(mContext);
    }
    public synchronized MyDatabaseHelper getInstance(Context context){
        if(mHelper == null){
            mHelper = new MyDatabaseHelper(context);
        }
        return mHelper;
    }
    /**
     * 判断User是否存在
     * @param account
     * @return
     */
    public Boolean isExistMail(String account){
        SQLiteDatabase db = mHelper.getWritableDatabase();
        Cursor cursor = db.query("USER", new String[]{"id"},"account = ?",
                new String[]{account},null,null,null,null);
        return cursor.moveToFirst();
    }


    /**
     * 插入User信息
     * @param user
     */
    public Boolean InsertMessages(User user){

        SQLiteDatabase db = mHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id",user.getUser_id());
        values.put("account",user.getAccount());
        values.put("password",user.getPassword());
        db.insert("USER",null,values);
        values.clear();
        db.close();
        return isExistMail(user.getAccount());
    }
}
