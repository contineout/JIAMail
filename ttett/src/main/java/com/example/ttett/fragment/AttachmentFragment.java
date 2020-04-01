package com.example.ttett.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ttett.Adapter.AttachmentAdapter;
import com.example.ttett.Entity.Attachment;
import com.example.ttett.Entity.Email;
import com.example.ttett.R;
import com.example.ttett.Service.AttachmentService;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class AttachmentFragment extends Fragment {
    private static final String TAG ="AttachmentFragment" ;
    private View view;
    private Toolbar mToolbar;
    private AttachmentAdapter attachmentAdapter;
    private RecyclerView AttachmentRv;
    private AttachmentService attachmentService;
    private Email email;
    private List<Attachment> attachments;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_attachment,container,false);

        AttachmentRv = view.findViewById(R.id.attachment_rv);
        mToolbar = view.findViewById(R.id.attachment_toolbar);

        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.mipmap.menu);
        actionBar.setDisplayHomeAsUpEnabled(true);

        assert getArguments() != null;
        try{
            email = getArguments().getParcelable("email");
        }catch (NullPointerException ignored){
        }
        Log.d(TAG,"email=" + email.getEmail_id());
        initAttach();

        return view;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.toobar_attachment_item,menu);
//        super.onCreateOptionsMenu(menu, inflater);
    }

    public void initAttach(){
        attachmentService = new AttachmentService(getContext());
        if(email!=null){
            if (attachments!= null){
                attachments.clear();
                attachments.addAll(attachmentService.queryAllAttachment(email.getEmail_id()));
                attachmentAdapter.notifyDataSetChanged();
            }else {
                attachments = attachmentService.queryAllAttachment(email.getEmail_id());
                if(attachments!=null){
                    AttachmentRv.setLayoutManager(new LinearLayoutManager(getContext()));
                    attachmentAdapter = new AttachmentAdapter(getContext(),attachments);
                    AttachmentRv.setAdapter(attachmentAdapter);
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.attachment_find)
            Toast.makeText(getContext(),"你点击了附件搜索",Toast.LENGTH_SHORT).show();
        return super.onOptionsItemSelected(item);
    }
}
