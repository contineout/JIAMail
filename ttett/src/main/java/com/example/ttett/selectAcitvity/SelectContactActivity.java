package com.example.ttett.selectAcitvity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.ttett.Adapter.SelectContactAdapter;
import com.example.ttett.Contact_module.ContactService;
import com.example.ttett.Entity.Contact;
import com.example.ttett.R;
import com.example.ttett.bean.MessageEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SelectContactActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar mToolbar;
    private RecyclerView selectRv;
    private TextView tvCancel;
    private TextView tvConfirm;
    private Map<Integer, Boolean> checkStatus;
    private ArrayList<Integer> id_item;
    private List<Contact> contacts;
    private SelectContactAdapter adapter;
    private String TAG = "SelectAttachmentActivity";
    private String flag = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_attachment);

        mToolbar = findViewById(R.id.select_toolbar);
        selectRv = findViewById(R.id.select_rv);
        tvCancel = findViewById(R.id.cancel_select);
        tvConfirm = findViewById(R.id.confirm_select);

        EventBus.getDefault().register(this);

        setSupportActionBar(mToolbar);
        int email_id = getIntent().getIntExtra("email_id",0);
        flag = getIntent().getStringExtra("flag");
        ContactService service = new ContactService(this);
        contacts = service.queryAllContact(email_id);
        checkStatus = new HashMap<>();
        if(contacts.size() > 0){
            for(int i =0;i < contacts.size();i++){
                checkStatus.put(i,false);
            }
            selectRv.setLayoutManager(new LinearLayoutManager(this));
            adapter = new SelectContactAdapter(this,contacts,checkStatus);
            selectRv.setAdapter(adapter);
        }
        tvCancel.setOnClickListener(this);
        tvConfirm.setOnClickListener(this);
    }

    /**
     * 获取选择的item状态和messageId
     * @param event
     */
    @SuppressLint("ResourceType")
    @Subscribe(threadMode = ThreadMode.POSTING)
    public void AttachmentCheck(MessageEvent event){
        if(event.getMessage().equals("contact_check_status")){
            checkStatus = event.getCheckStatus();
            id_item = new ArrayList<Integer>();
            for(Map.Entry<Integer,Boolean> entry:checkStatus.entrySet()){
                if(entry.getValue()){
                    id_item.add(contacts.get(entry.getKey()).getContact_id());
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cancel_select:
                finish();
                break;
            case R.id.confirm_select:
                EventBus.getDefault().post(new MessageEvent(flag,id_item));
                finish();
                break;
            default:
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
