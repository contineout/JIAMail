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

import com.example.ttett.CustomDialog.ContactsDialogFragment;
import com.example.ttett.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;


public class ContactsFragment extends Fragment {
    private static final String TAG ="ContactsFragment";
    private View view;
    private Toolbar mToolbar;
    private ContactsDialogFragment contactsDialogFragment;

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

        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.mipmap.menu);
        actionBar.setDisplayHomeAsUpEnabled(true);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.toobar_contacts_item,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public void showDialog(){
        contactsDialogFragment = new ContactsDialogFragment();
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
