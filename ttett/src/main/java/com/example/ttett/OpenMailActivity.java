package com.example.ttett;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ttett.Adapter.AttachmentAdapter;
import com.example.ttett.Adapter.InboxAdapter;
import com.example.ttett.CustomDialog.MenuDialogFragment;
import com.example.ttett.CustomDialog.TranDialogFragment;
import com.example.ttett.Entity.Attachment;
import com.example.ttett.Entity.EmailMessage;
import com.example.ttett.Service.AttachmentService;
import com.example.ttett.Service.MailService;
import com.example.ttett.bean.MessageEvent;

import org.greenrobot.eventbus.EventBus;

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
        EventBus.getDefault().postSticky(new MessageEvent("update_message"));

        RichEditor webView = findViewById(R.id.webView);
        TextView tvSubject = findViewById(R.id.mail_subject);
        TextView tvFromId = findViewById(R.id.from_id);
        TextView tvFromMail = findViewById(R.id.from_mail);
        TextView tvToId = findViewById(R.id.to_id);
        TextView tvToMail = findViewById(R.id.to_mail);
        TextView tvDate = findViewById(R.id.mail_date);
        ImageView menu = findViewById(R.id.mail_menu);
        ImageView tran = findViewById(R.id.mail_tran);

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

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTranDialog();
            }
        });
        tran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMenuDialog();
            }
        });
    }

    private void showTranDialog(){
        MenuDialogFragment fragment = new MenuDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("message",emailMessage);
        fragment.setArguments(bundle);
        fragment.show(getSupportFragmentManager(),"MenuDialogFragment");
    }

    private void showMenuDialog(){
        TranDialogFragment fragment = new TranDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("message",emailMessage);
        fragment.setArguments(bundle);
        fragment.show(getSupportFragmentManager(),"TranDialogFragment");
    }
}
