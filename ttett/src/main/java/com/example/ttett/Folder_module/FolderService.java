package com.example.ttett.Folder_module;

import android.content.Context;
import android.util.Log;

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
            }else {
                return false;
            }
        return true;
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

    /**
     * 更改文件夹
     * @param id_item
     * @param folder_id
     */
    public void updateFolder(List<Integer> id_item,int folder_id){
        FolderDao folderDao = new FolderDao(mContext);
        for(int id:id_item){
            folderDao.updateFolder_id(id,folder_id);
        }
    }

    public int queryFolderMessageCount(int id){
        FolderDao folderDao = new FolderDao(mContext);
        return folderDao.queryFolderMessageCount(id);
    }


}
