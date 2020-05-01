package com.example.ttett.Service;

import android.content.Context;
import android.util.Log;

import com.example.ttett.Dao.MailDao;
import com.example.ttett.Entity.Email;
import com.example.ttett.Entity.EmailMessage;
import com.example.ttett.util.RecipientMessage;

import java.util.List;
import java.util.Random;

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
    public int SaveMessage( List<EmailMessage> emailMessages) {
        MailDao mailDao = new MailDao(mContext);
        int i = 0;
        for (EmailMessage emailMessage : emailMessages) {
            if (!mailDao.isExistMail(emailMessage.getMessage_id())) {
                String color = queryAvatar_color(emailMessage.getFrom());
                emailMessage.setAvatar_color(color);
                mailDao.InsertMessages(emailMessage);
                i+=1;
            }
        }
        return i;
    }

    public int queryEmail_id(int id){
        MailDao mailDao = new MailDao(mContext);
        if(mailDao.isExistMessage(id)){
            return mailDao.QueryEmail_id(id);
        }
        return 0;
    }

    public List<EmailMessage> queryDialogMessage(String address){
        MailDao mailDao = new MailDao(mContext);
        if(mailDao.queryIsDialogMessage(address)){
           return mailDao.queryDialogMessage(address);
        }
        return null;
    }

    public List<EmailMessage> queryFolderMessage(int folder_id){
        MailDao mailDao = new MailDao(mContext);
        return  mailDao.queryFolderMessage(folder_id);
    }

    /**
     * 判断有没有新邮件
     * @param email
     * @param messages
     * @return
     */
    public Message[] isNewMessage(Email email, Message[] messages) {
        int MessageCount = email.getMessage_count();
        Log.d(TAG, "MessageCount: "+"邮件数量:　" + MessageCount);
        int RecipientMessageCount = messages.length;
        Log.d(TAG, "RecipientMessageCount: "+"邮件数量:　" + RecipientMessageCount);
        if(MessageCount == 0){
            return messages;
        }
        if(MessageCount < RecipientMessageCount){
            int Count = RecipientMessageCount - MessageCount;
            Message[] temp = new Message[Count];
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
    public void SynchronizeMessage(Email email){
        RecipientMessage recipientMessage = new RecipientMessage(email,mContext);
        switch (email.getType()){
            case "sina.com":
                List<EmailMessage> emailMessages = recipientMessage.SinaRecipient();
                checkSaveMessage(email,emailMessages);
                break;
            case "qq.com":
                emailMessages = recipientMessage.QQRecipient();
                checkSaveMessage(email,emailMessages);
                break;
        }
    }

    public void checkSaveMessage(Email email,List<EmailMessage> emailMessages){
        EmailService emailService;
        int saveCount = SaveMessage(emailMessages);
        if(saveCount!=0){
            emailService = new EmailService(mContext);
            email.setMessage_count(saveCount+email.getMessage_count());
            emailService.updateMessageCount(email);
            Log.d(TAG,email.getType()+"有新邮件保存成功");
        }else {
            Log.d(TAG,email.getType()+"无新邮件");
        }
    }

    /**
     * 查询sqlite所有邮件
     * @param email
     * @return
     */
    public List<EmailMessage> queryAllMessage(Email email){
        MailDao mailDao = new MailDao(mContext);
        return mailDao.QueryAllMessage(email);
    }

    /**
     * 查询sqlite所有已发送邮件
     * @param email
     * @return
     */
    public List<EmailMessage> querySendedMessage(Email email){
        MailDao mailDao = new MailDao(mContext);
        return mailDao.QuerySendedMessage(email);
    }

    /**
     * 查询sqlite所有未发送邮件
     * @param email
     * @return
     */
    public List<EmailMessage> queryDraftsMessage(Email email){
        MailDao mailDao = new MailDao(mContext);
        return mailDao.QueryDraftMessage(email);
    }

    /**
     * 批量修改isRead标志位
     * @param id_item
     */
    public void updateReadMessage(List<Integer> id_item,boolean setUnread) {
        MailDao mailDao = new MailDao(mContext);
        for(int id:id_item){
            if(mailDao.isExistMessage(id)){
               if(setUnread){
                   mailDao.updateunRead(id);
               }else{
                   mailDao.updateRead(id);
               }
            }
        }
    }

    /**
     * 批量修改isStar标志位
     * @param id_item
     */
    public void updateStarMessage(List<Integer> id_item,boolean setStar) {
        MailDao mailDao = new MailDao(mContext);
        for(int id:id_item){
            if(mailDao.isExistMessage(id)){
                if(setStar){
                    mailDao.updateStar(id);
                }else{
                    mailDao.updateUnStar(id);
                }
            }
        }
    }

    /**
     * 点击取消设置isRead = 1
     * @param id
     */
    public void updateReadMessage(int id) {
        MailDao mailDao = new MailDao(mContext);
        if(mailDao.isExistMessage(id)){
            mailDao.updateRead(id);
        }
    }

    /**
     * 查询选中已读邮件数量 isRead = 1
     * @param id_item
     * @return
     */

    public int queryReadCount(List<Integer> id_item){
        MailDao mailDao = new MailDao(mContext);
        int ReadCount = 0;
        for(int id:id_item){
            if(mailDao.isExistMessage(id)){
                ReadCount+=mailDao.queryisRead(id);
            }
        }
        return ReadCount;
    }

    /**
     * 查询星标邮件数
     * @param id_item
     * @return
     */
    public int queryStarCount(List<Integer> id_item){
        MailDao mailDao = new MailDao(mContext);
        int StarCount = 0;
        for(int id:id_item){
            if(mailDao.isExistMessage(id)){
                StarCount+=mailDao.queryisStar(id);
            }
        }
        return StarCount;
    }


    /**
     * 修改删除
     * @param id
     */
    public void updateisDelete(int id) {
        MailDao mailDao = new MailDao(mContext);
        if(mailDao.isExistMessage(id)){
            mailDao.updateisDelete(id);
        }
    }

    /**
     * 彻底删除邮件
     * @param id
     */
    public void deleteMessage(int id) {
        MailDao mailDao = new MailDao(mContext);
        if(mailDao.isExistMessage(id)){
            mailDao.deleteMessage(id);
        }
    }

    /**
     * 查询未读邮件
     * @param email
     * @return
     */
    public List<EmailMessage> queryUnReadMessage(Email email){
        MailDao mailDao = new MailDao(mContext);
        return mailDao.QueryUnReadMessage(email);
    }

    /**
     * 查询已删除邮件
     * @param email
     * @return
     */
    public List<EmailMessage> queryDeleteMessage(Email email){
        MailDao mailDao = new MailDao(mContext);
        return mailDao.QueryDelteMessage(email);
    }

    /**
     * 查询星标邮件
     * @param email
     * @return
     */
    public List<EmailMessage> queryStarMessage(Email email){
        MailDao mailDao = new MailDao(mContext);
        return mailDao.QueryStarMessage(email);
    }

    public String queryAvatar_color(String from){
        MailDao mailDao = new MailDao(mContext);
        if(!mailDao.isExistFrom(from).equals("")){
            return mailDao.isExistFrom(from);
        }else {
            return getRandColor();
        }
    }

    public String getRandColor() {
        String R, G, B;
        Random random = new Random();
        R = Integer.toHexString(random.nextInt(256)).toUpperCase();
        G = Integer.toHexString(random.nextInt(256)).toUpperCase();
        B = Integer.toHexString(random.nextInt(256)).toUpperCase();

        R = R.length() == 1 ? "0" + R : R;
        G = G.length() == 1 ? "0" + G : G;
        B = B.length() == 1 ? "0" + B : B;

        return "#" + R + G + B;
    }

}
