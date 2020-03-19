package com.example.ttett.Dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.ttett.Entity.Contact;
import com.example.ttett.MailSqlite.MyDatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class ContactDao {

    private static final String TAG = "ContactDao.this" ;
    private final MyDatabaseHelper mHelper;

    private Context mContext;

    public ContactDao(Context context) {
        mHelper = new MyDatabaseHelper(context);
        this.mContext = context;
    }


    /**
     * 判断联系人是否存在
     * @param email
     * @return
     */
    public Boolean isExistMail(String email){
        SQLiteDatabase db = mHelper.getWritableDatabase();
        Cursor cursor = db.query("CONTACT", new String[]{"id"},"email = ?",
                new String[]{email},null,null,null,null);
        return cursor.moveToFirst();
    }

    /**
     * 插入联系人信息
     * @param contact
     */
    public void InsertContact(Contact contact){

        SQLiteDatabase db = mHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("user_id",contact.getUser_id());
        values.put("contacts_name",contact.getName());
        values.put("contacts_remark",contact.getRemark());
        values.put("contacts_birthday",contact.getBirthday());
        values.put("contacts_company",contact.getCompany());
        values.put("contacts_department",contact.getDepartment());
        values.put("contacts_position",contact.getPosition());
        values.put("contacts_email",contact.getEmail());
        values.put("contacts_iphone",contact.getIphone());
        values.put("contact_address",contact.getAddress());
        db.insert("CONTACT",null,values);
        values.clear();
        db.close();
    }

    /**
     * 读取User联系人信息
     * @param user_id
     * @return
     */
    public List<Contact> QueryAllContact(int user_id){
        SQLiteDatabase db = mHelper.getWritableDatabase();
        Cursor cursor = db.query("CONTACT", null,"user_id = ?",
                new String[]{String.valueOf(user_id)},null,"id desc",null,null);
        Log.d(TAG,"查询了+"+cursor.getCount());

        List<Contact> contacts =new ArrayList<>();
        Contact contact;
        if(cursor.moveToFirst()){
            do {
                contact = new Contact();
                contact.setUser_id(cursor.getInt(cursor.getColumnIndex("user_id")));
                contact.setName(cursor.getString(cursor.getColumnIndex("contacts_name")));
                contact.setRemark(cursor.getString(cursor.getColumnIndex("contacts_remark")));
                contact.setBirthday(cursor.getString(cursor.getColumnIndex("contacts_birthday")));
                contact.setCompany(cursor.getString(cursor.getColumnIndex("contacts_company")));
                contact.setDepartment(cursor.getString(cursor.getColumnIndex("contacts_department")));
                contact.setPosition(cursor.getString(cursor.getColumnIndex("contacts_position")));
                contact.setEmail(cursor.getString(cursor.getColumnIndex("contacts_email")));
                contact.setIphone(cursor.getString(cursor.getColumnIndex("contacts_iphone")));
                contact.setAddress(cursor.getString(cursor.getColumnIndex("contact_address")));
                contacts.add(contact);
            }while (cursor.moveToNext());
            cursor.close();
            return contacts;
        }
        cursor.close();
        return null;
    }
}
