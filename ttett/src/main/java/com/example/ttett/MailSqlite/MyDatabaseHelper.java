package com.example.ttett.MailSqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.ttett.Constants.SqlConstants;

import androidx.annotation.Nullable;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    public static final String USER = "CREATE TABLE USER(" +
            "id INTEGER primary key autoincrement," +
            "account varchar(20) NOT NULL," +
            "password varchar(20) NOT NULL" +
            ")";

    public static final String EMAIL="CREATE TABLE EMAIL (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "user_id int(11) NOT NULL," +
            "email_type varchar(15)," +
            "email_address varchar(50)," +
            "AuthorizationCode varchar(30)," +
            "email_name varchar(15)," +
            "message_count int(5)," +
            "avatar_color text,"+
            "FOREIGN KEY (user_id) REFERENCES USER (id)" +
            ")";

    public static final String FOLDER="CREATE TABLE FOLDER (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "email_id int(11)," +
            "folder_name varchar(30)," +
            "message_number int(5)," +
            "datetime text," +
            "FOREIGN KEY (email_id) REFERENCES EMAIL (id)" +
            ")";

    public static final String EMAILMESSAGE="CREATE TABLE EMAILMESSAGE (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "message_id varchar(100)," +
            "email_id int(11)," +
            "folder_id int(11)," +
            "user_id int(11)," +
            "subject varchar(200)," +
            "from_mail varchar(50)," +
            "to_mail varchar(200)," +
            "cc varchar(200)," +
            "bcc varchar(200)," +
            "content text," +
            "sendDate text," +
            "avatar_color text," +
            "isStar bit," +
            "isRead bit," +
            "isSend bit," +
            "isDelete bit," +
            "attachment varchar(200)," +
            "isAttachment bit," +
            "FOREIGN KEY (email_id) REFERENCES EMAIL (id)," +
            "FOREIGN KEY (folder_id) REFERENCES FOLDER (id)," +
            "FOREIGN KEY (user_id) REFERENCES USER (id)"+
            ")";



    public static final String CONTACT="CREATE TABLE CONTACT (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "email_id int(11)," +
            "contacts_name varchar(15)," +
            "contacts_remark varchar(40)," +
            "contacts_birthday text," +
            "contacts_company varchar(30)," +
            "contacts_department varchar(30)," +
            "contacts_position varchar(30)," +
            "contacts_email varchar(50)," +
            "contacts_iphone int(15)," +
            "contact_address varchar(100)," +
            "avatar_color text,"+
            "FOREIGN KEY (email_id) REFERENCES EMAIL(id)" +
            ")";
    public static final String ATTACHMENT="CREATE TABLE ATTACHMENT (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "email_id int(11)," +
            "emailmessage_id int(11),"+
            "message_id varchar(100)," +
            "name varchar(15)," +
            "type varchar(15)," +
            "size text," +
            "saveDate text," +
            "path varchar(200)," +
            "FOREIGN KEY (email_id) REFERENCES EMAIL(id)," +
            "FOREIGN KEY (emailmessage_id) REFERENCES EMAILMESSAGE(id)"+
            ")";

    private Context mContext;

    public MyDatabaseHelper(@Nullable Context context) {
        super(context, SqlConstants.DataBase_Name, null, SqlConstants.VERSION_CODE);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(USER);
        db.execSQL(EMAIL);
        db.execSQL(FOLDER);
        db.execSQL(EMAILMESSAGE);
        db.execSQL(CONTACT);
        db.execSQL(ATTACHMENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
