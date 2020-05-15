package com.example.ttett.util.mailUtil;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.example.ttett.Entity.Attachment;
import com.example.ttett.Entity.Email;
import com.example.ttett.Entity.EmailMessage;
import com.example.ttett.WriteLetterActivity;
import com.example.ttett.bean.MessageEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class SendMessage {

    private static final String TAG = "SendMessage";
    private EmailMessage emailMessage;
    private Email email;
    private List<Attachment> attachments;
    private List<MimeBodyPart> mimeBodyParts;
    private Context context;
    private boolean isSuccess;

    public SendMessage(EmailMessage emailMessage, Email email, List<Attachment> attachments,Context context) {
        this.emailMessage = emailMessage;
        this.email = email;
        this.attachments = attachments;
        this.context = context;
    }

    public void SendMessage(){
        EmailProp emailProp = new EmailProp();
        Session session = emailProp.getSTMPSession(email);
        try {
            Message msg = null;
            try{
                if(WriteLetterActivity.flag.equals("tran")){
                    msg = createTranMimeMessage(session);
                }else{
                    msg = createMimeMessage(session);
                }
            }catch (NullPointerException e){
                msg = createMimeMessage(session);
            }

            msg.saveChanges();

            Transport transport = session.getTransport();
            transport.connect(email.getAddress(), email.getAuthorizationCode());

            transport.sendMessage(msg, msg.getAllRecipients());
            transport.close();
            EventBus.getDefault().postSticky(new MessageEvent("SendSuccess",email,emailMessage));
        } catch (Exception e) {
            e.printStackTrace();
            EventBus.getDefault().postSticky(new MessageEvent("SendError",email,emailMessage));
        }
    }

    private MimeMessage createMimeMessage(Session session) throws Exception {

        MimeMessage msg = new MimeMessage(session);
        //发件人
        msg.setFrom(new InternetAddress(emailMessage.getFrom(), email.getName(), "UTF-8"));
        //接收人
        if(!emailMessage.getTo().isEmpty()){
            String[] toList = emailMessage.getTo().split("[,]");
            for (int i = 0;i < toList.length -1;i++){
                msg.addRecipient(MimeMessage.RecipientType.TO, new InternetAddress(toList[i], email.getName(), "UTF-8"));
            }
        }

        //密送
        if(!emailMessage.getBcc().isEmpty()){
            String[] bccList = emailMessage.getBcc().split("[,]");
            for (int i = 0;i < bccList.length -1;i++){
                msg.addRecipient(MimeMessage.RecipientType.BCC,new InternetAddress(bccList[i],email.getName(),"UTF-8"));
            }
        }
        //抄送
        if(!emailMessage.getCc().isEmpty()){
            String[] ccList = emailMessage.getCc().split("[,]");
            for (int i = 0;i < ccList.length -1;i++){
                msg.addRecipient(MimeMessage.RecipientType.CC,new InternetAddress(ccList[i],email.getName(),"UTF-8"));
            }
        }
        //主题
        msg.setSubject(emailMessage.getSubject(), "UTF-8");


        mimeBodyParts = new ArrayList<>();
        if(emailMessage.getContent().split("[<>]").length > 0){
            Log.d(TAG,emailMessage.getContent().split("[<>]").length+"1");
            MimeBodyPart content = createContent(emailMessage.getContent());
            mimeBodyParts.add(content);
        }else {
            msg.setContent(emailMessage.getContent(), "text/html;charset=UTF-8");
        }

        //附件根节点

        int i = 0;
        if(attachments!=null){
            for(Attachment attachment:attachments){
                mimeBodyParts.add(i,createAttachment(attachment.getPath()));
                i+=1;
            }
        }

        //合成大节点
        MimeMultipart mimeMultipart = new MimeMultipart("mixed");
        for(MimeBodyPart bodyPart:mimeBodyParts){
            mimeMultipart.addBodyPart(bodyPart);
        }
        //内容
        msg.setContent(mimeMultipart);

        //日期
        msg.setSentDate(new Date());

        return msg;
    }

    private MimeMessage createTranMimeMessage(Session session) throws Exception {

        MimeMessage msg = new MimeMessage(session);
        //发件人
        msg.setFrom(new InternetAddress(emailMessage.getFrom(), email.getName(), "UTF-8"));
        //接收人
        if(!emailMessage.getTo().isEmpty()){
            String[] toList = emailMessage.getTo().split("[,]");
            for (int i = 0;i < toList.length -1;i++){
                msg.addRecipient(MimeMessage.RecipientType.TO, new InternetAddress(toList[i], email.getName(), "UTF-8"));
            }
        }

        //密送
        if(!emailMessage.getBcc().isEmpty()){
            String[] bccList = emailMessage.getBcc().split("[,]");
            for (int i = 0;i < bccList.length -1;i++){
                msg.addRecipient(MimeMessage.RecipientType.BCC,new InternetAddress(bccList[i],email.getName(),"UTF-8"));
            }
        }
        //抄送
        if(!emailMessage.getCc().isEmpty()){
            String[] ccList = emailMessage.getCc().split("[,]");
            for (int i = 0;i < ccList.length -1;i++){
                msg.addRecipient(MimeMessage.RecipientType.CC,new InternetAddress(ccList[i],email.getName(),"UTF-8"));
            }
        }
        //主题
        msg.setSubject(emailMessage.getSubject(), "UTF-8");


        mimeBodyParts = new ArrayList<>();
        msg.setContent(emailMessage.getContent(), "text/html;charset=UTF-8");

        //附件根节点

        int i = 0;
        if(attachments!=null){
            for(Attachment attachment:attachments){
                mimeBodyParts.add(i,createAttachment(attachment.getPath()));
                i+=1;
            }
            //合成大节点
            MimeMultipart mimeMultipart = new MimeMultipart("mixed");
            for(MimeBodyPart bodyPart:mimeBodyParts){
                mimeMultipart.addBodyPart(bodyPart);
            }
            //内容
            msg.setContent(mimeMultipart);
        }


        //日期
        msg.setSentDate(new Date());

        return msg;
    }

    private MimeBodyPart createContent(String content) throws Exception{
        Log.d(TAG,content);
        String[] split = content.split("[<>]");
        String newContent = "";
        MimeMultipart text_image = new MimeMultipart();
        MimeBodyPart text = new MimeBodyPart();
        for(int i = 0;i < split.length;i=i+2){
            String[] URI = split[i+1].split("[\"]");
            String path = getRealFilePath(context,Uri.parse(URI[1]));
            MimeBodyPart image = new MimeBodyPart();
            DataHandler dh = new DataHandler(new FileDataSource(path));
            image.setDataHandler(dh);
            String contentID = email.getAddress()+ String.valueOf(i);
            image.setContentID(contentID);
            newContent = newContent + split[i] + "<br/>" + "<img src = 'cid:"+contentID+"'/>";
            text_image.addBodyPart(image);
        }
        Log.d(TAG,newContent);
        text.setContent(newContent,"text/html;charset=UTF-8");
        text_image.addBodyPart(text);
        text_image.setSubType("related");

        MimeBodyPart bodyPart = new MimeBodyPart();
        bodyPart.setContent(text_image);
        return bodyPart;
    }


//    private static MimeBodyPart createContent(String body, String filename) throws Exception{
//        MimeBodyPart contentPart = new MimeBodyPart();
//        MimeMultipart contentMultipart = new MimeMultipart("related");
//        MimeBodyPart htmlBodyPart = new MimeBodyPart();
//        htmlBodyPart.setContent(body,"text/html;charset=gb2312");
//        contentMultipart.addBodyPart(htmlBodyPart);
//        MimeBodyPart pngBodyPart = new MimeBodyPart();
//        FileDataSource fds = new FileDataSource(filename);
//        pngBodyPart.setDataHandler(new DataHandler(fds));
//        pngBodyPart.setContentID("it315_logo_png");
//        contentMultipart.addBodyPart(pngBodyPart);
//
//        contentPart.setContent(contentMultipart);
//        return contentPart;
//    }

    private static MimeBodyPart createAttachment(String filename) throws Exception{
        MimeBodyPart attachPart = new MimeBodyPart();
        FileDataSource fds = new FileDataSource(filename);
        attachPart.setDataHandler(new DataHandler(fds));
        attachPart.setFileName(fds.getName());
        return attachPart;
    }
    public static String getRealFilePath(Context context, final Uri uri ) {
        if ( null == uri ) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if ( scheme == null )
            data = uri.getPath();
        else if ( ContentResolver.SCHEME_FILE.equals( scheme ) ) {
            data = uri.getPath();
        } else if ( ContentResolver.SCHEME_CONTENT.equals( scheme ) ) {
            Cursor cursor = context.getContentResolver().query( uri, new String[] { MediaStore.Images.ImageColumns.DATA }, null, null, null );
            if ( null != cursor ) {
                if ( cursor.moveToFirst() ) {
                    int index = cursor.getColumnIndex( MediaStore.Images.ImageColumns.DATA );
                    if ( index > -1 ) {
                        data = cursor.getString( index );
                    }
                }
                cursor.close();
            }
        }
        return data;
    }
}
