package com.example.ttett;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ttett.Adapter.AttachmentAdapter;
import com.example.ttett.Adapter.InboxAdapter;
import com.example.ttett.Entity.Attachment;
import com.example.ttett.Entity.EmailMessage;
import com.example.ttett.Service.AttachmentService;
import com.example.ttett.Service.MailService;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cn.qzb.richeditor.RichEditor;

public class OpenMailActivity extends AppCompatActivity {
    private String TAG = "OpenMailActivity";
    private AttachmentService attachmentService;
    EmailMessage emailMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_mail);

        Bundle bundle = getIntent().getExtras();
        emailMessage = bundle.getParcelable("emailMessage");
        String fromFrag = bundle.getString("from_Frag");

        MailService mailService = new MailService(this);
        mailService.updateReadMessage(emailMessage.getId());

        RichEditor webView = findViewById(R.id.webView);
        TextView tvSubject = findViewById(R.id.mail_subject);
        TextView tvFromId = findViewById(R.id.from_id);
        TextView tvFromMail = findViewById(R.id.from_mail);
        TextView tvToId = findViewById(R.id.to_id);
        TextView tvToMail = findViewById(R.id.to_mail);
        TextView tvDate = findViewById(R.id.mail_date);
        ImageView iv_mail = findViewById(R.id.mail_button);
        RecyclerView attachmentRv = findViewById(R.id.mail_attachment_rv);
        attachmentRv.setVisibility(View.GONE);

        try {
            if (!emailMessage.getFrom().isEmpty() && !emailMessage.getTo().isEmpty()) {
                String[] from = emailMessage.getFrom().split("[<>]");
                String[] to = emailMessage.getTo().split("[<>]");
                tvFromId.setText(from[0]);
                tvFromMail.setText(from[1]);
                tvToId.setText(to[0]);
                tvToMail.setText(to[1]);
            }
        }catch (Exception e){

        }

        tvSubject.setText(emailMessage.getSubject());
        webView.setHtml(emailMessage.getContent());
        tvDate.setText(emailMessage.getSendDate());

        List<Attachment> attachments;
        List<Integer> id_item = new ArrayList<>();

        //判断打开时的fragment
        if(emailMessage.getIsAttachment() != 0){
            attachmentService = new AttachmentService(this);
            if(fromFrag.equals(InboxAdapter.sendedFragment)){
                String[] ids = emailMessage.getAttachment().split("[&]");
                for(String id:ids){
                    if(!id.equals("")){
                        id_item.add(Integer.parseInt(id));
                    }
                }
                 attachments = attachmentService.querySelectAttachment(id_item);
            }else{
                 attachments = attachmentService.queryMessageAllAttachment(emailMessage.getMessage_id());
            }
            if(attachments!=null){
                attachmentRv.setVisibility(View.VISIBLE);
                attachmentRv.setLayoutManager(new LinearLayoutManager(this));
                AttachmentAdapter attachmentAdapter = new AttachmentAdapter(this, attachments);
                attachmentAdapter.setOPEN_MESSAGE();
                attachmentRv.setAdapter(attachmentAdapter);
            }
        }

        iv_mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(OpenMailActivity.this, "mail_button", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getHtmlData(String bodyHTML) {
        String head = "<head>" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, user-scalable=no\"> " +
                "<style>img{max-width: 100%; width:auto; height:auto!important;}</style>" +
                "</head>";
        return "<html>" + head + "<body>" + bodyHTML + "</body></html>";
    }
}
