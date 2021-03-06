package com.example.ttett.util.mailUtil;

import android.content.Context;
import android.util.Log;

import com.example.ttett.Entity.Email;
import com.example.ttett.Entity.EmailMessage;
import com.example.ttett.Service.MailService;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeMessage;

public class RecipientMessage{
    private String TAG = "RecipientMessage";
    private MailService mailService;
    private Email email;
    private Context context;
    private Store store = null;
    private Message[] messages =null;
    private Folder folder = null;

    public RecipientMessage(Email email, Context context) {
        this.email = email;
        this.context = context;
    }

    public List<EmailMessage> QQRecipient(){
        //1、连接邮件服务器的参数配置
        Properties props = new Properties();
        //设置传输协议
        String smtpPort = "465";
        props.setProperty("mail.smtp.port", smtpPort);
        props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.setProperty("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.smtp.socketFactory.port", smtpPort);
        props.setProperty("mail.store.protocol", "pop3");
        //设置收件人的POP3服务器
        props.setProperty("mail.pop3.host", "pop.qq.com");
        //2、创建定义整个应用程序所需的环境信息的 Session 对象
        Session session = Session.getInstance(props);
        //设置调试信息在控制台打印出来
//        session.setDebug(true);

        try {
            //连接收件人POP3服务器
            store = session.getStore("pop3");
            store.connect("pop.qq.com", email.getAddress(), email.getAuthorizationCode());
            //获得用户的邮件账户，注意通过pop3协议获取某个邮件夹的名称只能为inbox
            folder = store.getFolder("inbox");
            //设置对邮件账户的访问权限
            folder.open(Folder.READ_WRITE);
            //得到邮件账户的所有邮件信息
            messages = new Message[0];
            messages = folder.getMessages();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dealMessage(messages);
    }

    public List<EmailMessage> SinaRecipient(){
        //1、连接邮件服务器的参数配置
        Properties props = new Properties();
        //设置传输协议
        props.setProperty("mail.store.protocol", "pop3");
        //设置收件人的POP3服务器
        props.setProperty("mail.pop3.host", "pop3.sina.com");
        //2、创建定义整个应用程序所需的环境信息的 Session 对象
        Session session = Session.getInstance(props);

        try {
            //连接收件人POP3服务器
            store = session.getStore("pop3");
            store.connect("pop3.sina.com", email.getAddress(), email.getAuthorizationCode());
            //获得用户的邮件账户，注意通过pop3协议获取某个邮件夹的名称只能为inbox
            folder = store.getFolder("inbox");
            //设置对邮件账户的访问权限
            folder.open(Folder.READ_WRITE);
            //得到邮件账户的所有邮件信息
            messages = folder.getMessages();
            System.out.println("邮件数量:　" + messages.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dealMessage(messages);
    }

    public List<EmailMessage> recipient(){

        try {
            //连接收件人POP3服务器
            store = EmailProp.getPOP3Properties(email);
            //获得用户的邮件账户，注意通过pop3协议获取某个邮件夹的名称只能为inbox
            folder = store.getFolder("inbox");
            //设置对邮件账户的访问权限
            folder.open(Folder.READ_WRITE);
            //得到邮件账户的所有邮件信息
            messages = folder.getMessages();
            System.out.println("邮件数量:　" + messages.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dealMessage(messages);
    }

    private List<EmailMessage> dealMessage(Message[] messages){
        mailService = new MailService(context);
        assert messages != null;
        Message[] temp = mailService.isNewMessage(email,messages);
        List<EmailMessage> MessageList = new ArrayList<EmailMessage>();
        EmailMessage emailMessage = null;
        if(temp != null){
            System.out.println("邮件数量:　" + temp.length);

            Log.d(TAG, "SinaRecipient: "+"邮件数量:　" + temp.length);
            ShowMail re = null;
            for (int i = 0; i < temp.length; i++) {
                re = new ShowMail((MimeMessage) temp[i],context,email.getEmail_id());
                emailMessage = new EmailMessage();
                try {
                    emailMessage.setMessage_id(re.getMessageId());
                    emailMessage.setEmail_id(email.getEmail_id());
                    emailMessage.setUser_id(email.getUser_id());
                    emailMessage.setSubject(re.getSubject());
                    emailMessage.setSendDate(re.getSentDate());
                    emailMessage.setFrom(re.getFrom());
                    emailMessage.setTo(re.getMailAddress("to"));
                    emailMessage.setCc(re.getMailAddress("cc"));
                    emailMessage.setBcc(re.getMailAddress("bcc"));
                    emailMessage.setIsRead(0);
                    emailMessage.setIsStar(0);
                    emailMessage.setIsDelete(0);
                    emailMessage.setFolder_id(1);
                    re.getMailContent((Part) temp[i]);
                    String BodyText = "\r\n" + re.getBodyText();
                    emailMessage.setContent(BodyText);
                    MessageList.add(emailMessage);
                    if(re.isContainAttach(messages[i])){
                        emailMessage.setIsAttachment(1);
                        Log.d(TAG,"第"+i+"封"+"有附件");
                        re.saveAttachMent(messages[i],re.getMessageId());
                    }else{
                        Log.d(TAG,"第"+i+"封"+"没附件");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
//            Boolean isRead = re.isContainAttach((Part) message);
//                re.setDateFormat("yy年MM月dd日　HH:mm");
//                String mailSendDate = re.getSentDate();
        }else {
            Log.d(TAG, email.getType()+"没有新邮件");
        }
        //关闭邮件夹对象
        try {
            folder.close();
            store.close();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return MessageList;
    }


}
