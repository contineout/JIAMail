package com.example.ttett;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.ttett.Adapter.SelectAdapter;
import com.example.ttett.Entity.EmailMessage;
import com.example.ttett.bean.MessageEvent;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SelectMailActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private Toolbar mToolbar;
    private RecyclerView selectRv;
    private List<EmailMessage> emailMessages;
    private SelectAdapter selectAdapter;
    private String TAG = "SelectMailActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_mail);
        mToolbar = findViewById(R.id.select_toolbar);
        selectRv = findViewById(R.id.select_rv);
        EventBus.getDefault().register(this);

        setSupportActionBar(mToolbar);
        Bundle bundle = getIntent().getExtras();
        emailMessages = bundle.getParcelableArrayList("emailMessages");
        Log.d(TAG,"das"+emailMessages.toString());


        selectRv.setLayoutManager(new LinearLayoutManager(this));
        selectAdapter = new SelectAdapter(this,emailMessages);
        selectRv.setAdapter(selectAdapter);

        bottomNavigationView = findViewById(R.id.select_Bnv);
        bottomNavigationView.setItemIconTintList(null);
        bottomNavigationView.setEnabled(false);
        bottomNavigationView.setOnNavigationItemSelectedListener(onBottomNavigationItemSelectedListener);
    }

    @SuppressLint("ResourceType")
    @Subscribe(threadMode = ThreadMode.POSTING)
    public void bottomNavigationViewCheck(MessageEvent event){
        if(event.getMessage().equals("check")){
            Log.d(TAG,"das"+event.getMessage());
            bottomNavigationView.getMenu().removeGroup(R.menu.select_bottomnav_item);
            bottomNavigationView.inflateMenu(R.menu.select_bottomnav_item_check);
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener onBottomNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId()){
                case R.id.b_inbox:

                    return true;
                case R.id.dialogmail:

                    return true;
                case R.id.contacts:

                    return true;
                case R.id.attachment:

                    return true;
                default:
                    break;

            }
            return false;
        }
    };

}
