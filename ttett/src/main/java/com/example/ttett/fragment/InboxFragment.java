package com.example.ttett.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.example.ttett.util.RecipientMessage;
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

import static androidx.constraintlayout.widget.Constraints.TAG;


public class InboxFragment extends Fragment {
    private View view;
    private Toolbar mToolbar;
    private ImageView IvInbox;
    private TextView ToolbarTitle;
    private List<Topmenu> Topmenus = new ArrayList<>();
    private FloatingActionButton fab;
    private RecyclerView InboxRv;
    private List<EmailMessage> MessageList;
    private SwipeRefreshLayout swipeRefreshLayout;
    private InboxAdapter inboxAdapter;
    private Email email;
    private List<EmailMessage> emailMessages;




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

        final RecipientMessage receiptMessage = new RecipientMessage();
        final MailService mailService = new MailService(getContext());

        assert getArguments() != null;
        email = getArguments().getParcelable("email");
        emailMessages = getArguments().getParcelableArrayList("emailMessages");
        Log.d(TAG,"email"+email.getAddress() + email.getName()+email.getAuthorizationCode());
        for (EmailMessage Message: emailMessages){
            Log.d(TAG,"emailMessages"+Message.getSubject());
            Log.d(TAG,"emailMessages"+Message.getTo());
        }
        InboxRv = view.findViewById(R.id.inbox_rv);
        InboxRv.setLayoutManager(new LinearLayoutManager(getContext()));
        inboxAdapter = new InboxAdapter(getContext(),emailMessages);
        InboxRv.setAdapter(inboxAdapter);

        //初始化收件frag

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
////                    initMail(user);
//                    Thread.sleep(2000);
//
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {

//                    }
//                });
//
//            }
//        }).start();

        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                refreshMails(user);
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

//    private void refreshMails(final User user){
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Thread.sleep(2000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
////                        initMail(user);
//                        inboxAdapter.notifyDataSetChanged();
//                        swipeRefreshLayout.setRefreshing(false);
//
//                    }
//                });
//            }
//        }).start();
//    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.toobar_inbox_item,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Log.d("INonCe","INonCe");
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

}
