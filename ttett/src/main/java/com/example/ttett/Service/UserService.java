package com.example.ttett.Service;

import android.content.Context;

import com.example.ttett.Dao.UserDao;
import com.example.ttett.Entity.User;

public class UserService {

    private String TAG  = "ContactService";
    private Context mContext;
    public UserService(Context context) {
        this.mContext = context;
    }

    /**
     * 判断又没有相同的邮箱，若无则保存
     * @param
     * @return
     */
    public Boolean SaveUser(User user) {
        UserDao userDao = new UserDao(mContext);
        if (!userDao.isExistMail(user.getAccount())) {
            return userDao.InsertMessages(user);
        }else{
            return false;
        }
    }

}
