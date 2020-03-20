package com.example.ttett.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
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
import com.example.ttett.Service.MailService;
import com.example.ttett.bean.Topmenu;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


public class InboxFragment extends Fragment {
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

    @SuppressLint("HandlerLeak")
    private
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            emailMessages = data.getParcelableArrayList("emailMessages");
            inboxAdapter = new InboxAdapter(getContext(),emailMessages);
            InboxRv.setAdapter(inboxAdapter);
            inboxAdapter.notifyDataSetChanged();
            swipeRefreshLayout.setRefreshing(false);
        }
    };


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
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
        email = getArguments().getParcelable("email");
        emailMessages = getArguments().getParcelableArrayList("emailMessages");

        if(emailMessages!=null){
            InboxRv = view.findViewById(R.id.inbox_rv);
            InboxRv.setLayoutManager(new LinearLayoutManager(getContext()));
            inboxAdapter = new InboxAdapter(getContext(),emailMessages);
            InboxRv.setAdapter(inboxAdapter);
        }



        //初始化收件frag


        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        final Email finalEmail = email;
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshMessage(finalEmail);
            }
        });

        //收件actionbar
        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.mipmap.menu);
        actionBar.setDisplayHomeAsUpEnabled(true);

        initMenu();
        //topmenu点击事件
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
                Intent intent = new Intent(getContext(),WriteLetterActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

    public void refreshMessage(final Email email){
        new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<EmailMessage> emailMessages = (ArrayList<EmailMessage>) initMessage(email);
                Message msg = new Message();
                Bundle data = new Bundle();
                data.putParcelableArrayList("emailMessages",emailMessages);
                msg.setData(data);
                handler.sendMessage(msg);
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


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

    //topmenu_item点击事件
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
                        Toast.makeText(getContext(),"0",Toast.LENGTH_SHORT).show();
                        ToolbarTitle.setText("收件箱");
                        break;
                    case 1:
                        Toast.makeText(getContext(),"1",Toast.LENGTH_SHORT).show();
                        ToolbarTitle.setText("未读邮件");
                        break;
                    case 2:
                        Toast.makeText(getContext(),"2",Toast.LENGTH_SHORT).show();
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
    //topmenu——item
    private void initMenu() {
            Topmenu inbox = new Topmenu("收件箱",R.mipmap.nav_inbox);
            Topmenus.add(inbox);
            Topmenu unread = new Topmenu("未读邮件",R.mipmap.unread_mail);
            Topmenus.add(unread);
            Topmenu star = new Topmenu("星标邮件",R.mipmap.star_mail);
            Topmenus.add(star);
    }

    private List<EmailMessage> initMessage(Email email){
        Log.d("INonCe","INonCe"+email.getAuthorizationCode()+email.getAddress());
        MailService mailService = new MailService(getContext());
        return mailService.SynchronizeMessage(email);
    }

}
