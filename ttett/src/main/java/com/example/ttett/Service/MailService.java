package com.example.ttett.Service;

import android.content.Context;
import android.util.Log;

import com.example.ttett.Dao.MailDao;
import com.example.ttett.Entity.Email;
import com.example.ttett.Entity.EmailMessage;
import com.example.ttett.util.RecipientMessage;

import java.util.List;

import javax.mail.Message;

import static androidx.constraintlayout.widget.Constraints.TAG;

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
        Message[] temp = new Message[6];
        int MessageCount = mailDao.QueryMessageCount(email);
        Log.d(TAG,"MessageCount"+MessageCount);
        int RecipientMessageCount = messages.length;
        if(MessageCount == 0){
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

    /**
     * 同步邮件
     * @param email
     * @return
     */
    public List<EmailMessage> SynchronizeMessage(Email email){
        RecipientMessage recipientMessage = new RecipientMessage();
        List<EmailMessage> emailMessages = recipientMessage.SinaRecipient(email,mContext);
        if(SaveMessage(emailMessages)){
            Log.d(TAG,"有新邮件保存成功");
        }
        MailDao mailDao = new MailDao(mContext);
        return mailDao.QueryAllMessage(email);
    }
}
