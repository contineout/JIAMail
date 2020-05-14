package com.example.ttett.Contact_module;

import android.content.Context;
import android.util.Log;

import com.example.ttett.Entity.Contact;
import com.example.ttett.Entity.Email;
import com.example.ttett.bean.Person;

import java.util.ArrayList;
import java.util.List;

public class ContactService {

    private String TAG  = "ContactService";
    private Context mContext;
    public ContactService(Context context) {
        this.mContext = context;
    }

    /**
     * 判断又没有相同的邮箱，若无则保存
     * @param contact
     * @return
     */
    public Boolean SaveContact(Contact contact,int email_id) {
        ContactDao contactDao = new ContactDao(mContext);
        if (!contactDao.isExistContact(contact.getEmail(),email_id)) {
            return contactDao.InsertContact(contact);
        }else{
            return false;
        }
    }

    /**
     * 查询所有联系人
     * @param email_id
     * @return
     */
    public List<Contact> queryAllContact(int email_id){
        ContactDao contactDao = new ContactDao(mContext);
        return contactDao.QueryAllContact(email_id);
    }

    /**
     * 查询所有往来邮件联系人
     * @param email
     * @return
     */
    public List<Contact> queryAllEmailContact(Email email){
        ContactDao contactDao = new ContactDao(mContext);
        List<String> names = contactDao.QueryAllEmailNames(email);
        List<String> new_names = new ArrayList<>();
        for(int i=0;i<names.size();i++) {
            if (!new_names.contains(names.get(i))) {
                new_names.add(names.get(i));
            }
        }
        List<Contact> contacts = new ArrayList<>();
        for(String s: new_names){
            contacts.add(contactDao.QueryAllEmailContact(email,s));
        }
        return contacts;
    }


    /**
     * 导入邮件联系人
     * @param email
     * @return
     */
    public void insertAllMailContact(Email email){
        ContactDao contactDao = new ContactDao(mContext);
        List<String> names = contactDao.QueryAllEmailNames(email);
        List<String> new_names = new ArrayList<>();
        for(int i=0;i<names.size();i++) {
            if (!new_names.contains(names.get(i))) {
                new_names.add(names.get(i));
            }
        }
        List<Contact> contacts = new ArrayList<>();
        for(String s: new_names){
            contacts.add(contactDao.QueryAllEmailContact(email,s));
        }
        for(Contact contact:contacts){
            SaveContact(contact,email.getEmail_id());
        }

    }

    /**
     * 删除邮件联系人
     * @param id
     * @return
     */
    public boolean deleteContact(int id){
        ContactDao contactDao = new ContactDao(mContext);
        if(contactDao.isExistMail(id)){
            contactDao.deleteContact(id);
        }
        return !contactDao.isExistMail(id);
    }

    /**
     * 修改邮件联系人信息
     * @param contact
     * @return
     */
    public void updateContact(Contact contact){
        ContactDao contactDao = new ContactDao(mContext);
        Log.d(TAG,contact.getContact_id()+"ContactId");
        if(contactDao.isExistMail(contact.getContact_id())){

            contactDao.updateContact(contact);
        }
    }

    /**
     * 联系人信息
     * @param id
     * @return
     */
    public Contact queryContact(int id){
        ContactDao contactDao = new ContactDao(mContext);
        if(contactDao.isExistMail(id)){
            return contactDao.queryContact(id);
        }
        return null;
    }

    /**
     * 联系人信息
     * @param id
     * @return
     */
    public Person queryPerson(int id){
        ContactDao contactDao = new ContactDao(mContext);
        if(contactDao.isExistMail(id)){
            return contactDao.queryPerson(id);
        }
        return null;
    }

}
