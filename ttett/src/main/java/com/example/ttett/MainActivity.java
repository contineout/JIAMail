package com.example.ttett;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.ttett.Adapter.EmailAdapter;
import com.example.ttett.Contact_module.ContactsDialogFragment;
import com.example.ttett.Contact_module.ContactsFragment;
import com.example.ttett.Dao.MailDao;
import com.example.ttett.Entity.Email;
import com.example.ttett.Folder_module.FolderFragment;
import com.example.ttett.MailDialog_module.DialogMailFragment;
import com.example.ttett.Service.EmailService;
import com.example.ttett.bean.MessageEvent;
import com.example.ttett.fragment.AttachmentFragment;
import com.example.ttett.fragment.DeletedFragment;
import com.example.ttett.fragment.DraftsFragment;
import com.example.ttett.fragment.InboxFragment;
import com.example.ttett.fragment.SendedFragment;
import com.example.ttett.fragment.SpamFragment;
import com.example.ttett.util.ToastUtil;
import com.example.ttett.util.mailUtil.SaveMessage;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private DrawerLayout drawerLayout;
    private FrameLayout coordinatorLayout;
    private BottomNavigationView bottomNavigationView;
    private String TAG = "MainActivity.this";

    private ContactsDialogFragment contactsDialogFragment = new ContactsDialogFragment();
    private InboxFragment inboxFragment;
    private DialogMailFragment dialogMailFragment;
    private ContactsFragment contactsFragment;
    private AttachmentFragment attachmentFragment;
    private SendedFragment sendedFragment;
    private DraftsFragment draftsFragment;
    private DeletedFragment trashFragment;
    private SpamFragment spamFragment;
    private FolderFragment folderFragment;
    private Fragment[] fragments;
    private int lastfragmen = 0;
    private RecyclerView Email_Rv;
    private List<Email> emails;
    private EmailService emailService = new EmailService(this);
    private EmailAdapter emailAdapter;
    private int user_id;
    private TextView tv_address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        verifyStoragePermissions(this);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);
        initView();

        //Tooldar标题栏
        Toolbar mToolber = findViewById(R.id.inbox_toolbar);
        setSupportActionBar(mToolber);

        //drawerlayout侧工具栏
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.mipmap.menu);
        }
/**
 * 若没有MAIL.db则创建
 */
        final MailDao mailDao = new MailDao(this);
        mailDao.CreateMessageTable();
