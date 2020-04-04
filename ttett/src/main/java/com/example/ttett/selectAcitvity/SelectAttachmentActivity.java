package com.example.ttett.selectAcitvity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.ttett.Adapter.SelectAttachmentAdapter;
import com.example.ttett.Entity.Attachment;
import com.example.ttett.R;
import com.example.ttett.Service.AttachmentService;
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

public class SelectAttachmentActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar mToolbar;
    private RecyclerView selectRv;
    private TextView tvCancel;
    private TextView tvConfirm;
    private Map<Integer, Boolean> checkStatus;
    private ArrayList<Integer> id_item;
    private AttachmentService attachmentService;
    private List<Attachment> attachments;
    private SelectAttachmentAdapter adapter;
    private String TAG = "SelectAttachmentActivity";

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
        attachmentService = new AttachmentService(this);
        attachments = attachmentService.queryAllAttachment(email_id);
        checkStatus = new HashMap<>();
        for(int i =0;i < attachments.size();i++){
            checkStatus.put(i,false);
        }

        selectRv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SelectAttachmentAdapter(this,attachments,checkStatus);
        selectRv.setAdapter(adapter);
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
        if(event.getMessage().equals("attachment_check_status")){
            checkStatus = event.getCheckStatus();
            id_item = new ArrayList<Integer>();
            for(Map.Entry<Integer,Boolean> entry:checkStatus.entrySet()){
                if(entry.getValue()){
                    id_item.add(attachments.get(entry.getKey()).getAttachment_id());
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cancel_select:
                break;
            case R.id.confirm_select:
                Intent intent = new Intent();
                intent.putIntegerArrayListExtra("id_item",id_item);
                setResult(Activity.RESULT_OK,intent);
                SelectAttachmentActivity.this.finish();
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
