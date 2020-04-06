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
    private static MyDatabaseHelper mHelper = null;

    private Context mContext;

    public AttachmentDao(Context context) {
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
        values.put("path",attachment.getPath());
        db.insert("ATTACHMENT",null,values);
        values.clear();
        db.close();
    }

    /**
     * 查询邮件里的附件
     * @param message_id
     * @return
     */
    public List<Attachment> QueryMessageAttachment(String message_id){
        SQLiteDatabase db = mHelper.getWritableDatabase();
        Cursor cursor = db.query("ATTACHMENT", null,"message_id = ?",
                new String[]{message_id},null,null,null,null);
        Log.d(TAG,"查询了+"+cursor.getCount());
        return attachments(cursor);
    }

    /**
     * 查询邮箱里的附件
     * @param email_id
     * @return
     */
    public List<Attachment> QueryAllAttachment(int email_id){
        SQLiteDatabase db = mHelper.getWritableDatabase();
        Cursor cursor = db.query("ATTACHMENT", null,"email_id = ?",
                new String[]{String.valueOf(String.valueOf(email_id))},null,null,null,null);
        Log.d(TAG,"查询了+"+cursor.getCount());
        return attachments(cursor);
    }

    /**
     * 根据attachment_id查询里的附件
     * @param id
     * @return
     */
    public Attachment QuerySelectAttachment(int id){
        SQLiteDatabase db = mHelper.getWritableDatabase();
        Cursor cursor = db.query("ATTACHMENT", null,"id = ?",
                new String[]{String.valueOf(String.valueOf(id))},null,null,null,null);
        Attachment attachment;
        if(cursor.moveToFirst()){
            do {
                attachment = new Attachment();
                attachment.setAttachment_id(cursor.getInt(cursor.getColumnIndex("id")));
                attachment.setEmail_id(cursor.getInt(cursor.getColumnIndex("email_id")));
                attachment.setMessage_id(cursor.getString(cursor.getColumnIndex("message_id")));
                attachment.setType(cursor.getString(cursor.getColumnIndex("type")));
                attachment.setSize(cursor.getString(cursor.getColumnIndex("size")));
                attachment.setSaveDate(cursor.getString(cursor.getColumnIndex("saveDate")));
                attachment.setName(cursor.getString(cursor.getColumnIndex("name")));
                attachment.setPath(cursor.getString(cursor.getColumnIndex("path")));
            }while (cursor.moveToNext());
            cursor.close();
            return attachment;
        }
        return null;
    }

    /**
     * 设置属性返回attachment
     * @param cursor
     * @return
     */
    private List<Attachment> attachments(Cursor cursor){
        List<Attachment> attachments =new ArrayList<>();
        Attachment attachment;
        if(cursor.moveToFirst()){
            do {
                attachment = new Attachment();
                attachment.setAttachment_id(cursor.getInt(cursor.getColumnIndex("id")));
                attachment.setEmail_id(cursor.getInt(cursor.getColumnIndex("email_id")));
                attachment.setMessage_id(cursor.getString(cursor.getColumnIndex("message_id")));
                attachment.setType(cursor.getString(cursor.getColumnIndex("type")));
                attachment.setSize(cursor.getString(cursor.getColumnIndex("size")));
                attachment.setSaveDate(cursor.getString(cursor.getColumnIndex("saveDate")));
                attachment.setName(cursor.getString(cursor.getColumnIndex("name")));
                attachment.setPath(cursor.getString(cursor.getColumnIndex("path")));
                attachments.add(attachment);
            }while (cursor.moveToNext());
            cursor.close();
            return attachments;
        }
        cursor.close();
        return null;
    }
}
