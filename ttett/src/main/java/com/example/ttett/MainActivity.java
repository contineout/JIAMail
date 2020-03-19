package com.example.ttett;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.ttett.Dao.MailDao;
import com.example.ttett.Entity.Email;
import com.example.ttett.Entity.EmailMessage;
import com.example.ttett.fragment.ArchiveFragment;
import com.example.ttett.fragment.AttachmentFragment;
import com.example.ttett.fragment.ContactsFragment;
import com.example.ttett.fragment.DialogMailFragment;
import com.example.ttett.fragment.DraftsFragment;
import com.example.ttett.fragment.FolderFragment;
import com.example.ttett.fragment.InboxFragment;
import com.example.ttett.fragment.SendedFragment;
import com.example.ttett.fragment.SpamFragment;
import com.example.ttett.fragment.TrashFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private CoordinatorLayout coordinatorLayout;
    private NavigationView navigationView;
    private BottomNavigationView bottomNavigationView;


    private InboxFragment inboxFragment;
    private DialogMailFragment dialogMailFragment;
    private ContactsFragment contactsFragment;
    private AttachmentFragment attachmentFragment;
    private SendedFragment sendedFragment;
    private DraftsFragment draftsFragment;
    private TrashFragment trashFragment;
    private SpamFragment spamFragment;
    private ArchiveFragment archiveFragment;
    private FolderFragment folderFragment;
    private Fragment[] fragments;
    private int lastfragmen = 0;
    private List<EmailMessage> emailMessages = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
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

        final Email email = new Email();
        email.setAddress("xl335665873@sina.com");
        email.setAuthorizationCode("d8405717ca1664a2");
        email.setName("xl335665873");
        email.setEmail_id(1);
//
        emailMessages = mailDao.QueryAllMessage(email);
        ArrayList<EmailMessage> ems = (ArrayList<EmailMessage>) emailMessages;
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("emailMessages",ems);
        bundle.putParcelable("email",email);

//        Bundle bundle1 = new Bundle();
//        bundle1.putParcelable("folder_email",email);
//
        inboxFragment.setArguments(bundle);
        folderFragment.setArguments(bundle);

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
        trashFragment = new TrashFragment();
        spamFragment = new SpamFragment();
        archiveFragment = new ArchiveFragment();
        folderFragment = new FolderFragment();

        fragments = new Fragment[]{inboxFragment,dialogMailFragment,contactsFragment,attachmentFragment,//bottomNavigationView 0 - 3
                sendedFragment,draftsFragment,trashFragment,spamFragment,archiveFragment,folderFragment};//NavigationView 4 - 9

        coordinatorLayout = findViewById(R.id.coordinator);

        getSupportFragmentManager().beginTransaction().replace(R.id.coordinator,inboxFragment).show(inboxFragment).commit();

        bottomNavigationView = findViewById(R.id.Bnv);
        bottomNavigationView.setOnNavigationItemSelectedListener(onBottomNavigationItemSelectedListener);

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(onNavigationItemSelectedListener);
    }

    /**
     * NavigationView点击切换frag
     */
    private NavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener
            = new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId()){
                case R.id.inbox:
                    if(lastfragmen != 0){
                        switchFragment(lastfragmen,0);
                        lastfragmen = 0;
                    }
                    drawerLayout.closeDrawers();
                    return true;
                case R.id.sended:
                    if(lastfragmen != 4){
                        switchFragment(lastfragmen,4);
                        lastfragmen = 4;
                    }
                    return true;
                case R.id.draft:
                    if(lastfragmen != 5){
                        switchFragment(lastfragmen,5);
                        lastfragmen = 5;
                    }
                    return true;
                case R.id.trash:
                    if(lastfragmen != 6){
                        switchFragment(lastfragmen,6);
                        lastfragmen = 6;
                    }
                    return true;
                case R.id.spam:
                    if(lastfragmen != 7){
                        switchFragment(lastfragmen,7);
                        lastfragmen = 7;
                    }
                    return true;
//                case R.id.archive:
//                    if(lastfragmen != 8){
//                        switchFragment(lastfragmen,8);
//                        lastfragmen = 8;
//                    }
//                    return true;
                case R.id.folder:
                    if(lastfragmen != 9){
                        switchFragment(lastfragmen,9);
                        lastfragmen = 9;
                    }
                    return true;
                default:
                    break;

            }
            return false;
        }
    };
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
}
