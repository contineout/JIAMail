package com.example.ttett.Folder_module;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.ttett.Entity.Email;
import com.example.ttett.Entity.Folder;
import com.example.ttett.R;
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


public class FolderFragment extends Fragment{
    private static final String TAG ="FolderFragment" ;
    private View view;
    private Toolbar mToolbar;
    private FolderAdapter folderAdapter;
    private FolderDialogFragment folderDialogFragment;
    private RecyclerView FolderRv;
    private List<Folder> folders;
    private Email email;
    private FolderService folderService;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        EventBus.getDefault().register(this);
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_folder,container,false);

        mToolbar = view.findViewById(R.id.folder_toolbar);
        FolderRv = view.findViewById(R.id.folder_rv);

        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.mipmap.menu);
        actionBar.setDisplayHomeAsUpEnabled(true);

        assert getArguments() != null;
        try{
            email = getArguments().getParcelable("email");
        }catch(NullPointerException ignored){

        }
        initFolder();
        return view;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.toobar_folder_item,menu);
    }

    public void initFolder() {
        folderService = new FolderService(getContext());
        if (email != null) {
            if (folders != null) {
                folders.clear();
                folders.addAll(folderService.queryAllFolder(email));
            } else {
                folders = folderService.queryAllFolder(email);
                if (folders != null) {
                    FolderRv.setLayoutManager(new LinearLayoutManager(getContext()));
                    folderAdapter = new FolderAdapter(getContext(), folders,email);
                    FolderRv.setAdapter(folderAdapter);
                }else{
                    folders.clear();
                }
            }
        }else {
            if(folders!=null){
                folders.clear();
            }
        }
        folderAdapter.notifyDataSetChanged();
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void NewFolder(MessageEvent messageEvent){
        if (messageEvent.getMessage().equals("New_folder")||
                messageEvent.getMessage().equals("dismiss") ){
            initFolder();
        }
    }


    @Subscribe(threadMode = ThreadMode.POSTING,sticky = true)
    public void SwitchFolder(MessageEvent messageEvent){
        if (messageEvent.getMessage().equals("Switch_Email")
                ||messageEvent.getMessage().equals("new_Email")
        ){
            email = messageEvent.getEmail();
            folderAdapter.email = email;
            initFolder();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.add_folder)
           showDialog();
        return super.onOptionsItemSelected(item);
    }

    public void showDialog(){
        folderDialogFragment = new FolderDialogFragment();
        folderDialogFragment.show(getFragmentManager(),"folderDialogFragment");
        Bundle bundle = new Bundle();
        bundle.putParcelable("email",email);
        folderDialogFragment.setArguments(bundle);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
