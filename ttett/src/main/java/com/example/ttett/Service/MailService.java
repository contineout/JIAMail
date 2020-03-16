package com.example.ttett.Service;

import android.content.Context;

import com.example.ttett.Dao.MailDao;
import com.example.ttett.Entity.Email;
import com.example.ttett.Entity.EmailMessage;

import java.util.List;

import javax.mail.Message;

public class MailService {

    private Context mContext;
    public MailService(Context context) {
        this.mContext = context;
    }

    /**
     * 判断message_id是否有重复，若没有则保存
     * @param emailMessages
     * @return
     */
    public Boolean SaveMessage( List<EmailMessage> emailMessages) {
        MailDao mailDao = new MailDao(mContext);
        int i = 0;
        for (EmailMessage emailMessage : emailMessages) {
            if (!mailDao.isExistMail(emailMessage.getMessage_id())) {
                mailDao.InsertMessages(emailMessage);
                i+=1;
            }
        }
        return i > 0;
    }

    /**
     * 判断有没有新邮件
     * @param email
     * @param messages
     * @return
     */
    public Message[] isNewMessage(Email email, Message[] messages) {
        MailDao mailDao = new MailDao(mContext);
        Message[] temp = new Message[]{};
        int MessageCount = mailDao.QueryMessageCount(email);
        int RecipientMessageCount = messages.length;
        if(MessageCount == 0 ){
            return messages;
        }
        if(MessageCount < RecipientMessageCount){
            int Count = RecipientMessageCount - MessageCount;
            for(int i = 0;i < Count; i++){
                temp[i] = messages[MessageCount+i];
            }
            return temp;
        }
        return null;
    }
}
