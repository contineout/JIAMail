package com.example.ttett.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ttett.Adapter.ContactsAdapter;
import com.example.ttett.CustomDialog.ContactsDialogFragment;
import com.example.ttett.Entity.Contact;
import com.example.ttett.Entity.Email;
import com.example.ttett.R;
import com.example.ttett.Service.ContactService;

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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
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
        email = getArguments().getParcelable("email");

        contactService = new ContactService(getContext());
        contacts = contactService.queryAllContact(email.getUser_id());

        ContactRv.setLayoutManager(new LinearLayoutManager(getContext()));
        if(contacts !=null){
            contactsAdapter = new ContactsAdapter(getContext(),contacts);
            ContactRv.setAdapter(contactsAdapter);
            contactsAdapter.setContact(contacts);
        }

        return view;
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
        bundle.putInt("user_id",email.getUser_id());
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


}