//        user_id = getIntent().getIntExtra("user_id",0);

        try{
            user_id = 1;
        }catch (Exception e){
        }

        initEmail();
        if(emails!=null){
            Email email = emails.get(0);
            initPara(email);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        drawerLayout.closeDrawers();
    }

    public void initEmail(){
        if(emails!=null){
            emails.clear();
            emails.addAll(emailService.queryAllEmail(user_id));
            emailAdapter.notifyDataSetChanged();
        }else {
            emails = emailService.queryAllEmail(user_id);
            if(emails!=null){
                LinearLayoutManager layoutManager = new LinearLayoutManager(this);
                layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                Email_Rv.setLayoutManager(layoutManager);
                emailAdapter = new EmailAdapter(this,emails);
                Email_Rv.setAdapter(emailAdapter);
            }
        }
    }

    public void initPara(Email email){
        Bundle bundle = new Bundle();
        bundle.putParcelable("email",email);
        inboxFragment.setArguments(bundle);
        folderFragment.setArguments(bundle);
        sendedFragment.setArguments(bundle);
        draftsFragment.setArguments(bundle);
        contactsFragment.setArguments(bundle);
        trashFragment.setArguments(bundle);
        attachmentFragment.setArguments(bundle);
        dialogMailFragment.setArguments(bundle);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1 && resultCode == LoginEmailActivity.RESULT_CODE) {
            Bundle bundle = data.getExtras();
            boolean strResult = bundle.getBoolean("result");
            final String address = bundle.getString("address");

            if(strResult){
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                initEmail();
                                if(!address.isEmpty()){
                                    final Email email = emailService.queryEmail(address);
                                    EventBus.getDefault().post(new MessageEvent("New_Email",address));
                                    initPara(email);
                                }
                            }
                        });
                    }
                }).start();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
 * 点击Toolbar触发事件
 */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Fragment初始化
     */
    private void initView(){
        //NavigationView
        inboxFragment = new InboxFragment();
        dialogMailFragment = new DialogMailFragment();
        contactsFragment = new ContactsFragment();
        attachmentFragment = new AttachmentFragment();

        //bottomNavigationView
        sendedFragment = new SendedFragment();
        draftsFragment = new DraftsFragment();
        trashFragment = new DeletedFragment();
        spamFragment = new SpamFragment();
        folderFragment = new FolderFragment();

        drawerLayout = findViewById(R.id.drawer_layout);

        ImageView add_email = findViewById(R.id.add_email);
        ImageView user_info = findViewById(R.id.user_info);
        RelativeLayout nav_inbox = findViewById(R.id.nav_inbox);
        RelativeLayout nav_sended = findViewById(R.id.nav_sended);
        RelativeLayout nav_drafts = findViewById(R.id.nav_drafts);
        RelativeLayout nav_delete = findViewById(R.id.nav_delete);
        RelativeLayout nav_spam = findViewById(R.id.nav_spam);
        RelativeLayout nav_folder = findViewById(R.id.nav_folder);

        tv_address = findViewById(R.id.email_address);
        Email_Rv = findViewById(R.id.email_rv);

        fragments = new Fragment[]{inboxFragment,dialogMailFragment,contactsFragment,attachmentFragment,//bottomNavigationView 0 - 3
                sendedFragment,draftsFragment,trashFragment,spamFragment,folderFragment};//NavigationView 4 - 8

        coordinatorLayout = findViewById(R.id.coordinator);

        getSupportFragmentManager().beginTransaction().replace(R.id.coordinator,inboxFragment).show(inboxFragment).commit();

        bottomNavigationView = findViewById(R.id.Bnv);
        bottomNavigationView.setOnNavigationItemSelectedListener(onBottomNavigationItemSelectedListener);

        nav_inbox.setOnClickListener(this);
        nav_sended.setOnClickListener(this);
        nav_drafts.setOnClickListener(this);
        nav_delete.setOnClickListener(this);
        nav_spam.setOnClickListener(this);
        nav_folder.setOnClickListener(this);
        user_info.setOnClickListener(this);
        add_email.setOnClickListener(this);
        //nav_inbox nav_sended nav_drafts nav_delete nav_spam nav_folder user_info add_email email_address
    }


    /**
     * bottomNavigationViewd点击切换frag
     */
    private BottomNavigationView.OnNavigationItemSelectedListener onBottomNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId()){
                case R.id.b_inbox:
                    if(lastfragmen != 0){
                        switchFragment(lastfragmen,0);
                        lastfragmen = 0;
                        drawerLayout.closeDrawers();
                    }
                    return true;
                case R.id.dialogmail:
                    if(lastfragmen != 1){
                        switchFragment(lastfragmen,1);
                        lastfragmen = 1;
                    }
                    return true;
                case R.id.contacts:
                    if(lastfragmen != 2){
                        switchFragment(lastfragmen,2);
                        lastfragmen = 2;
                    }
                    return true;
                case R.id.attachment:
                    if(lastfragmen != 3){
                        switchFragment(lastfragmen,3);
                        lastfragmen = 3;
                    }
                    return true;
                    default:
                        drawerLayout.closeDrawers();
                        break;

            }
            return false;
        }
    };


    @Subscribe(threadMode = ThreadMode.POSTING)
    public void SwitchEmail(MessageEvent messageEvent){
        if (messageEvent.getMessage().equals("Switch_Email")){
            if(messageEvent.getEmail()!=null){
                tv_address.setText(messageEvent.getEmail().getAddress());
                initEmail();
            }else{
                tv_address.setText("请添加邮箱");
                emails.clear();
                emailAdapter.notifyDataSetChanged();
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.POSTING,sticky = true)
    public void SendResult(MessageEvent messageEvent){
        if (messageEvent.getMessage().equals("SendSuccess")){
            try {
                int id = messageEvent.getEmailMessage().getId();
                MailDao dao = new MailDao(this);
                dao.updateSend(id);
            }catch (NullPointerException e){
                SaveMessage saveMessage = new SaveMessage(messageEvent.getEmailMessage(),this,messageEvent.getEmail());
                saveMessage.saveSendMessage();
                Looper.prepare();
                ToastUtil.showTextToas(this,"发送成功");
                Looper.loop();
            }
        }
        if (messageEvent.getMessage().equals("SendError")){
            try {
                int id = messageEvent.getEmailMessage().getId();
                Log.d(TAG,id+"ddd");
                Looper.prepare();
                ToastUtil.showTextToas(this,"发送失败");
                Looper.loop();
            }catch (NullPointerException e){
                SaveMessage saveMessage = new SaveMessage(messageEvent.getEmailMessage(),this,messageEvent.getEmail());
                saveMessage.saveDraftsMessage();
                Looper.prepare();
                ToastUtil.showTextToas(this,"发送失败");
                Looper.loop();
            }
        }
    }
    /**
     * 切换fragment
     * @param lastfragmen
     * @param index
     */
    private void switchFragment(int lastfragmen,int index) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.hide(fragments[lastfragmen]);
        if (!fragments[index].isAdded()) {
            transaction.add(R.id.coordinator, fragments[index]);
        }
        transaction.show(fragments[index]).commitAllowingStateLoss();
        drawerLayout.closeDrawers();
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()){
            case R.id.add_email:
                intent = new Intent(MainActivity.this,LoginEmailActivity.class);
                intent.putExtra("user_id",user_id);
                startActivityForResult(intent,1);
                break;
            case R.id.user_info:
                intent = new Intent(MainActivity.this,UserinfoActivity.class);
                intent.putExtra("user_id",user_id);
                drawerLayout.closeDrawers();
                startActivity(intent);
                break;
            case R.id.nav_inbox:
                if(lastfragmen != 0){
                    switchFragment(lastfragmen,0);
                    lastfragmen = 0;
                }
                break;
            case R.id.nav_sended:
                if(lastfragmen != 4){
                    switchFragment(lastfragmen,4);
                    lastfragmen = 4;
                }
                break;
            case R.id.nav_drafts:
                if(lastfragmen != 5){
                    switchFragment(lastfragmen,5);
                    lastfragmen = 5;
                }
                break;
            case R.id.nav_delete:
                if(lastfragmen != 6){
                    switchFragment(lastfragmen,6);
                    lastfragmen =6;
                }
                break;
            case R.id.nav_spam:
                if(lastfragmen != 7){
                    switchFragment(lastfragmen,7);
                    lastfragmen = 7;
                }
                break;
            case R.id.nav_folder:
                if(lastfragmen != 8){
                    switchFragment(lastfragmen,8);
                    lastfragmen = 8;
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private  final int REQUEST_EXTERNAL_STORAGE = 1;
    private  String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE };
    public  void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        }
    }
}
