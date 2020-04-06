package com.example.ttett;

import android.os.Bundle;

import com.example.ttett.Entity.Contact;
import com.example.ttett.Entity.EmailMessage;
import com.example.ttett.MailDialog_module.MailDialogAdapter;
import com.example.ttett.Service.MailService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MailDialogActivity extends AppCompatActivity {
    private MailDialogAdapter adapter;
    private Map<Integer,Boolean> clickStatus = new HashMap<Integer, Boolean>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail_dialog);

        final RecyclerView Rv = findViewById(R.id.mail_dialog_rv);
        Bundle bundle = getIntent().getExtras();
        Contact contact = bundle.getParcelable("contact");

        MailService mailService = new MailService(this);
        List<EmailMessage> emailMessage = mailService.queryDialogMessage(contact.getEmail());



        if(emailMessage!=null){
            Rv.setLayoutManager(new LinearLayoutManager(this));
            adapter = new MailDialogAdapter(this, emailMessage);
            Rv.setAdapter(adapter);
        }
    }
}
