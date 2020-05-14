package com.example.ttett.Folder_module;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.ttett.Adapter.InboxAdapter;
import com.example.ttett.Entity.Email;
import com.example.ttett.Entity.EmailMessage;
import com.example.ttett.Entity.Folder;
import com.example.ttett.R;
import com.example.ttett.Service.MailService;
import com.example.ttett.bean.MessageEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class OpenFolderActivity extends AppCompatActivity {
private InboxAdapter inboxAdapter;
private RecyclerView Rv;
private Folder folder;
private TextView TvTitle;
private Email email;
private String TAG = "OpenFolderActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_open_folder);
        Rv = findViewById(R.id.open_folder_rv);
        TvTitle = findViewById(R.id.title);

        folder = getIntent().getParcelableExtra("folder");
        email = getIntent().getParcelableExtra("email");
        TvTitle.setText(folder.getFolder_name());

        MailService mailService = new MailService(this);
        List<EmailMessage> emailMessages = mailService.queryFolderMessage(folder.getFolder_id(),email.getEmail_id());
        if(emailMessages!=null){
            Rv.setLayoutManager(new LinearLayoutManager(this));
            Rv.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
            inboxAdapter = new InboxAdapter(this,emailMessages,InboxAdapter.inboxFragment,email);
            Rv.setAdapter(inboxAdapter);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void OpenFolder(MessageEvent event){
        if (event.getMessage().equals("Open_Folder")){
            folder = event.getFolder();
            Log.d(TAG,folder.toString());
            email = event.getEmail();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
