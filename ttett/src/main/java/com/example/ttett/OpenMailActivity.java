package com.example.ttett;

import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ttett.Adapter.AttachmentAdapter;
import com.example.ttett.Entity.Attachment;
import com.example.ttett.Entity.EmailMessage;
import com.example.ttett.Service.AttachmentService;
import com.example.ttett.Service.MailService;
import com.example.ttett.util.ImageGetterUtils;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class OpenMailActivity extends AppCompatActivity {
    private TextView TvSubject,TvFromId,TvFromMail,TvToId,TvToMail,TvDate,TvContent;
    private ImageView Iv_mail;
    private MailService mailService;
    private AttachmentService attachmentService;
    private AttachmentAdapter attachmentAdapter;
    private RecyclerView AttachmentRv;
    private String TAG = "OpenMailActivity";

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
        AttachmentRv = findViewById(R.id.mail_attachment_rv);

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
        TvContent.setText(Html.fromHtml(emailMessage.getContent(),new ImageGetterUtils.MyImageGetter(this,TvContent),null));
        TvDate.setText(emailMessage.getSendDate());
        attachmentService = new AttachmentService(this);
        AttachmentRv.setVisibility(View.GONE);
        Log.d(TAG,emailMessage.getMessage_id());
        if(emailMessage.getIsAttachment() != 0){
            List<Attachment> attachments = attachmentService.queryMessageAllAttachment(emailMessage.getMessage_id());
            if(attachments!=null){
                AttachmentRv.setVisibility(View.VISIBLE);
                AttachmentRv.setLayoutManager(new LinearLayoutManager(this));
                attachmentAdapter = new AttachmentAdapter(this,attachments);
                attachmentAdapter.setOPEN_MESSAGE();
                AttachmentRv.setAdapter(attachmentAdapter);
            }
        }

        Iv_mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(OpenMailActivity.this, "mail_button", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
