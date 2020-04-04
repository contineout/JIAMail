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


public class DeletedFragment extends Fragment {
    private View view;
    private Toolbar mToolbar;
    private Email email;
    private MailService mailService;
    private List<EmailMessage> emailMessages;
    private RecyclerView DeletedRv;
    private InboxAdapter inboxAdapter;
    private String TAG = "SendedFragment";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public void onStart() {
        super.onStart();
        initEmailMessage();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_trash,container,false);
        mToolbar = view.findViewById(R.id.trash_toolbar);
        DeletedRv = view.findViewById(R.id.trash_rv);

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

    public void initEmailMessage(){
        mailService = new MailService(getContext());
        if(email!=null){
            if(emailMessages!=null){
                if(mailService.queryDeleteMessage(email)!=null){
                    emailMessages.clear();
                    emailMessages.addAll(mailService.queryDeleteMessage(email));
                    inboxAdapter.notifyDataSetChanged();
                }else{
                    emailMessages.clear();
                    inboxAdapter.notifyDataSetChanged();
                }
            }else{
                emailMessages = mailService.queryDeleteMessage(email);
                if(emailMessages!=null){
                    DeletedRv.setLayoutManager(new LinearLayoutManager(getContext()));
                    inboxAdapter = new InboxAdapter(getContext(),emailMessages,InboxAdapter.deleteFragment,email);
                    DeletedRv.setAdapter(inboxAdapter);
                }
            }
        }
    }

    /**
     * 接送更改Deleted
     * @param messageEvent
     */
    @Subscribe(threadMode = ThreadMode.POSTING)
    public void SwitchMessage(MessageEvent messageEvent){
        if (messageEvent.getMessage().equals("Switch_Email")){
            email = messageEvent.getEmail();
            mailService = new MailService(getContext());
            initEmailMessage();
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.toobar_trash_item,menu);
//        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.trash_find)
            Toast.makeText(getContext(),"你点击了已删除搜索",Toast.LENGTH_SHORT).show();
        return super.onOptionsItemSelected(item);
    }
}
