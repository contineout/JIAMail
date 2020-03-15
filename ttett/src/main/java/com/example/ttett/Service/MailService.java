package com.example.ttett.Service;

import android.content.Context;

public class MailService {

    private Context mContext;
    public MailService(Context context) {
        this.mContext = context;
    }

//    public Boolean SaveMessage( List<EmailMessage> emailMessages) {
//        MailDao mailDao = new MailDao(mContext);
//        int i = 0;
//        for (EmailMessage emailMessage : emailMessages) {
//            if (!mailDao.isExistMail(emailMessage.getMessage_id())) {
//                mailDao.InsertMessages(emailMessage);
//                i+=1;
//            }
//        }
//        return i > 0;
//    }

//    public Boolean SaveMessage(Email email) {
//        MailDao mailDao = new MailDao(mContext);
//        mailDao.QueryMessageCount(email);
//    }
}
