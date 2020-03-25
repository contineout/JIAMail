package com.example.ttett.util;

import com.example.ttett.Entity.Email;
import com.example.ttett.Entity.EmailMessage;

import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendMessage {

    public boolean QQSend(EmailMessage emailMessage, Email email) {
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
            Message msg = getMimeMessage(session, emailMessage);
            Transport transport = session.getTransport();
            transport.connect(email.getAddress(), email.getAuthorizationCode());
            transport.sendMessage(msg, msg.getAllRecipients());
            transport.close();
            return transport.isConnected();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean SinaSend(EmailMessage emailMessage, Email email) {
        Properties props = new Properties();
        props.setProperty("mail.smtp.auth","true");
        props.setProperty("mail.transport.protocol","smtp");
        props.setProperty("mail.smtp.host","smtp.sina.com");
        Session session = Session.getInstance(props);

        session.setDebug(true);

        try {
            Message msg = getMimeMessage(session, emailMessage);
            Transport transport = session.getTransport();
            transport.connect(email.getAddress(), email.getAuthorizationCode());
            transport.sendMessage(msg, msg.getAllRecipients());
            transport.close();
            return transport.isConnected();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    private static MimeMessage getMimeMessage(Session session, EmailMessage emailMessage) throws Exception {

        MimeMessage msg = new MimeMessage(session);

        msg.setFrom(new InternetAddress(emailMessage.getFrom(), "WUJIA", "UTF-8"));

        msg.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(emailMessage.getTo(), "LL", "UTF-8"));

        if(!emailMessage.getBcc().isEmpty()){
            msg.setRecipient(MimeMessage.RecipientType.CC,new InternetAddress(emailMessage.getBcc()));
        }
        if(!emailMessage.getCc().isEmpty()){
            msg.setRecipient(MimeMessage.RecipientType.BCC,new InternetAddress(emailMessage.getBcc()));
        }

        msg.setSubject(emailMessage.getSubject(), "UTF-8");

        msg.setContent(emailMessage.getContent(), "text/html;charset=UTF-8");

        msg.setSentDate(new Date());

        return msg;
    }


}
