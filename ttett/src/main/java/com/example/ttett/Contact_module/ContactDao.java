package com.example.ttett.Contact_module;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.ttett.Dao.SQLiteDatabaseUtil;
import com.example.ttett.Entity.Contact;
import com.example.ttett.Entity.Email;

import java.util.ArrayList;
import java.util.List;

public class ContactDao {

    private static final String TAG = "ContactDao.this" ;
    private SQLiteDatabase db;

    private Context mContext;

    public ContactDao(Context context) {
        this.mContext = context;
        db = SQLiteDatabaseUtil.getInstance(context).getWritableDatabase();
    }

    /**
     * 判断联系人是否存在
     * @param id
     * @return
     */
    public Boolean isExistMail(int id){
        Cursor cursor = db.query("CONTACT", null,"id = ?",
                new String[]{String.valueOf(id)},null,null,null,null);
        return cursor.moveToFirst();
    }

    public void deleteContact(int id){
        db.delete("CONTACT","id = ?",new String[]{String.valueOf(id)});
    }


    /**
     * 判断联系人是否存在
     * @param email
     * @return
     */
    public Boolean isExistMail(String email){
        Cursor cursor = db.query("CONTACT", new String[]{"id"},"contacts_email = ?",
                new String[]{email},null,null,null,null);
        return cursor.moveToFirst();
    }

//select distinct from_mail from EMAILMESSAGE where user_id = 1;
    public List<String> queryRecipientMailContact(int email_id){
        Cursor cursor = db.query("EMAILMESSAGE", new String[]{"DISTINCT from_mail"},"email_id = ?",
                new String[]{String.valueOf(email_id)},null,null,null,null);
        List<String> form_mail = new ArrayList<>();
        if(cursor.moveToFirst()){
            do {
                form_mail.add(cursor.getString(cursor.getColumnIndex("from_mail")));
            }while (cursor.moveToNext());
            cursor.close();
        }
        return form_mail;
    }



    /**
     * 插入联系人信息
     * @param contact
     */
    public boolean InsertContact(Contact contact){
        ContentValues values = new ContentValues();
        values.put("email_id",contact.getEmail_id());
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
        return isExistMail(contact.getEmail());
    }

    /**
     * 读取User联系人信息
     * @param email_id
     * @return
     */
    public List<Contact> QueryAllContact(int email_id){
        Cursor cursor = db.query("CONTACT", null,"email_id = ?",
                new String[]{String.valueOf(email_id)},null,null,"id desc",null);
        Log.d(TAG,"查询了+"+cursor.getCount());

        List<Contact> contacts =new ArrayList<>();
        Contact contact;
        if(cursor.moveToFirst()){
            do {
                contact = new Contact();
                contact.setContact_id(cursor.getInt(cursor.getColumnIndex("id")));
                contact.setEmail_id(cursor.getInt(cursor.getColumnIndex("email_id")));
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

    /**
     * 查询所有往来邮件联系人
     * @param email
     * @return
     */
    public List<Contact> QueryAllEmailContact(Email email){
        Cursor cursor = db.query("EMAILMESSAGE", new String[]{"distinct from_mail"},"email_id = ? and not from_mail = ?",
                new String[]{String.valueOf(email.getEmail_id()),(email.getAddress())},null,null,"id desc",null);
        Log.d(TAG,"查询了+"+cursor.getCount());

        List<Contact> contacts =new ArrayList<>();
        Contact contact;
        if(cursor.moveToFirst()){
            do {
                String[] str = cursor.getString(cursor.getColumnIndex("from_mail")).split("[<>]");
                contact = new Contact();
                contact.setName(str[0]);
                contact.setEmail(str[1]);
                contacts.add(contact);
            }while (cursor.moveToNext());
            cursor.close();
            return contacts;
        }
        cursor.close();
        return null;
    }
}
