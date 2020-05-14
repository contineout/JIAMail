package com.example.ttett;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ttett.Adapter.UserEmailAdapter;
import com.example.ttett.CustomDialog.DeleteEmailFragment;
import com.example.ttett.Entity.Email;
import com.example.ttett.Service.EmailService;
import com.example.ttett.bean.MessageEvent;
import com.example.ttett.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class UserinfoActivity extends AppCompatActivity implements View.OnClickListener {

    private int user_id;
    private UserEmailAdapter adapter;
    private RecyclerView Rv;
    private TextView add_email;
    private ImageView exit;
    private List<Email> emails;
    private EmailService emailService;
    private String address;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo);
        EventBus.getDefault().register(this);
        Rv = findViewById(R.id.user_mail_rv);
        add_email = findViewById(R.id.add_email);
        exit = findViewById(R.id.exit);

        try{
            user_id = getIntent().getIntExtra("user_id",0);
        }catch (NullPointerException ignored){
        }
        emailService = new EmailService(this);
        emails = emailService.queryAllEmail(user_id);
        if(emails!=null){
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            Rv.setLayoutManager(layoutManager);
            adapter = new UserEmailAdapter(this, emails, new UserEmailAdapter.OnClickItemListener() {
                @Override
                public void onClick(int email_id,String email_address) {
                    DeleteEmailFragment fragment = new DeleteEmailFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt("email_id", email_id);
                    address = email_address;
                    fragment.setArguments(bundle);
                    fragment.show(getSupportFragmentManager(),"deleteDialogFragment");
                }
            });
            Rv.setAdapter(adapter);
        }

        add_email.setOnClickListener(this);
        exit.setOnClickListener(this);
    }

    /**
     * 更改inbox
     * @param messageEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void deleteEmail(MessageEvent messageEvent){
        if (messageEvent.getMessage().equals("deleteEmail")){
            Email email = null;
            emails.clear();
            List<Email> emailList = emailService.queryAllEmail(user_id);
            if(emailList!=null){
                emails.addAll(emailList);
                EventBus.getDefault().postSticky(new MessageEvent("Switch_Email",emails.get(0)));
            }else {
                EventBus.getDefault().postSticky(new MessageEvent("Switch_Email",email));
            }
            adapter.notifyDataSetChanged();
            ToastUtil.showTextToas(this,address+"邮箱已经删除");
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()){
            case R.id.add_email:
                intent = new Intent(this,LoginEmailActivity.class);
                intent.putExtra("user_id",user_id);
                startActivityForResult(intent,1);
                break;
            case R.id.exit:
                finish();
                break;
        }
    }
}
