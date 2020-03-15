package com.example.ttett.Dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.ttett.Entity.Email;
import com.example.ttett.Entity.EmailMessage;
import com.example.ttett.MailSqlite.MyDatabaseHelper;


public class MailDao {

    private final MyDatabaseHelper mHelper;

    private Context mContext;

    public MailDao(Context context) {
        mHelper = new MyDatabaseHelper(context);
        this.mContext = context;
    }

    public int QueryMessageCount(Email email){
        SQLiteDatabase db = mHelper.getWritableDatabase();
        Cursor cursor = db.query("EMAILMESSAGE", new String[]{"isSend"},"from_mail = ?",
                new String[]{email.getName()+"<"+email.getAddress()+">"},null,null,null,null);
        return cursor.getCount();
    }

    /**
     * 创建邮件表
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
//            values.put("cc",);
//            values.put("bcc");
            values.put("content",emailMessage.getMessage_text());
            values.put("sendDate",emailMessage.getSendDate());
            values.put("isRead",emailMessage.getIsRead());
            values.put("isSend",emailMessage.getIsSend());
            values.put("isDelete",emailMessage.getIsDelete());
            db.insert("EMAILMESSAGE",null,values);
            values.clear();
            db.close();
    }

//    public List<Mail> queryAllMessages(){
////        SQLiteDatabase db = mHelper.getWritableDatabase();
////        Cursor cursor = db.query("MAIL", new String[]{"message_id"},"message_id = ?",
////                new String[]{message_id},null,null,null,null);
////        return cursor.moveToFirst();
//        return
//    }
}
