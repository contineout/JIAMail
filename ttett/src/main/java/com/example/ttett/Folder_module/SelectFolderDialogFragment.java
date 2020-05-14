package com.example.ttett.Folder_module;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.example.ttett.Entity.Email;
import com.example.ttett.Entity.Folder;
import com.example.ttett.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SelectFolderDialogFragment extends DialogFragment implements View.OnClickListener{
    private TextView TvConfirm,TvCancel;
    private EditText EtName;
//    private Folder folder;
    private Email email;
    private FolderService folderService;
    private String folder_name;
    private String TAG = "SelectFolderDialogFragment";
    private RecyclerView Rv;
    private SelectFolderAdapter adapter;
    private List<Integer> id_item;



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
        View view = LayoutInflater.from(getContext()).inflate(R.layout.select_folder_dialog,null);

        TvCancel = view.findViewById(R.id.cancel);
        try{
            email = getArguments().getParcelable("email");
            id_item = getArguments().getIntegerArrayList("id_item");
        }catch (Exception e){
        }

        TvCancel.setOnClickListener(this);
        Rv = view.findViewById(R.id.select_folder_rv);
        Rv.setLayoutManager(new LinearLayoutManager(getContext()));
        folderService = new FolderService(getContext());
        List<Folder> folders = folderService.queryAllFolder(email);
        adapter = new SelectFolderAdapter(getContext(),folders,id_item);
        Rv.setAdapter(adapter);

        builder.setView(view);
        return builder.create();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
                case R.id.cancel:
                    dismiss();
                    break;
                    default:
         }
    }


    @Override
    public void onStart() {
        super.onStart();
        Window win = getDialog().getWindow();
        win.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);

        WindowManager.LayoutParams params = win.getAttributes();
        params.gravity = Gravity.CENTER_HORIZONTAL;
        params.width = 1200;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        win.setAttributes(params);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
