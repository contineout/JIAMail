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
    private SQLiteDatabase db;
    private Context mContext;

    public MailDao(Context context) {
        this.mContext = context;
        db = SQLiteDatabaseUtil.getInstance(context).getWritableDatabase();
    }

    /**
     * 查询SQLite存放邮件数量
     * @param email 每个邮件账号
     * @return
     */
    public int QueryMessageCount(Email email){
        Cursor cursor = db.query("EMAILMESSAGE", new String[]{"isSend"},"to_mail LIKE ?",
                new String[]{"%"+ email.getAddress() + "%"},null,null,null,null);
        return cursor.getCount();
    }


    /**
     * message_id查询邮件所属Email_id
     * @param id
     * @return
     */
    public int QueryEmail_id(int id){
        Cursor cursor = db.query("EMAILMESSAGE", new String[]{"email_id"},"id = ?",
                new String[]{String.valueOf(id)},null,null,null,null);
        int email_id = 0;
        if(cursor.moveToFirst()){
            email_id = cursor.getInt(cursor.getColumnIndex("email_id"));
        }
        return email_id;
    }

    /**
     * 读取SQLite存放的邮件
     * @param email 每个邮件账号
     * @return
     */
    public List<EmailMessage> QueryAllMessage(Email email){
        Cursor cursor = db.query("EMAILMESSAGE", null,"to_mail LIKE ? and isDelete = ? and folder_id = ?",
                new String[]{("%"+ email.getAddress() + "%"),("0"),("1")},null,null,"to_mail desc",null);
        Log.d(TAG,"查询了+"+cursor.getCount());

        return setMessages(cursor);
    }

    /**
     * 读取对话邮件存放的邮件
     * @param address 每个邮件账号
     * @return
     */
    public List<EmailMessage> queryDialogMessage(String address,int email_id){
        Cursor cursor = db.query("EMAILMESSAGE", null,"from_mail LIKE ? and email_id = ? and not folder_id = ?",
                new String[]{("%"+ address + "%"),(String.valueOf(email_id)),("0")},null,null,"SendDate desc",null);
        Log.d(TAG,"查询了+"+cursor.getCount());
        return setMessages(cursor);
    }

    public List<EmailMessage> queryFolderMessage(int folder_id){
        Cursor cursor = db.query("EMAILMESSAGE", null,"folder_id = ?",
                new String[]{(String.valueOf(folder_id))},null,null,"SendDate desc",null);
        Log.d(TAG,"查询了+"+cursor.getCount());
        return setMessages(cursor);
    }

    /**
     * 是否有对话邮件
     * @param address
     * @return
     */
    public boolean queryIsDialogMessage(String address){
        Cursor cursor = db.query("EMAILMESSAGE", null,"to_mail LIKE ? or from_mail LIKE ?",
                new String[]{("%"+ address + "%"),("%"+ address + "%")},null,null,"SendDate desc",null);
        Log.d(TAG,"查询了+"+cursor.getCount());
        return cursor.moveToFirst();
    }


    /**
     * 查询SQLite已发送邮件数量
     * @param email isSend = 1;
     * @return
     */
    public List<EmailMessage> QuerySendedMessage(Email email){
        Cursor cursor = db.query("EMAILMESSAGE", null,"from_mail LIKE ? and isSend = ? and isDelete = ?",
                new String[]{("%"+ email.getAddress() + "%"),("1"),("0")},null,null,null,null);
        return setMessages(cursor);
    }


    /**
     * 查询SQLite未发送邮件数量
     * @param email isSend = 0;
     * @return
     */
    public List<EmailMessage> QueryDraftMessage(Email email){
        Log.d(TAG,"da"+ email.getAddress());
        Cursor cursor = db.query("EMAILMESSAGE", null,"from_mail = ? or from_mail = ? and isSend = ? ",
                new String[]{("%"+ email.getAddress() + "%"),(email.getAddress()),("0")},null,null,null,null);
        Log.d(TAG,"da"+cursor.getCount());
        return setMessages(cursor);
    }

    /**
     * 查询SQLite未读取邮件数量
     * @param email
     * @return
     */
    public List<EmailMessage> QueryUnReadMessage(Email email){
        Cursor cursor = db.query("EMAILMESSAGE", null,"to_mail LIKE ? and isRead = ? and isDelete = ?",
                new String[]{("%"+ email.getAddress() + "%"),("0"),("0")},null,null,null,null);
        return setMessages(cursor);
    }

    /**
     * 查询SQLite星标邮件数量
     * @param email
     * @return
     */
    public List<EmailMessage> QueryStarMessage(Email email){
        Cursor cursor = db.query("EMAILMESSAGE", null,"to_mail LIKE ? and isStar = ? and isDelete = ?",
                new String[]{("%"+ email.getAddress() + "%"),("1"),("0")},null,null,null,null);
        return setMessages(cursor);
    }

    /**
     * 查询SQLite已删除邮件数量
     * @param email
     * @return
     */
    public List<EmailMessage> QueryDelteMessage(Email email){
        Cursor cursor = db.query("EMAILMESSAGE", null,"to_mail LIKE ? and isDelete = ? and folder_id = ?",
                new String[]{("%"+ email.getAddress() + "%"),("1"),("1")},null,null,null,null);
        return setMessages(cursor);
    }


    /**
     * 创建MAIL.db
     */
    public void CreateMessageTable(){
        MyDatabaseHelper helper = new MyDatabaseHelper(mContext);
        helper.getWritableDatabase();
    };

    /**
     * 判断邮件是否存在，同步邮件时
     * @param message_id
     * @return
     */
    public Boolean isExistMail(String message_id){
        Cursor cursor = db.query("EMAILMESSAGE", new String[]{"message_id"},"message_id = ?",
                new String[]{message_id},null,null,null,null);
        return cursor.moveToFirst();
    }
    /**
     * 判断发件人是否存在,返回color
     * @param from_mail
     * @return
     */
    public String isExistFrom(String from_mail){
        Cursor cursor = db.query("EMAILMESSAGE", null,"from_mail = ? or from_mail = ?",
                new String[]{("%"+from_mail+"%"),(from_mail)},"from_mail",null,null,null);
        if(cursor.moveToFirst()){
            return cursor.getString(cursor.getColumnIndex("avatar_color"));
        }
        return "";
    }

    /**
     * 判断邮件是否存在，增删改时
     * @param id
     * @return
     */
    public Boolean isExistMessage(int id){
        Cursor cursor = db.query("EMAILMESSAGE", null,"id = ?",
                new String[]{String.valueOf(id)},null,null,null,null);
        return cursor.moveToFirst();
    }




    /**
     * 插入邮件信息
     * @param emailMessage
     */
    public void InsertMessages(EmailMessage emailMessage){
        ContentValues values = new ContentValues();
            values.put("message_id",emailMessage.getMessage_id());
            values.put("email_id",emailMessage.getEmail_id());
            values.put("user_id",emailMessage.getUser_id());
            values.put("folder_id",emailMessage.getFolder_id());
            values.put("subject",emailMessage.getSubject());
            values.put("from_mail",emailMessage.getFrom());
            values.put("to_mail",emailMessage.getTo());
            values.put("cc",emailMessage.getCc());
            values.put("bcc",emailMessage.getBcc());
            values.put("content",emailMessage.getMessage_text());
            values.put("sendDate",emailMessage.getSendDate());
            values.put("isStar",emailMessage.getIsStar());
            values.put("isRead",emailMessage.getIsRead());
            values.put("isSend",emailMessage.getIsSend());
            values.put("isDelete",emailMessage.getIsDelete());
            values.put("isAttachment",emailMessage.getIsAttachment());
            values.put("attachment",emailMessage.getAttachment());
            values.put("avatar_color",emailMessage.getAvatar_color());
            db.insert("EMAILMESSAGE",null,values);
            values.clear();
//            db.close();
    }

    /**
     * 修改为已读
     * @param id isRead = 1
     */
    public void updateRead(int id){
        ContentValues values = new ContentValues();
        values.put("isRead",1);
        db.update("EMAILMESSAGE",values,"id = ?",new String[]{String.valueOf(id)});
    }

    /**
     * 修改为未读
     * @param id isRead = 0
     */
    public void updateunRead(int id){
        ContentValues values = new ContentValues();
        values.put("isRead",0);
        db.update("EMAILMESSAGE",values,"id = ?",new String[]{String.valueOf(id)});
    }

    /**
     * 查詢isRead值
     * @param id isRead
     */
    public int queryisRead(int id){
        int isRead = 0;
        Cursor cursor = db.query("EMAILMESSAGE", null,"id = ?",
                new String[]{(String.valueOf(id))},null,null,null,null);
        if(cursor.moveToFirst()){
             isRead = cursor.getInt(cursor.getColumnIndex("isRead"));
        }
        cursor.close();
        return isRead;
    }

    /**
     * 查詢isStar值
     * @param id isStar
     */
    public int queryisStar(int id){
        int isStar = 0;
        Cursor cursor = db.query("EMAILMESSAGE", null,"id = ?",
                new String[]{(String.valueOf(id))},null,null,null,null);
        if(cursor.moveToFirst()){
            isStar = cursor.getInt(cursor.getColumnIndex("isStar"));
        }
        cursor.close();
        return isStar;
    }



    /**
     * 修改isDelete为1,存入已删除
     * @param id
     */
    public void updateisDelete(int id){
        ContentValues values = new ContentValues();
        values.put("isDelete",1);
        values.put("isStar",0);
        db.update("EMAILMESSAGE",values,"id = ?",new String[]{String.valueOf(id)});
    }

    /**
     * 彻底删除邮件信息
     * @param id
     */
    public void deleteMessage(int id){
        db.delete("EMAILMESSAGE","id = ?",new String[]{String.valueOf(id)});
    }


    /**
     * 修改邮件信息文件存放id
     * @param id
     */
    public void updatefolder(int id,int folder_id){
        ContentValues values = new ContentValues();
        values.put("folder_id",folder_id);
        db.update("EMAILMESSAGE",values,"id = ?",new String[]{String.valueOf(id)});
    }

    /**
     * 修改邮件为星标
     * @param id
     */
    public void updateStar(int id){
        ContentValues values = new ContentValues();
        values.put("isStar",1);
        db.update("EMAILMESSAGE",values,"id = ?",new String[]{String.valueOf(id)});
    }
    /**
     * 取消邮件星标
     * @param id
     */
    public void updateUnStar(int id){
        ContentValues values = new ContentValues();
        values.put("isStar",0);
        db.update("EMAILMESSAGE",values,"id = ?",new String[]{String.valueOf(id)});
    }



    /**
     * 设置属性返回messages
     * @param cursor
     * @return
     */
    private List<EmailMessage> setMessages(Cursor cursor){
        List<EmailMessage> messages =new ArrayList<>();
        EmailMessage message;
        if(cursor.moveToFirst()){
            do {
                message = new EmailMessage();
                message.setId(cursor.getInt((cursor.getColumnIndex("id"))));
                message.setMessage_id(cursor.getString(cursor.getColumnIndex("message_id")));
                message.setSubject(cursor.getString(cursor.getColumnIndex("subject")));
                message.setFolder_id(cursor.getInt(cursor.getColumnIndex("folder_id")));
                message.setFrom(cursor.getString(cursor.getColumnIndex("from_mail")));
                message.setTo(cursor.getString(cursor.getColumnIndex("to_mail")));
                message.setCc(cursor.getString(cursor.getColumnIndex("cc")));
                message.setBcc(cursor.getString(cursor.getColumnIndex("bcc")));
                message.setContent(cursor.getString(cursor.getColumnIndex("content")));
                message.setSendDate(cursor.getString(cursor.getColumnIndex("sendDate")));
                message.setIsRead(cursor.getInt(cursor.getColumnIndex("isRead")));
                message.setIsSend(cursor.getInt(cursor.getColumnIndex("isSend")));
                message.setIsDelete(cursor.getInt(cursor.getColumnIndex("isDelete")));
                message.setIsStar(cursor.getInt(cursor.getColumnIndex("isStar")));
                message.setIsAttachment(cursor.getInt(cursor.getColumnIndex("isAttachment")));
                message.setAttachment(cursor.getString(cursor.getColumnIndex("attachment")));
                message.setAvatar_color(cursor.getString(cursor.getColumnIndex("avatar_color")));
                messages.add(message);
            }while (cursor.moveToNext());
            cursor.close();
            return messages;
        }
        cursor.close();
        return null;
    }
}
