package com.example.ttett;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ttett.Entity.EmailMessage;
import com.example.ttett.Service.MailService;

import androidx.appcompat.app.AppCompatActivity;

public class OpenMailActivity extends AppCompatActivity {
    private TextView TvSubject,TvFromId,TvFromMail,TvToId,TvToMail,TvDate,TvContent;
    private ImageView Iv_mail;
    private MailService mailService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_mail);

        Bundle bundle = getIntent().getExtras();
        EmailMessage emailMessage = bundle.getParcelable("emailMessage");

        mailService = new MailService(this);
        mailService.updateReadMessage(emailMessage.getId());


        TvSubject = findViewById(R.id.mail_subject);
        TvFromId = findViewById(R.id.from_id);
        TvFromMail = findViewById(R.id.from_mail);
        TvToId = findViewById(R.id.to_id);
        TvToMail = findViewById(R.id.to_mail);
        TvDate = findViewById(R.id.mail_date);
        Iv_mail = findViewById(R.id.mail_button);
        TvContent = findViewById(R.id.mail_context);

        try {
            if (!emailMessage.getFrom().isEmpty() && !emailMessage.getTo().isEmpty()) {
                String[] from = emailMessage.getFrom().split("[<>]");
                String[] to = emailMessage.getTo().split("[<>]");
                TvFromId.setText(from[0]);
                TvFromMail.setText(from[1]);
                TvToId.setText(to[0]);
                TvToMail.setText(to[1]);
            }
        }catch (Exception e){

        }

        TvSubject.setText(emailMessage.getSubject());
        TvContent.setText(emailMessage.getContent());
        TvDate.setText(emailMessage.getSendDate());

        Iv_mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(OpenMailActivity.this, "mail_button", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
