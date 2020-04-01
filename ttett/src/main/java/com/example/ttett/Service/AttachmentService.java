package com.example.ttett.Service;

import android.content.Context;

import com.example.ttett.Dao.AttachmentDao;
import com.example.ttett.Entity.Attachment;

import java.util.List;

public class AttachmentService {

    private String TAG  = "AttachmentService";
    private Context mContext;
    public AttachmentService(Context context) {
        this.mContext = context;
    }

    /**
     * 判断又没有相同名文件,若有"(重复数)"
     * @param attachment
     * @return
     */
    public void SaveAttachment(Attachment attachment) {
        AttachmentDao attachmentDao = new AttachmentDao(mContext);
        int repeatCount = attachmentDao.isExistName(attachment.getName());
        if (repeatCount == 0) {
            attachmentDao.InsertAttachment(attachment);
        }else{
            attachment.setName(attachment.getName()+"("+repeatCount+")");
        }
    }

    /**
     * 查询一个邮箱下的附件
     * @param email_id
     * @return
     */
    public List<Attachment> queryAllAttachment(int email_id){
        AttachmentDao attachmentDao = new AttachmentDao(mContext);
        return attachmentDao.QueryAllAttachment(email_id);
    }

    /**
     * 查询一个邮件下的附件
     * @param message_id
     * @return
     */
    public List<Attachment> queryMessageAllAttachment(String message_id){
        AttachmentDao attachmentDao = new AttachmentDao(mContext);
        return attachmentDao.QueryMessageAttachment(message_id);
    }

}
