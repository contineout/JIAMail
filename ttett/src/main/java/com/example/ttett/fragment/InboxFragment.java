package com.example.ttett.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ttett.Adapter.InboxAdapter;
import com.example.ttett.Adapter.TopMenuAdapter;
import com.example.ttett.Entity.Email;
import com.example.ttett.Entity.EmailMessage;
import com.example.ttett.R;
import com.example.ttett.Service.EmailService;
import com.example.ttett.Service.MailService;
import com.example.ttett.WriteLetterActivity;
import com.example.ttett.bean.MessageEvent;
import com.example.ttett.bean.Topmenu;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


public class InboxFragment extends Fragment {
    private static final String TAG = "InboxFragment";
    private View view;
    private Toolbar mToolbar;
    private ImageView IvInbox;
    private TextView ToolbarTitle;
    private List<Topmenu> Topmenus = new ArrayList<>();
    private FloatingActionButton fab;
    private RecyclerView InboxRv;
    private SwipeRefreshLayout swipeRefreshLayout;
    private InboxAdapter inboxAdapter;
    private List<EmailMessage> emailMessages;
    private Email email;
    private int UPDATE_MESSAGES = 1;
    private MailService mailService;
    private EmailService emailService;
    private boolean folderflag  = false;
    @SuppressLint("HandlerLeak")
    private
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == UPDATE_MESSAGES){
                initEmailMessage();
            }
            swipeRefreshLayout.setRefreshing(false);
        }
    };


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        EventBus.getDefault().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_inbox,container,false);
        mToolbar = view.findViewById(R.id.inbox_toolbar);
        IvInbox = view.findViewById(R.id.Iv_inbox);
        ToolbarTitle = view.findViewById(R.id.inbox_ToolbarTitle);
        swipeRefreshLayout = view.findViewById(R.id.sr_inbox);

        assert getArguments() != null;
        try{
            email = getArguments().getParcelable("email");
        }catch (NullPointerException ignored){
        }
         if(email!=null){
             swipeRefreshLayout.post(new Runnable() {
                 @Override
                 public void run() {
                     refreshMessage(email);
                     swipeRefreshLayout.setRefreshing(true);
                 }
             });

             if(swipeRefreshLayout.isRefreshing()){
                 swipeRefreshLayout.setRefreshing(false);
             }
         }

        initEmailMessage();

        /**
         * swipe刷新inbox
         */
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshMessage(email);
            }
        });

        //收件actionbar
        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.mipmap.menu);
        actionBar.setDisplayHomeAsUpEnabled(true);

        //topmenu点击事件
        initMenu();
        IvInbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopuopwindow();
            }
        });

        //写信fab点击事件
        fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), WriteLetterActivity.class);
                intent.putExtra("email",email);
                startActivity(intent);
            }
        });
        return view;
    }

    /**
     * 接送更改inbox
     * @param messageEvent
     */
    @Subscribe(threadMode = ThreadMode.POSTING)
    public void SwitchMessage(MessageEvent messageEvent){
        if (messageEvent.getMessage().equals("Switch_Email")){
            email = messageEvent.getEmail();
            initEmailMessage();
        }
    }

    /**
     * 新加入的邮箱进行刷新
     * @param messageEvent
     */
    @Subscribe(threadMode = ThreadMode.POSTING)
    public void NewEmail(MessageEvent messageEvent) {
        if (messageEvent.getMessage().equals("New_Email")) {
            emailService = new EmailService(getContext());
            email = emailService.queryEmail(messageEvent.getAddress());
            refreshMessage(email);
        }
    }

    /**
     * 更改inbox
     * @param messageEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void updateMessage(MessageEvent messageEvent){
        if (messageEvent.getMessage().equals("update_message")){
            initEmailMessage();
        }
    }



    /**
     * 初始化EmailMessag
     */
    public void initEmailMessage(){
        mailService = new MailService(getContext());
        if(email!=null){
            if(emailMessages!=null){
                emailMessages.clear();
                if(mailService.queryAllMessage(email)!=null){
                    emailMessages.addAll(mailService.queryAllMessage(email));
                    inboxAdapter.notifyDataSetChanged();
                }
            }else{
                emailMessages = mailService.queryAllMessage(email);
                if(emailMessages!=null){
                    InboxRv = view.findViewById(R.id.inbox_rv);
                    InboxRv.setLayoutManager(new LinearLayoutManager(getContext()));
                    InboxRv.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));

                    inboxAdapter = new InboxAdapter(getContext(),emailMessages,InboxAdapter.inboxFragment,email);
                    InboxRv.setAdapter(inboxAdapter);

                    DefaultItemAnimator defaultItemAnimator = new DefaultItemAnimator();
                    defaultItemAnimator.setAddDuration(500);
                    defaultItemAnimator.setRemoveDuration(500);
                    InboxRv.setItemAnimator(defaultItemAnimator);
                }
            }
        }
    }

    /**
     * 同步刷新email
     * @param email
     */
    public void refreshMessage(final Email email){
        new Thread(new Runnable() {
            @Override
            public void run() {
                mailService.SynchronizeMessage(email);
                Message msg = new Message();
                msg.what = UPDATE_MESSAGES;
                handler.sendMessage(msg);

            }
        }).start();
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.toobar_inbox_item,menu);
//        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case  R.id.inbox_find:
                Toast.makeText(getContext(),"你点击了搜索",Toast.LENGTH_SHORT).show();
                break;
            default:
        }
        return true;
    }

    /**
     * topmenu_item点击事件
     */

    private void showPopuopwindow() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.inbox_top_menu_rv,null,false);
        RecyclerView recyclerView = view.findViewById(R.id.inbox_top_rv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        TopMenuAdapter adapter = new TopMenuAdapter(getActivity(), Topmenus, new TopMenuAdapter.OnClickItemListener() {
            @Override
            public void onClick(int position) {
                switch (position){
                    case 0:
                        topMenuSwitch(mailService.queryAllMessage(email));
                        break;
                    case 1:
                        topMenuSwitch(mailService.queryUnReadMessage(email));
                        ToolbarTitle.setText("未读邮件");
                        break;
                    case 2:
                        topMenuSwitch(mailService.queryStarMessage(email));
                        ToolbarTitle.setText("星标邮件");
                        break;
                        default:
                            break;
                }
            }
        });

        recyclerView.setAdapter(adapter);
        PopupWindow popupWindow = new PopupWindow(view,ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setContentView(view);
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.showAsDropDown(view,500,-100);
    }

    /**
     * topMenu方法
     * @param newMessages
     */

    public void topMenuSwitch(List<EmailMessage> newMessages){
        if(emailMessages!=null){
            emailMessages.clear();
            if(newMessages!=null){
                emailMessages.addAll(newMessages);
            }
            inboxAdapter.notifyDataSetChanged();
        }
    }

    /**
     * topmenu——item
     */

    private void initMenu() {
            Topmenu inbox = new Topmenu("收件箱",R.mipmap.nav_inbox);
            Topmenus.add(inbox);
            Topmenu unread = new Topmenu("未读邮件",R.mipmap.unread_mail);
            Topmenus.add(unread);
            Topmenu star = new Topmenu("星标邮件",R.mipmap.star_mail);
            Topmenus.add(star);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


}
