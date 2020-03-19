package com.example.ttett.Service;

import android.content.Context;
import android.util.Log;

import com.example.ttett.Dao.FolderDao;
import com.example.ttett.Entity.Email;
import com.example.ttett.Entity.Folder;

import java.util.List;

public class FolderService {

    private String TAG  = "FolderService";
    private Context mContext;
    public FolderService(Context context) {
        this.mContext = context;
    }

    /**
     * 判断又没有同名文件夹,若没有则新建文件夹
     * @param folder
     * @return
     */
    public Boolean SaveFolder(Folder folder) {
        FolderDao folderDao = new FolderDao(mContext);
            if (!folderDao.isExistFolder(folder.getFolder_name())) {
                folderDao.InsertFolder(folder);
            }
        return folderDao.isExistFolder(folder.getFolder_name());
    }

    /**
     * 查询所有文件夹
     * @param email
     * @return
     */
    public List<Folder> queryAllFolder(Email email){
        FolderDao folderDao = new FolderDao(mContext);
        Log.d(TAG,"f"+email.getEmail_id());
        return folderDao.QueryAllFolder(email);
    }
}
