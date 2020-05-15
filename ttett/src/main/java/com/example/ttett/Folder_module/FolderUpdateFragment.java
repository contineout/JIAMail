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

import com.example.ttett.R;
import com.example.ttett.bean.MessageEvent;
import com.example.ttett.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class FolderUpdateFragment extends DialogFragment implements View.OnClickListener{
    private TextView TvConfirm,TvCancel;
    private EditText EtName;
    private int id;
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
        View view = LayoutInflater.from(getContext()).inflate(R.layout.update_folder_dialog,null);
        TvConfirm = view.findViewById(R.id.add_folder_dialog_confirm);
        TvCancel = view.findViewById(R.id.add_folder_dialog_cancel);
        EtName = view.findViewById(R.id.add_folder_dialog_name);
        try{
            id = getArguments().getInt("id");
        }catch (Exception e){
        }


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
                if(folder_name.equals("")){
                    ToastUtil.showTextToas(getContext(),"请输入文件名");
                }else{
                    FolderDao dao = new FolderDao(getContext());
                    dao.updateFolderName(id,folder_name);
                    EventBus.getDefault().post(new MessageEvent("updateFolder"));
                }
                dismiss();
                break;
                case R.id.add_folder_dialog_cancel:
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
}
