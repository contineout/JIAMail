package com.example.ttett.CustomDialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ttett.Entity.Email;
import com.example.ttett.Entity.Folder;
import com.example.ttett.R;
import com.example.ttett.Service.FolderService;
import com.example.ttett.bean.MessageEvent;

import org.greenrobot.eventbus.EventBus;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class FolderDialogFragment extends DialogFragment implements View.OnClickListener{
    private TextView TvConfirm,TvCancel;
    private EditText EtName;
//    private Folder folder;
    private Email email;
    private FolderService folderService;
    private String folder_name;
    private String TAG = "FolderDialogFragment";



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return super.onCreateView(inflater,container,savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.add_folder_dialog,null);
        TvConfirm = view.findViewById(R.id.add_folder_dialog_confirm);
        TvCancel = view.findViewById(R.id.add_folder_dialog_cancel);
        EtName = view.findViewById(R.id.add_folder_dialog_name);
        try{
            email = getArguments().getParcelable("email");
        }catch (Exception e){
        }
        Log.d(TAG,"email=" + email.getEmail_id());


        TvConfirm.setOnClickListener(this);
        TvCancel.setOnClickListener(this);
        builder.setView(view);
        return builder.create();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_folder_dialog_confirm:
                folder_name = EtName.getText().toString();
                addFolder(folder_name);
                break;
                case R.id.add_folder_dialog_cancel:
                    dismiss();
                    break;
                    default:
         }
    }


    private void addFolder(String name) {
        folderService = new FolderService(getContext());
        if(name.isEmpty()){
            Toast.makeText(getContext(), "JiaMail:请输入文件名", Toast.LENGTH_SHORT).show();
        }else {
            Folder folder = new Folder();
            Log.d(TAG,"email=" + name);
            folder.setFolder_name(name);
            folder.setEmail_id(email.getEmail_id());
            Boolean addResult = folderService.SaveFolder(folder);
            if (addResult) {
                EventBus.getDefault().post(new MessageEvent("add_folder",email));
                dismiss();
            } else {
                Toast.makeText(getContext(), "JiaMail:同名文件夹已经存在", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
