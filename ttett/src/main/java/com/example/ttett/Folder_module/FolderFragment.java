package com.example.ttett.Folder_module;

import android.os.Bundle;
import android.util.Log;
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
        }catch (NullPointerException e){

        }
        Log.d(TAG,"email=" + email.getEmail_id());
        initFolder();
        return view;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.toobar_folder_item,menu);
    }

    public void initFolder(){
        folderService = new FolderService(getContext());
        if(email!=null){
            if (folders!= null){
                folders.clear();
                folders.addAll(folderService.queryAllFolder(email));
                folderAdapter.notifyDataSetChanged();
            }else {
                folders = folderService.queryAllFolder(email);
                if(folders!=null){
                    FolderRv.setLayoutManager(new LinearLayoutManager(getContext()));
                    folderAdapter = new FolderAdapter(getContext(),folders);
                    FolderRv.setAdapter(folderAdapter);
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initFolder();
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void NewFolder(MessageEvent messageEvent){
        if (messageEvent.getMessage().equals("New_folder")){
            initFolder();
        }
    }


    @Subscribe(threadMode = ThreadMode.POSTING)
    public void SwitchFolder(MessageEvent messageEvent){
        if (messageEvent.getMessage().equals("Switch_Email")){
            email = messageEvent.getEmail();
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
