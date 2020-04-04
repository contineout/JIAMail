package com.example.ttett.Service;

import android.content.Context;
import android.util.Log;

import com.example.ttett.Dao.EmailDao;
import com.example.ttett.Entity.Email;

import java.util.List;

public class EmailService {

    private String TAG  = "EmailService";
    private Context mContext;
    public EmailService(Context context) {
        this.mContext = context;
    }

    /**
     * 判断又没有相同的邮箱，若无则保存,创建inbox文件夹
     * @param email
     * @return
     */
    public Boolean SaveEmail(Email email) {
        EmailDao emailDao = new EmailDao(mContext);
        if (!emailDao.isExistEmail(email.getAddress())){

            return emailDao.InsertEmail(email);
        }else{
            return false;
        }
    }

    /**
     * 根据邮箱地址查询邮箱
     * @param address
     * @return
     */
    public Email queryEmail(String address) {
        EmailDao emailDao = new EmailDao(mContext);
        if (emailDao.isExistEmail(address)){
            Log.d(TAG,"存在");
            return emailDao.QueryNewEmail(address);
        }else{
            return null;
        }
    }

    /**
     * 根据Email_d查询邮箱
     * @param email_id
     * @return
     */
    public Email queryEmail(int email_id) {
        EmailDao emailDao = new EmailDao(mContext);
        return emailDao.QueryNewEmail(email_id);
    }

    /**
     * 查询所有邮箱
     * @param user_id
     * @return
     */
    public List<Email> queryAllEmail(int user_id){
        EmailDao emailDao = new EmailDao(mContext);
        Log.d(TAG,"f"+user_id);
        return emailDao.QueryAllEmail(user_id);
    }

    /**
     * 修改邮箱邮件数量
     * @param email
     * @return
     */
    public void updateMessageCount(Email email){
        EmailDao emailDao = new EmailDao(mContext);
        emailDao.updateMessageCount(email);
    }
}
