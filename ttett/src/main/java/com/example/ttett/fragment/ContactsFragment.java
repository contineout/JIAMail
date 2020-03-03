package com.example.ttett.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ttett.ContactsActivity;
import com.example.ttett.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;


public class ContactsFragment extends Fragment implements View.OnClickListener {
    private View view;
    private Toolbar mToolbar;

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = null;
        switch (item.getItemId()) {
            case R.id.contacts_find:
                Toast.makeText(getContext(), "你点击了联系人搜索", Toast.LENGTH_SHORT).show();
                break;
            case R.id.contacts_add:
                showPopupWindow();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showPopupWindow(){
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.contacts_popup,null);
        PopupWindow mPopup = new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        TextView Tv_import = contentView.findViewById(R.id.tv_import);
        TextView Tv_new = contentView.findViewById(R.id.tv_new);
        View rootview = LayoutInflater.from(getContext()).inflate(R.layout.frag_contacts,null);
        mPopup.showAtLocation(rootview, Gravity.BOTTOM,-50,0);

        Tv_import.setOnClickListener(this);
        Tv_new.setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()){
            case R.id.tv_import:
                Toast.makeText(getContext(), "你点击了联系人搜索", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_new:
                intent = new Intent(getContext(),ContactsActivity.class);
                startActivity(intent);
                break;
        }
    }
}
