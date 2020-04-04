package com.example.ttett.util;

import com.example.ttett.Entity.Attachment;
import com.example.ttett.Entity.Email;
import com.example.ttett.Entity.EmailMessage;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

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

    private EmailMessage emailMessage;
    private Email email;
    private List<Attachment> attachments;
    private List<MimeBodyPart> mimeBodyParts;
    private boolean isSuccess;

    public SendMessage(EmailMessage emailMessage, Email email, List<Attachment> attachments) {
        this.emailMessage = emailMessage;
        this.email = email;
        this.attachments = attachments;
    }

    public Boolean SendMessage(){
        switch (email.getType()){
            case "qq.com":
                QQSend();
                break;
            case  "sina.com":
                SinaSend();
                break;
            default:
        }
        return isSuccess;
    }


    private boolean QQSend() {
        Properties props = new Properties();
        props.setProperty("mail.smtp.auth", "true");
        props.setProperty("mail.transport.protocol", "smtp");
        props.setProperty("mail.smtp.host", "smtp.qq.com");
        final String smtpPort = "465";
        props.setProperty("mail.smtp.port", smtpPort);
        props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.setProperty("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.smtp.socketFactory.port", "smtpPort");
        Session session = Session.getInstance(props);

        session.setDebug(true);

        try {
            Message msg = createMimeMessage(session);
            Transport transport = session.getTransport();
            transport.connect(email.getAddress(), email.getAuthorizationCode());
            transport.sendMessage(msg, msg.getAllRecipients());
            isSuccess =transport.isConnected();
            transport.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return isSuccess;
    }

    private boolean SinaSend() {
        Properties props = new Properties();
        props.setProperty("mail.smtp.auth","true");
        props.setProperty("mail.transport.protocol","smtp");
        props.setProperty("mail.smtp.host","smtp.sina.com");
        Session session = Session.getInstance(props);

        try {
            Message msg = createMimeMessage(session);
            Transport transport = session.getTransport();
            transport.connect(email.getAddress(), email.getAuthorizationCode());
            transport.sendMessage(msg, msg.getAllRecipients());
            isSuccess =transport.isConnected();
            transport.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isSuccess;
    }


    private MimeMessage createMimeMessage(Session session) throws Exception {

        MimeMessage msg = new MimeMessage(session);
        //发件人
        msg.setFrom(new InternetAddress(emailMessage.getFrom(), email.getName(), "UTF-8"));
        //接收人
        msg.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(emailMessage.getTo(), "LL", "UTF-8"));
        //密送
        if(!emailMessage.getBcc().isEmpty()){
            msg.setRecipient(MimeMessage.RecipientType.CC,new InternetAddress(emailMessage.getCc()));
        }
        //抄送
        if(!emailMessage.getCc().isEmpty()){
            msg.setRecipient(MimeMessage.RecipientType.BCC,new InternetAddress(emailMessage.getBcc()));
        }
        //主题
        msg.setSubject(emailMessage.getSubject(), "UTF-8");

        msg.setContent(emailMessage.getContent(), "text/html;charset=UTF-8");

        //附件根节点
        mimeBodyParts = new ArrayList<>();
        int i = 0;
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
        //日期
        msg.setSentDate(new Date());

        return msg;
    }

    private static MimeBodyPart createContent(String body, String filename) throws Exception{
        MimeBodyPart contentPart = new MimeBodyPart();
        MimeMultipart contentMultipart = new MimeMultipart("related");
        MimeBodyPart htmlBodyPart = new MimeBodyPart();
        htmlBodyPart.setContent(body,"text/html;charset=gb2312");
        contentMultipart.addBodyPart(htmlBodyPart);
        MimeBodyPart pngBodyPart = new MimeBodyPart();
        FileDataSource fds = new FileDataSource(filename);
        pngBodyPart.setDataHandler(new DataHandler(fds));
        pngBodyPart.setContentID("it315_logo_png");
        contentMultipart.addBodyPart(pngBodyPart);

        contentPart.setContent(contentMultipart);
        return contentPart;
    }

    private static MimeBodyPart createAttachment(String filename) throws Exception{
        MimeBodyPart attachPart = new MimeBodyPart();
        FileDataSource fds = new FileDataSource(filename);
        attachPart.setDataHandler(new DataHandler(fds));
        attachPart.setFileName(fds.getName());
        return attachPart;
    }


}
