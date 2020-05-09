package com.example.ttett.util.mailUtil;

import com.example.ttett.Entity.Email;

import java.util.Properties;

import javax.mail.Session;

public class EmailProp {
    public Session getSTMPSession(Email email){
        String account[] = email.getAddress().split("@");
        String mailTpye = "";
        mailTpye = account[1];
        String SMTPHost = "";//smtp服务器地址
        String SMTPPort = "";//smtp服务器地址端口
        switch (mailTpye) {
            case "qq.com":
            case "foxmail.com":
                SMTPHost = "smtp.qq.com";
                SMTPPort = "465";
                break;
            case "sina.com":
                SMTPHost = "smtp.sina.com";
                SMTPPort = "25";
                break;
            case "sina.cn":
                SMTPHost = "smtp.sina.cn";
                SMTPPort = "25";
                break;
            case "139.com":
                SMTPHost = "smtp.139.com";
                SMTPPort = "465";
                break;
            case "163.com":
                SMTPHost = "smtp.163.com";
                SMTPPort = "25";
                break;
            case "188.com":
                SMTPHost = "smtp.188.com";
                SMTPPort = "25";
                break;
            case "126.com":
                SMTPHost = "smtp.126.com";
                SMTPPort = "25";
                break;
            case "gmail.com":
                SMTPHost = "smtp.gmail.com";
                SMTPPort = "465";
                break;
            case "outlook.com":
                SMTPHost = "smtp.outlook.com";
                SMTPPort = "465";
                break;
            default:
                System.err.println("暂时不支持此账号作为服务账号发送邮件！");
                break;
        }
        Properties prop = new Properties();
        prop.setProperty("mail.transport.protocol", "smtp"); // 设置邮件传输采用的协议smtp
        prop.setProperty("mail.smtp.host", SMTPHost);// 设置发送人邮件服务器的smtp地址
        prop.setProperty("mail.smtp.auth", "true");     // 设置验证机制
        prop.setProperty("mail.smtp.port", SMTPPort);// SMTP 服务器的端口 (非 SSL 连接的端口一般默认为 25, 可以不添加, 如果开启了 SSL 连接,
//        if(!SMTPPort.equals("25")){
            prop.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            prop.setProperty("mail.smtp.socketFactory.fallback", "false");
            prop.setProperty("mail.smtp.socketFactory.port", SMTPPort);
//        }
        Session session = Session.getInstance(prop);
        return session;
    }
}
