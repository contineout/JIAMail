package com.example.ttett.util.mailUtil;

import android.annotation.SuppressLint;
import android.content.Context;

import com.example.ttett.Dao.MailDao;
import com.example.ttett.Entity.Email;
import com.example.ttett.Entity.EmailMessage;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SaveMessage {
    private EmailMessage emailMessage;
    private Email email;
    private Context context;

    public SaveMessage(EmailMessage emailMessage, Context context, Email email){
        this.emailMessage = emailMessage;
        this.context = context;
        this.email = email;
    }
    public SaveMessage(EmailMessage emailMessage, Context context){
        this.emailMessage = emailMessage;
        this.context = context;
    }

    /**
     * 保存到数据库，在已发送中
     */
    public void saveSendMessage(){
        MailDao mailDao = new MailDao(context);
        String from_mail = email.getName()+"<"+ emailMessage.getFrom() + ">";
        emailMessage.setFrom(from_mail);
        emailMessage.setIsSend(1);
        emailMessage.setSendDate(newDate());
        mailDao.InsertMessages(emailMessage);
    }

    /**
     * 保存到数据库，在草稿中
     */
    public void saveDraftsMessage(){
        emailMessage.setSendDate(newDate());
        MailDao mailDao = new MailDao(context);
        mailDao.InsertMessages(emailMessage);
    }

    private String newDate(){
        Date date;
        @SuppressLint("SimpleDateFormat") DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str;
        date = new Date();
        str = format.format(date);
        return str;
    }
}
