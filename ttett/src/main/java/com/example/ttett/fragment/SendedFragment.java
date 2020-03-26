package com.example.ttett.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ttett.Adapter.InboxAdapter;
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


public class SendedFragment extends Fragment {
    private View view;
    private Toolbar mToolbar;
    private Email email;
    private MailService mailService;
    private List<EmailMessage> emailMessages;
    private RecyclerView SendedRv;
    private InboxAdapter inboxAdapter;
    private String TAG = "SendedFragment";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_sended,container,false);
        mToolbar = view.findViewById(R.id.sended_toolbar);
        SendedRv = view.findViewById(R.id.sended_rv);

        assert getArguments() != null;
        try{
            email = getArguments().getParcelable("email");
        }catch (NullPointerException ignored){
        }
        if(email!=null){
            initEmailMessage();
        }

        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.mipmap.menu);
        actionBar.setDisplayHomeAsUpEnabled(true);

        return view;
    }

    /**
     * 初始化EmailMessag
     */
    public void initEmailMessage(){
        mailService = new MailService(getContext());
        emailMessages = mailService.querySendedMessage(email);
        if(emailMessages!=null){
            SendedRv.setLayoutManager(new LinearLayoutManager(getContext()));
            inboxAdapter = new InboxAdapter(getContext(),emailMessages);
            SendedRv.setAdapter(inboxAdapter);
        }
    }

    /**
     * 接送更改Sended
     * @param messageEvent
     */
    @Subscribe(threadMode = ThreadMode.POSTING)
    public void SwitchMessage(MessageEvent messageEvent){
        if (messageEvent.getMessage().equals("Switch_Email")){
            email = messageEvent.getEmail();
            mailService = new MailService(getContext());
            if(email!=null){
                if(emailMessages!=null){
                    emailMessages.clear();
                    emailMessages.addAll(mailService.querySendedMessage(email));
                    inboxAdapter.notifyDataSetChanged();
                }else{
                    emailMessages = mailService.querySendedMessage(email);
                    initEmailMessage();
                }
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.toobar_sended_item,menu);
//        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.sended_find)
            Toast.makeText(getContext(),"你点击了已发送搜索",Toast.LENGTH_SHORT).show();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
