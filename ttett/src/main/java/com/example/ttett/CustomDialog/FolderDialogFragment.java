package com.example.ttett.CustomDialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.ttett.R;
import com.example.ttett.fragment.FolderFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class FolderDialogFragment extends DialogFragment implements View.OnClickListener{
    public static final String FOLDER_NAME = "folder_name";
    private TextView TvConfirm,TvCancel;
    private EditText EtName;



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

        TvConfirm.setOnClickListener(this);
        TvCancel.setOnClickListener(this);
        builder.setView(view);
        return builder.create();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_folder_dialog_confirm:
                if(getTargetFragment() == null){
                    return;
                }
                Intent intent = new Intent();
                intent.putExtra(FOLDER_NAME,EtName.getText().toString());
                getTargetFragment().onActivityResult(FolderFragment.REQUEST_CODE, Activity.RESULT_OK,intent);
                break;
            case R.id.add_folder_dialog_cancel:
                dismiss();
                break;
                default:

        }
    }
}
