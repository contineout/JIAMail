package com.example.ttett.Folder_module;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.ttett.Dao.SQLiteDatabaseUtil;
import com.example.ttett.Entity.Email;
import com.example.ttett.Entity.Folder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FolderDao {

    private String TAG = "FolderDAO";
    private SQLiteDatabase db;

    FolderDao(Context context) {
        db = SQLiteDatabaseUtil.getInstance(context).getWritableDatabase();
    }
    /**
     * 判断文件夹是否存在
     * @param folder_name
     * @return
     */
    Boolean isExistFolder(String folder_name){
        Cursor cursor = db.query("FOLDER", new String[]{"folder_name"},"folder_name = ?",
                new String[]{folder_name},null,null,null,null);
        return cursor.moveToFirst();
    }

    /**
     * 插入文件夹信息
     * @param folder
     */
    void InsertFolder(Folder folder){
        folder.setDatetime(NewDate());
        Log.d(TAG,folder.getEmail_id()+"ddd");
        ContentValues values = new ContentValues();
        values.put("email_id",folder.getEmail_id());
        values.put("folder_name",folder.getFolder_name());
        values.put("datetime",folder.getDatetime());
        values.put("message_number",0);
        db.insert("FOLDER",null,values);
        values.clear();
//        db.close();
    }
    /**
     * 查询所有文件夹
     * @param email
     * @return
     */
    List<Folder> QueryAllFolder(Email email){
        Cursor cursor = db.query("FOLDER", null,"email_id = ? or id = ?",
                new String[]{String.valueOf(email.getEmail_id()),("1")},null,null,"id desc",null);
        Log.d(TAG,"查询了+"+cursor.getCount());

        List<Folder> folders =new ArrayList<>();
        Folder folder;
        if(cursor.moveToFirst()){
            do {
                folder = new Folder();
                folder.setFolder_name(cursor.getString(cursor.getColumnIndex("folder_name")));
                folder.setMessage_number(cursor.getString(cursor.getColumnIndex("message_number")));
                folder.setFolder_id(cursor.getInt(cursor.getColumnIndex("id")));
                folder.setDatetime(cursor.getString(cursor.getColumnIndex("datetime")));
                folders.add(folder);
            }while (cursor.moveToNext());
            cursor.close();
            return folders;
        }
        cursor.close();
        return null;
    }
    /**
     * 更改文件夹
     * @param id
     * @param folder_id
     */
    void updateFolder_id(int id, int folder_id){
        ContentValues values = new ContentValues();
        values.put("folder_id",folder_id);
        db.update("EMAILMESSAGE",values,"id = ?",new String[]{String.valueOf(id)});
    }

    int queryFolderMessageCount(int id,int email_id){
        Cursor cursor = db.query("EMAILMESSAGE", new String[]{"id"},"folder_id = ? and email_id = ?",
                new String[]{String.valueOf(id),String.valueOf(email_id)},null,null,null,null);
        return cursor.getCount();
    }



    private String NewDate(){
        Date date = new Date();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str = format.format(date);
        return str;
    }
}
