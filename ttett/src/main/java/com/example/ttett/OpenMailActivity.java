package com.example.ttett;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ttett.Entity.EmailMessage;

import androidx.appcompat.app.AppCompatActivity;

public class OpenMailActivity extends AppCompatActivity {
    private TextView TvSubject,TvFromId,TvFromMail,TvToId,TvToMail,TvDate,TvContent;
    private ImageView Iv_mail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_mail);

        Bundle bundle = getIntent().getExtras();
        EmailMessage emailMessage = bundle.getParcelable("emailMessage");

        TvSubject = findViewById(R.id.mail_subject);
        TvFromId = findViewById(R.id.from_id);
        TvFromMail = findViewById(R.id.from_mail);
        TvToId = findViewById(R.id.to_id);
        TvToMail = findViewById(R.id.to_mail);
        TvDate = findViewById(R.id.mail_date);
        Iv_mail = findViewById(R.id.mail_button);
        TvContent = findViewById(R.id.mail_context);

        TvSubject.setText(emailMessage.getSubject());
        TvFromId.setText(emailMessage.getFrom());
        TvToId.setText(emailMessage.getTo());
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
