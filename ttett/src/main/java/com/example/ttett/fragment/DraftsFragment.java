package com.example.ttett.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ttett.Adapter.DraftsAdapter;
import com.example.ttett.Entity.Email;
import com.example.ttett.Entity.EmailMessage;
import com.example.ttett.R;
import com.example.ttett.Service.MailService;
import com.example.ttett.bean.MessageEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class DraftsFragment extends Fragment {
    private View view;
    private Toolbar mToolbar;
    private Email email;
    private MailService mailService;
    private List<EmailMessage> emailMessages;
    private RecyclerView DraftsRv;
    private DraftsAdapter draftsAdapter;
    private String TAG = "DraftsFragment";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        EventBus.getDefault().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_drafts,container,false);
        mToolbar = view.findViewById(R.id.draft_toolbar);
        DraftsRv = view.findViewById(R.id.draft_rv);

        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.mipmap.menu);
        actionBar.setDisplayHomeAsUpEnabled(true);

        assert getArguments() != null;
        try{
            email = getArguments().getParcelable("email");
        }catch (NullPointerException ignored){
        }
        if(email!=null){
            initEmailMessage();
        }
        return view;
    }

    /**
     * 初始化DraftsEmailMessag
     */
    public void initEmailMessage(){
        mailService = new MailService(getContext());
        if(email!=null){
            if(emailMessages!=null){
                emailMessages.clear();
                if(mailService.queryDraftsMessage(email)!=null){
                    emailMessages.addAll(mailService.queryDraftsMessage(email));
                }
                draftsAdapter.notifyDataSetChanged();
            }else{
                emailMessages = mailService.queryDraftsMessage(email);
                if(emailMessages!=null){
                    DraftsRv.setLayoutManager(new LinearLayoutManager(getContext()));
                    draftsAdapter = new DraftsAdapter(getContext(),emailMessages,email);
                    DraftsRv.setAdapter(draftsAdapter);
                }
            }
        }else {
            if(emailMessages!=null){
                emailMessages.clear();
                draftsAdapter.notifyDataSetChanged();
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void SendResult(MessageEvent messageEvent){
            if (messageEvent.getMessage().equals("SendSuccess")||
                    messageEvent.getMessage().equals("SaveSuccess")) {
                initEmailMessage();
            }
    }

    /**
     * 接送更改DraftsMessage
     * @param messageEvent
     */
    @Subscribe(threadMode = ThreadMode.POSTING,sticky = true)
    public void SwitchMessage(MessageEvent messageEvent){
        if (messageEvent.getMessage().equals("Switch_Email")
        ||messageEvent.getMessage().equals("new_Email")){
            email = messageEvent.getEmail();
            initEmailMessage();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.toobar_drafts_item,menu);
//        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.drafts_trash){
                Toast.makeText(getContext(),"你点击了草稿箱删除",Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
