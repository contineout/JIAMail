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
     * 判断又没有相同的邮箱，若无则保存
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
     * 查询所有文件夹
     * @param user_id
     * @return
     */
    public List<Email> queryAllEmail(int user_id){
        EmailDao emailDao = new EmailDao(mContext);
        Log.d(TAG,"f"+user_id);
        return emailDao.QueryAllEmail(user_id);
    }
}
