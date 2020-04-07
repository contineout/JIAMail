package com.example.ttett;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ttett.Adapter.UserEmailAdapter;
import com.example.ttett.Entity.Email;
import com.example.ttett.Service.EmailService;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo);
        Rv = findViewById(R.id.user_mail_rv);
        add_email = findViewById(R.id.add_email);
        exit = findViewById(R.id.exit);

        try{
            user_id = getIntent().getIntExtra("user_id",0);
        }catch (NullPointerException ignored){
        }
        EmailService emailService = new EmailService(this);
        List<Email> emails = emailService.queryAllEmail(user_id);
        if(emails!=null){
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            Rv.setLayoutManager(layoutManager);
            adapter = new UserEmailAdapter(this,emails);
            Rv.setAdapter(adapter);
        }

        add_email.setOnClickListener(this);
        exit.setOnClickListener(this);
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
