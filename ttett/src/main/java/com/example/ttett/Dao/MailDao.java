package com.example.ttett.Dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.ttett.Entity.Email;
import com.example.ttett.Entity.EmailMessage;
import com.example.ttett.MailSqlite.MyDatabaseHelper;

import java.util.ArrayList;
import java.util.List;


public class MailDao {

    private static final String TAG = "MailDao.this" ;
    private final MyDatabaseHelper mHelper;

    private Context mContext;

    public MailDao(Context context) {
        mHelper = new MyDatabaseHelper(context);
        this.mContext = context;

    }

    /**
     * 查询SQLite存放邮件数量
     * @param email 每个邮件账号
     * @return
     */
    public int QueryMessageCount(Email email){
        SQLiteDatabase db = mHelper.getWritableDatabase();
        Cursor cursor = db.query("EMAILMESSAGE", new String[]{"isSend"},"to_mail = ?",
                new String[]{email.getName()+"<"+email.getAddress()+">"},null,null,null,null);
        Log.d(TAG,"存放了+"+cursor.getCount());
        return cursor.getCount();
    }

    /**
     * 创建MAIL.db
     */
    public void CreateMessageTable(){
        MyDatabaseHelper helper = new MyDatabaseHelper(mContext);
        helper.getWritableDatabase();
    };

    /**
     * 判断邮件是否存在
     * @param message_id
     * @return
     */
    public Boolean isExistMail(String message_id){
        SQLiteDatabase db = mHelper.getWritableDatabase();
        Cursor cursor = db.query("EMAILMESSAGE", new String[]{"message_id"},"message_id = ?",
                new String[]{message_id},null,null,null,null);
        return cursor.moveToFirst();
    }

    /**
     * 插入邮件信息
     * @param emailMessage
     */
    public void InsertMessages(EmailMessage emailMessage){

        SQLiteDatabase db = mHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
            values.put("message_id",emailMessage.getMessage_id());
            values.put("subject",emailMessage.getSubject());
            values.put("from_mail",emailMessage.getFrom());
            values.put("to_mail",emailMessage.getTo());
            values.put("cc",emailMessage.getCc());
            values.put("bcc",emailMessage.getBcc());
            values.put("content",emailMessage.getMessage_text());
            values.put("sendDate",emailMessage.getSendDate());
            values.put("isRead",emailMessage.getIsRead());
            values.put("isSend",emailMessage.getIsSend());
            values.put("isDelete",emailMessage.getIsDelete());
            db.insert("EMAILMESSAGE",null,values);
            values.clear();
            db.close();
    }

    /**
     * 读取SQLite存放的邮件
     * @param email 每个邮件账号
     * @return
     */
    public List<EmailMessage> QueryAllMessage(Email email){
        SQLiteDatabase db = mHelper.getWritableDatabase();
        Cursor cursor = db.query("EMAILMESSAGE", null,"to_mail = ?",
                new String[]{email.getName()+"<"+email.getAddress()+">"},null,null,"to_mail desc",null);
        Log.d(TAG,"查询了+"+cursor.getCount());

        List<EmailMessage> messages =new ArrayList<>();
        EmailMessage message;
        if(cursor.moveToFirst()){
            do {
                message = new EmailMessage();
                message.setSubject(cursor.getString(cursor.getColumnIndex("subject")));
                message.setFrom(cursor.getString(cursor.getColumnIndex("from_mail")));
                message.setTo(cursor.getString(cursor.getColumnIndex("to_mail")));
                message.setCc(cursor.getString(cursor.getColumnIndex("cc")));
                message.setBcc(cursor.getString(cursor.getColumnIndex("bcc")));
                message.setContent(cursor.getString(cursor.getColumnIndex("content")));
                message.setSendDate(cursor.getString(cursor.getColumnIndex("sendDate")));
                message.setIsRead(cursor.getInt(cursor.getColumnIndex("isRead")));
                message.setIsSend(cursor.getInt(cursor.getColumnIndex("isSend")));
                message.setIsDelete(cursor.getInt(cursor.getColumnIndex("isDelete")));
                messages.add(message);
            }while (cursor.moveToNext());
            cursor.close();
            return messages;
        }
        cursor.close();
        return null;
    }
}
