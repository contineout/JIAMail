package com.example.ttett.Dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.ttett.Entity.Email;
import com.example.ttett.MailSqlite.MyDatabaseHelper;

import java.util.ArrayList;
import java.util.List;


public class EmailDao {

    private static final String TAG = "EmailDao.this" ;
    private static MyDatabaseHelper mHelper = null;

    private Context mContext;

    public EmailDao(Context context) {
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
     * 查询SQLite存放邮件箱数量
     * @param user_id 每个邮件账号
     * @return
     */
    public int QueryEmailCount(int user_id){
        SQLiteDatabase db = mHelper.getWritableDatabase();
        Cursor cursor = db.query("EMAIL", new String[]{"id"},"user_id = ?",
                new String[]{String.valueOf(user_id)},null,null,null,null);
        Log.d(TAG,"存放了+"+cursor.getCount());
        return cursor.getCount();
    }

    /**
     * 判断邮箱是否存在
     * @param email_address
     * @return
     */
    public Boolean isExistEmail(String email_address){
        SQLiteDatabase db = mHelper.getWritableDatabase();
        Cursor cursor = db.query("EMAIL", null,"email_address = ?",
                new String[]{String.valueOf(email_address)},null,null,null,null);
        return cursor.moveToFirst();
    }

    /**
     * 修改邮箱邮件数量
     * @param email
     * @return
     */
    public void updateMessageCount(Email email){
        SQLiteDatabase db = mHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("message_count",email.getMessage_count());
        db.update("EMAIL",values,"id = ?",new String[]{String.valueOf(email.getEmail_id())});
    }

    /**
     * 插入邮箱信息
     * @param email
     */
    public boolean InsertEmail(Email email){
        SQLiteDatabase db = mHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
            values.put("user_id",email.getUser_id());
            values.put("email_type",email.getType());
            values.put("email_address",email.getAddress());
            values.put("AuthorizationCode",email.getAuthorizationCode());
            values.put("email_name", email.getName());
            values.put("message_count",email.getMessage_count());
            values.put("avatar_color",email.getAvatar_color());
            db.insert("EMAIL",null,values);
            values.clear();
            db.close();
            return isExistEmail(email.getAddress());
    }

    /**
     * 读取SQLite存放的邮箱
     * @param user_id 每个邮件账号
     * @return
     */
    public List<Email> QueryAllEmail(int user_id){
        SQLiteDatabase db = mHelper.getWritableDatabase();
        Cursor cursor = db.query("EMAIL", null,"user_id = ?",
                new String[]{String.valueOf(user_id)},null,null,null,null);
        Log.d(TAG,"查询了+"+cursor.getCount());

        List<Email> emails =new ArrayList<>();
        Email email;
        if(cursor.moveToFirst()){
            do {
                email = new Email();
                email.setEmail_id(cursor.getInt(cursor.getColumnIndex("id")));
                email.setUser_id(cursor.getInt(cursor.getColumnIndex("user_id")));
                email.setType(cursor.getString(cursor.getColumnIndex("email_type")));
                email.setAddress(cursor.getString(cursor.getColumnIndex("email_address")));
                email.setAuthorizationCode(cursor.getString(cursor.getColumnIndex("AuthorizationCode")));
                email.setName(cursor.getString(cursor.getColumnIndex("email_name")));
                email.setMessage_count(cursor.getInt(cursor.getColumnIndex("message_count")));
                email.setAvatar_color(cursor.getString(cursor.getColumnIndex("avatar_color")));
                emails.add(email);
            }while (cursor.moveToNext());
            cursor.close();
            return emails;
        }
        cursor.close();
        return null;
    }

    /**
     * 读取SQLite存放的邮箱
     * @param address
     * @return
     */
    public Email QueryNewEmail(String address){
        SQLiteDatabase db = mHelper.getWritableDatabase();
        Cursor cursor = db.query("EMAIL", null,"email_address= ?",
                new String[]{address},null,null,null,null);
        Email email = null;
        if(cursor.moveToFirst()){
            do {
                email = new Email();
                email.setEmail_id(cursor.getInt(cursor.getColumnIndex("id")));
                email.setUser_id(cursor.getInt(cursor.getColumnIndex("user_id")));
                email.setType(cursor.getString(cursor.getColumnIndex("email_type")));
                email.setAddress(cursor.getString(cursor.getColumnIndex("email_address")));
                email.setAuthorizationCode(cursor.getString(cursor.getColumnIndex("AuthorizationCode")));
                email.setName(cursor.getString(cursor.getColumnIndex("email_name")));
                email.setAvatar_color(cursor.getString(cursor.getColumnIndex("avatar_color")));
            }while (cursor.moveToNext());
            cursor.close();
            Log.d(TAG,"查询了+"+cursor.getCount() + email.getAddress());
            return email;
        }
        cursor.close();
        return null;
    }

    /**
     * 读取SQLite存放的邮箱
     * @param email_id
     * @return
     */
    public Email QueryNewEmail(int email_id){
        SQLiteDatabase db = mHelper.getWritableDatabase();
        Cursor cursor = db.query("EMAIL", null,"id = ?",
                new String[]{String.valueOf(email_id)},null,null,null,null);
        Email email = null;
        if(cursor.moveToFirst()){
            do {
                email = new Email();
                email.setEmail_id(cursor.getInt(cursor.getColumnIndex("id")));
                email.setUser_id(cursor.getInt(cursor.getColumnIndex("user_id")));
                email.setType(cursor.getString(cursor.getColumnIndex("email_type")));
                email.setAddress(cursor.getString(cursor.getColumnIndex("email_address")));
                email.setAuthorizationCode(cursor.getString(cursor.getColumnIndex("AuthorizationCode")));
                email.setName(cursor.getString(cursor.getColumnIndex("email_name")));
                email.setAvatar_color(cursor.getString(cursor.getColumnIndex("avatar_color")));
            }while (cursor.moveToNext());
            cursor.close();
            return email;
        }
        cursor.close();
        return null;
    }

    public void deleteEMAIL(int id){
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.delete("EMAIL","id = ?",new String[]{String.valueOf(id)});
        db.delete("EMAILMESSAGE","email_id = ?",new String[]{String.valueOf(id)});
        db.delete("CONTACT","email_id = ?",new String[]{String.valueOf(id)});
        db.delete("FOLDER","email_id = ?",new String[]{String.valueOf(id)});
        db.delete("ATTACHMENT","email_id = ?",new String[]{String.valueOf(id)});
    }
}
