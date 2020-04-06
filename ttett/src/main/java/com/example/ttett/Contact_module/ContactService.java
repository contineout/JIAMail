package com.example.ttett.Contact_module;

import android.content.Context;
import android.util.Log;

import com.example.ttett.Entity.Contact;
import com.example.ttett.Entity.Email;

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
    public Boolean SaveContact(Contact contact) {
        ContactDao contactDao = new ContactDao(mContext);
        if (!contactDao.isExistMail(contact.getEmail())) {
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
        Log.d(TAG,"f"+email_id);
        return contactDao.QueryAllContact(email_id);
    }

    /**
     * 查询所有往来邮件联系人
     * @param email
     * @return
     */
    public List<Contact> queryAllEmailContact(Email email){
        ContactDao contactDao = new ContactDao(mContext);
        return contactDao.QueryAllEmailContact(email);
    }


    /**
     * 导入邮件联系人
     * @param email_id
     * @return
     */
    public void insertAllMailContact(int email_id){
        ContactDao contactDao = new ContactDao(mContext);
        Contact contact;
        List<String> from_mails = contactDao.queryRecipientMailContact(email_id);
        for(String from_mail:from_mails){
            String[] str = from_mail.split("[<>]");
            contact = new Contact();
            contact.setEmail_id(email_id);
            contact.setName(str[0]);
            contact.setEmail(str[1]);
            SaveContact(contact);
        }
    }

}
