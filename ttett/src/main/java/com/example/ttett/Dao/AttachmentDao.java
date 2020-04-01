package com.example.ttett.Dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.ttett.Entity.Attachment;
import com.example.ttett.MailSqlite.MyDatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class AttachmentDao {

    private static final String TAG ="AttachmentDao" ;
    private final MyDatabaseHelper mHelper;

    private Context mContext;

    public AttachmentDao(Context context) {
        mHelper = new MyDatabaseHelper(context);
        this.mContext = context;
    }

    /**
     * 判断User是否存在
     * @param name
     * @return
     */
    public int isExistName(String name){
        SQLiteDatabase db = mHelper.getWritableDatabase();
        Cursor cursor = db.query("ATTACHMENT", new String[]{"id"},"name = ?",
                new String[]{name},null,null,null,null);
        return cursor.getCount();
    }

    /**
     * 添加附件
     * @param attachment
     */
    public void InsertAttachment(Attachment attachment){
        SQLiteDatabase db = mHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("message_id",attachment.getMessage_id());
        values.put("email_id",attachment.getEmail_id());
        values.put("name",attachment.getName());
        values.put("type",attachment.getType());
        values.put("size",attachment.getSize());
        values.put("saveDate",attachment.getSaveDate());
        db.insert("ATTACHMENT",null,values);
        values.clear();
        db.close();
    }

    public List<Attachment> QueryMessageAttachment(String message_id){
        SQLiteDatabase db = mHelper.getWritableDatabase();
        Cursor cursor = db.query("ATTACHMENT", null,"message_id = ?",
                new String[]{message_id},null,null,null,null);
        Log.d(TAG,"查询了+"+cursor.getCount());

        List<Attachment> attachments =new ArrayList<>();
        Attachment attachment;
        if(cursor.moveToFirst()){
            do {
                attachment = new Attachment();
                attachment.setEmail_id(cursor.getInt(cursor.getColumnIndex("email_id")));
                attachment.setMessage_id(cursor.getString(cursor.getColumnIndex("message_id")));
                attachment.setType(cursor.getString(cursor.getColumnIndex("type")));
                attachment.setSize(cursor.getString(cursor.getColumnIndex("size")));
                attachment.setSaveDate(cursor.getString(cursor.getColumnIndex("saveDate")));
                attachment.setName(cursor.getString(cursor.getColumnIndex("name")));
                attachments.add(attachment);
            }while (cursor.moveToNext());
            cursor.close();
            return attachments;
        }
        cursor.close();
        return null;
    }

    public List<Attachment> QueryAllAttachment(int email_id){
        SQLiteDatabase db = mHelper.getWritableDatabase();
        Cursor cursor = db.query("ATTACHMENT", null,"email_id = ?",
                new String[]{String.valueOf(String.valueOf(email_id))},null,null,null,null);
        Log.d(TAG,"查询了+"+cursor.getCount());

        List<Attachment> attachments =new ArrayList<>();
        Attachment attachment;
        if(cursor.moveToFirst()){
            do {
                attachment = new Attachment();
                attachment.setEmail_id(cursor.getInt(cursor.getColumnIndex("email_id")));
                attachment.setMessage_id(cursor.getString(cursor.getColumnIndex("message_id")));
                attachment.setType(cursor.getString(cursor.getColumnIndex("type")));
                attachment.setSize(cursor.getString(cursor.getColumnIndex("size")));
                attachment.setSaveDate(cursor.getString(cursor.getColumnIndex("saveDate")));
                attachment.setName(cursor.getString(cursor.getColumnIndex("name")));
                attachments.add(attachment);
            }while (cursor.moveToNext());
            cursor.close();
            return attachments;
        }
        cursor.close();
        return null;
    }
}
