package com.example.ttett.MailDialog_module;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ttett.Contact_module.ContactService;
import com.example.ttett.Entity.Contact;
import com.example.ttett.Entity.Email;
import com.example.ttett.R;
import com.example.ttett.bean.MessageEvent;
import com.example.ttett.util.charsort.SortUtils;
import com.example.ttett.util.sidebar.LetterView;

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


public class DialogMailFragment extends Fragment {
    private View view;
    private Toolbar mToolbar;
    private Email email;
    private List<Contact> contacts;
    private RecyclerView DialogRv;
    private DialogAdapter dialogAdapter;
    private String TAG = "DialogMailFragment";
    private ContactService contactService;
    private LetterView letterView;
    private LinearLayoutManager layoutManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        EventBus.getDefault().register(this);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_dialogmail,container,false);

        mToolbar = view.findViewById(R.id.dialogmail_toolbar);
        DialogRv = view.findViewById(R.id.dialog_rv);
        letterView = view.findViewById(R.id.letter_view);

        letterView.setCharacterListener(new LetterView.CharacterClickListener() {
            @Override
            public void clickCharacter(String character) {
                layoutManager.scrollToPositionWithOffset(dialogAdapter.getScrollPosition(character), 0);
            }

            @Override
            public void clickArrow() {
                layoutManager.scrollToPositionWithOffset(0, 0);
            }
        });



        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.mipmap.menu);
        actionBar.setDisplayHomeAsUpEnabled(true);
        setHasOptionsMenu(true);

        assert getArguments() != null;
        try{
            email = getArguments().getParcelable("email");
        }catch (NullPointerException ignored){
        }
        initDialog();
        return view;
    }

    private void initDialog(){
        if(email!=null){
            if(contacts!=null){
                contacts.clear();
                if(contactService.queryAllEmailContact(email)!=null){
                    contacts.addAll(SortUtils.contactNameSort(contactService.queryAllEmailContact(email)));
                }
                dialogAdapter.notifyDataSetChanged();
            }else{
            contactService = new ContactService(getContext());
            if(contactService.queryAllEmailContact(email)!=null) {
                contacts = SortUtils.contactNameSort(contactService.queryAllEmailContact(email));
                layoutManager = new LinearLayoutManager(getContext());
                DialogRv.setLayoutManager(layoutManager);
                dialogAdapter = new DialogAdapter(getContext(), contacts);
                DialogRv.setAdapter(dialogAdapter);
            }else{
                if(contacts!=null){
                    contacts.clear();
                }
            }
            }
        }else {
            contacts.clear();
            dialogAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 接送更改inbox
     * @param messageEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void SwitchMessage(MessageEvent messageEvent){
        if (messageEvent.getMessage().equals("Switch_Email")){
            email = messageEvent.getEmail();
            initDialog();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
