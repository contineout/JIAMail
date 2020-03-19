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

import com.example.ttett.Adapter.FolderAdapter;
import com.example.ttett.CustomDialog.FolderDialogFragment;
import com.example.ttett.Entity.Email;
import com.example.ttett.Entity.Folder;
import com.example.ttett.R;
import com.example.ttett.Service.FolderService;

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
    public static final int REQUEST_CODE = 1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
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
        FolderService folderService = new FolderService(getContext());

        assert getArguments() != null;
        email = getArguments().getParcelable("email");
        folders = folderService.queryAllFolder(email);



//        folderService.SaveFolder();
//        folderService.queryAllFolder()
        FolderRv.setLayoutManager(new LinearLayoutManager(getContext()));
        if(folders!=null){
            folderAdapter = new FolderAdapter(getContext(),folders);
            FolderRv.setAdapter(folderAdapter);
        }

        return view;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.toobar_folder_item,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.add_folder)
           showDialog();
        return super.onOptionsItemSelected(item);
    }

    public void showDialog(){
        folderDialogFragment = new FolderDialogFragment();
        folderDialogFragment.setTargetFragment(FolderFragment.this,REQUEST_CODE);
        folderDialogFragment.show(getFragmentManager(),"folderDialogFragment");
    }

    public void refreshFolders(final List<Folder> folders){
        new Thread(new Runnable() {
            @Override
            public void run() {

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        FolderRv.setLayoutManager(new LinearLayoutManager(getContext()));
                        folderAdapter = new FolderAdapter(getContext(),folders);
                        FolderRv.setAdapter(folderAdapter);
                        folderAdapter.notifyDataSetChanged();
                    }
                });
            }
        }).start();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE){
            String name = data.getStringExtra(FolderDialogFragment.FOLDER_NAME);
            Folder folder = new Folder();
            folder.setFolder_name(name);
            folder.setEmail_id(email.getEmail_id());

            FolderService folderService = new FolderService(getContext());
            Boolean addResult = folderService.SaveFolder(folder);
            if(addResult){
                folders = folderService.queryAllFolder(email);
                refreshFolders(folders);
                folderDialogFragment.dismiss();
            }else{
                Toast.makeText(getContext(),"JiaMail:同名文件夹已经存在",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
