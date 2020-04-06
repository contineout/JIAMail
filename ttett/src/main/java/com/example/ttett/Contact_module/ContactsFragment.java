package com.example.ttett.Contact_module;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ttett.Entity.Contact;
import com.example.ttett.Entity.Email;
import com.example.ttett.R;
import com.example.ttett.Service.EmailService;
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


public class ContactsFragment extends Fragment {
    private static final String TAG ="ContactsFragment";
    private View view;
    private Toolbar mToolbar;
    private ContactsDialogFragment contactsDialogFragment;
    private RecyclerView ContactRv;
    private Contact contact;
    private List<Contact> contacts;
    private ContactsAdapter contactsAdapter;
    private Email email;
    private ContactService contactService;
    int email_id;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        EventBus.getDefault().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_contacts,container,false);

        mToolbar = view.findViewById(R.id.contacts_toolbar);
        ContactRv = view.findViewById(R.id.contact_rv);

        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.mipmap.menu);
        actionBar.setDisplayHomeAsUpEnabled(true);

        assert getArguments() != null;
        try{
            email = getArguments().getParcelable("email");
            Log.d(TAG,email.getAddress());
        }catch (NullPointerException e){
        }
        initContacts();

        return view;
    }

    /**
     * 新加入的邮箱进行刷新
     * @param messageEvent
     */
    @Subscribe(threadMode = ThreadMode.POSTING)
    public void NewEmail(MessageEvent messageEvent) {
        if (messageEvent.getMessage().equals("New_Email")) {
            EmailService emailService = new EmailService(getContext());
            email = emailService.queryEmail(messageEvent.getAddress());
            initContacts();
        }
    }

    /**
     * 新加入的邮箱进行刷新
     * @param messageEvent
     */
    @Subscribe(threadMode = ThreadMode.POSTING)
    public void ImportContact(MessageEvent messageEvent) {
        if (messageEvent.getMessage().equals("import_contact")) {
            initContacts();
        }
    }


    /**
     * 切换邮箱
     * @param messageEvent
     */
    @Subscribe(threadMode = ThreadMode.POSTING)
    public void SwitchContact(MessageEvent messageEvent){
        if (messageEvent.getMessage().equals("Switch_Email")){
            email = messageEvent.getEmail();
            initContacts();
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        initContacts();
    }

    public void initContacts(){
        contactService = new ContactService(getContext());
        if(email!=null){
            if(contacts!=null){
                contacts.clear();
                if(contactService.queryAllContact(email.getEmail_id())!=null){
                    contacts.addAll(contactService.queryAllContact(email.getEmail_id()));
                    contactsAdapter.notifyDataSetChanged();
                }
            }else{
                contacts = contactService.queryAllContact(email.getEmail_id());
                if(contacts!=null){
                    ContactRv.setLayoutManager(new LinearLayoutManager(getContext()));
                    contactsAdapter = new ContactsAdapter(getContext(),contacts);
                    ContactRv.setAdapter(contactsAdapter);
                }
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.toobar_contacts_item,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    /**
     * 新增文件对话框
     */
    public void showDialog(){
        contactsDialogFragment = new ContactsDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("email",email);
        contactsDialogFragment.setArguments(bundle);
//        contactsDialogFragment.setTargetFragment(ContactsFragment.this,REQUEST_CODE);
        contactsDialogFragment.show(getFragmentManager(),"contactsDialogFragment");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = null;
        switch (item.getItemId()) {
            case R.id.contacts_find:
                Toast.makeText(getContext(), "你点击了联系人搜索", Toast.LENGTH_SHORT).show();
                break;
            case R.id.contacts_add:
                showDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
