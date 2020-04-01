package com.example.ttett.MailSqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.ttett.Constants.SqlConstants;

import androidx.annotation.Nullable;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    public static final String USER = "CREATE TABLE USER(" +
            "id integer primary key autoincrement," +
            "account text," +
            "password text" +
            ")";

    public static final String EMAIL="CREATE TABLE EMAIL (" +
            "id integer PRIMARY KEY AUTOINCREMENT," +
            "user_id integer," +
            "email_type text," +
            "email_address text," +
            "AuthorizationCode text," +
            "email_name text," +
            "FOREIGN KEY (user_id) REFERENCES User (id)" +
            ")";

    public static final String FOLDER="CREATE TABLE FOLDER (" +
            "id integer PRIMARY KEY AUTOINCREMENT," +
            "email_id integer," +
            "folder_name text," +
            "message_number integer," +
            "datetime text," +
            "FOREIGN KEY (email_id) REFERENCES EMAIL (id)" +
            ")";

    public static final String EMAILMESSAGE="CREATE TABLE EMAILMESSAGE (" +
            "id integer PRIMARY KEY AUTOINCREMENT," +
            "message_id text," +
            "email_id integer," +
            "folder_id integer," +
            "user_id integer," +
            "subject text," +
            "from_mail text," +
            "to_mail text," +
            "cc text," +
            "bcc text," +
            "content text," +
            "sendDate text," +
            "from_image image," +
            "isStar bit," +
            "isRead bit," +
            "isSend bit," +
            "isDelete bit," +
            "attachment text," +
            "isAttachment bit," +
            "FOREIGN KEY (email_id) REFERENCES EMAIL (id)," +
            "FOREIGN KEY (folder_id) REFERENCES FOLDER (id)," +
            "FOREIGN KEY (user_id) REFERENCES USER (id)"+
            ")";



    public static final String CONTACT="CREATE TABLE CONTACT (" +
            "id integer PRIMARY KEY AUTOINCREMENT," +
            "email_id integer," +
            "contacts_name text," +
            "contacts_remark text," +
            "contacts_birthday text," +
            "contacts_company text," +
            "contacts_department text," +
            "contacts_position text," +
            "contacts_email text," +
            "contacts_iphone text," +
            "contact_address text," +
            "FOREIGN KEY (email_id) REFERENCES EMAIL(email_id)" +
            ")";
    public static final String ATTACHMENT="CREATE TABLE ATTACHMENT (" +
            "id integer PRIMARY KEY AUTOINCREMENT," +
            "email_id integer," +
            "message_id text," +
            "name text," +
            "type text," +
            "size text," +
            "saveDate text," +
            "FOREIGN KEY (email_id) REFERENCES EMAIL(email_id)" +
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
