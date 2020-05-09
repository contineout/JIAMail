package com.example.ttett.util.mailUtil;

import com.example.ttett.Entity.Email;

import java.util.Properties;

import javax.mail.Session;
import javax.mail.Transport;

public class EmailLogin {

    public boolean SinaLogin(Email email){
        Properties props = new Properties();
        props.setProperty("mail.smtp.auth", "true");
        props.setProperty("mail.transport.protocol", "smtp");
        props.setProperty("mail.smtp.host", "smtp.sina.com");
        Session session = Session.getInstance(props);
        Transport transport = null;
        try {
            transport = session.getTransport();
            transport.connect(email.getAddress(),email.getAuthorizationCode());
        } catch (Exception e) {
            e.printStackTrace();
        }
        assert transport != null;
        return transport.isConnected();
    }

    public boolean QQLogin(Email email){
        Properties props = new Properties();
        props.setProperty("mail.smtp.auth", "true");
        props.setProperty("mail.transport.protocol", "smtp");
        props.setProperty("mail.smtp.host", "smtp.qq.com");
        String smtpPort = "465";
        props.setProperty("mail.smtp.port", smtpPort);
        props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.setProperty("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.smtp.socketFactory.port", "smtpPort");
        Session session = Session.getInstance(props);
        Transport transport = null;
        try {
            transport = session.getTransport();
            transport.connect(email.getAddress(),email.getAuthorizationCode());
        } catch (Exception e) {
            e.printStackTrace();
        }
        assert transport != null;
        return transport.isConnected();
    }
}
