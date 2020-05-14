package com.example.ttett.util.mailUtil;

import com.example.ttett.Entity.Email;

import java.util.Properties;

import javax.mail.Session;
import javax.mail.Store;

public class EmailProp {
    public static Session getSTMPSession(Email email) {
        String account[] = email.getAddress().split("@");
        String mailTpye = "";
        mailTpye = account[1];
        String SMTPHost = "";//smtp服务器地址
        String SMTPPort = "";//smtp服务器地址端口
        Properties prop = null;
        Session session = null;
        switch (mailTpye) {
            case "qq.com":
            case "foxmail.com":
                SMTPHost = "smtp.qq.com";
                SMTPPort = "465";
                prop = new Properties();
                prop.setProperty("mail.transport.protocol", "smtp"); // 设置邮件传输采用的协议smtp
                prop.setProperty("mail.smtp.host", SMTPHost);// 设置发送人邮件服务器的smtp地址
                prop.setProperty("mail.smtp.auth", "true");     // 设置验证机制
                prop.setProperty("mail.smtp.port", SMTPPort);// SMTP 服务器的端口 (非 SSL 连接的端口一般默认为 25, 可以不添加, 如果开启了 SSL 连接,
                prop.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
                prop.setProperty("mail.smtp.socketFactory.fallback", "false");
                prop.setProperty("mail.smtp.socketFactory.port", SMTPPort);
                session = Session.getInstance(prop);
                return session;
            case "sina.com":
                SMTPHost = "smtp.sina.com";
                SMTPPort = "25";
                break;
            case "163.com":
                SMTPHost = "smtp.163.com";
                SMTPPort = "25";
                break;
            case "gmail.com":
                SMTPHost = "smtp.gmail.com";
                SMTPPort = "465";
                break;
            case "outlook.com":
                prop = new Properties();
                prop.setProperty("mail.smtp.port", "587");
                prop.setProperty("mail.smtp.host", "smtp.office365.com");//要连接的SMTP服务器
                //当前smtp host设为可信任 否则抛出javax.mail.MessagingException: Could not                   convert socket to TLS
                prop.setProperty("mail.smtp.ssl.trust", "smtp.office365.com");
                prop.setProperty("mail.transport.protocol", "smtp");// 发送邮件协议名称
                prop.setProperty("mail.smtp.auth", "true");//是否开启身份验证
                prop.setProperty("mail.smtp.starttls.enable", "true");//是否将纯文本连接升级为加密连接(TLS或SSL)
                prop.setProperty("mail.smtp.ssl.checkserveridentity", "false");// 不做服务器证书校验
                session = Session.getInstance(prop);
                return session;
            default:
                System.err.println("暂时不支持此账号作为服务账号发送邮件！");
                break;
        }
        prop = new Properties();
        prop.setProperty("mail.transport.protocol", "smtp"); // 设置邮件传输采用的协议smtp
        prop.setProperty("mail.smtp.host", SMTPHost);// 设置发送人邮件服务器的smtp地址
        prop.setProperty("mail.smtp.auth", "true");     // 设置验证机制
        prop.setProperty("mail.smtp.port", SMTPPort);// SMTP 服务器的端口 (非 SSL 连接的端口一般默认为 25, 可以不添加, 如果开启了 SSL 连接,
        if(!SMTPPort.equals("25")){
            prop.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            prop.setProperty("mail.smtp.socketFactory.fallback", "false");
            prop.setProperty("mail.smtp.socketFactory.port", SMTPPort);
        }
        session = Session.getInstance(prop);
        return session;
    }

    public static Store getPOP3Properties(Email email) {
        String account[] = email.getAddress().split("@");
        String mailTpye = "";
        mailTpye = account[1];
        String SMTPHost = "";//smtp服务器地址
        String SMTPPort = "";//smtp服务器地址端口
        Properties prop = null;
        Session session = null;
        Store store = null;
        switch (mailTpye) {
            case "qq.com":
                prop = new Properties();
                prop.setProperty("mail.store.protocol", "pop3");
                prop.setProperty("mail.pop3.host", "pop.qq.com");
                session = Session.getInstance(prop);
                try {
                    store = session.getStore("pop3");
                    store.connect("pop.qq.com", email.getAddress(), email.getAuthorizationCode());
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
            case "foxmail.com":
                prop = new Properties();
                prop.setProperty("mail.store.protocol", "pop3");
                prop.setProperty("mail.pop3.host", "pop.foxmail.com");
                session = Session.getInstance(prop);
                try {
                    store = session.getStore("pop3");
                    store.connect("pop.foxmail.com", email.getAddress(), email.getAuthorizationCode());
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
            case "sina.com":
                prop = new Properties();
                prop.setProperty("mail.store.protocol", "pop3");
                prop.setProperty("mail.pop3.host", "pop3.sina.com");
                session = Session.getInstance(prop);
                try {
                    store = session.getStore("pop3");
                    store.connect("pop3.sina.com", email.getAddress(), email.getAuthorizationCode());
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
            case "163.com":
                prop = new Properties();
                prop.setProperty("mail.store.protocol", "pop3");
                prop.setProperty("mail.pop3.host", "pop.163.com");
                session = Session.getInstance(prop);
                try {
                    store = session.getStore("pop3");
                    store.connect("pop.163.com", email.getAddress(), email.getAuthorizationCode());
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
            case "gmail.com":
                SMTPHost = "smtp.gmail.com";
                SMTPPort = "465";
                break;
            case "outlook.com":
                prop = new Properties();
                prop.setProperty("mail.store.protocol", "pop3");
                prop.setProperty("mail.pop3.host", "outlook.office365.com");
                prop.setProperty("mail.pop3.auth", "true");//是否开启身份验证
                prop.setProperty("mail.pop3.starttls.enable", "true");//是否将纯文本连接升级为加密连接(TLS或SSL)
                prop.setProperty("mail.pop3.ssl.checkserveridentity", "false");// 不做服务器证书校验
                session = Session.getInstance(prop);
                try {
                    store = session.getStore("pop3");
                    store.connect("outlook.office365.com", email.getAddress(), email.getAuthorizationCode());
                }catch (Exception e){
                    e.printStackTrace();
                }
            default:
                System.err.println("暂时不支持此账号作为服务账号发送邮件！");
                break;
        }
        return store;
    }
}
