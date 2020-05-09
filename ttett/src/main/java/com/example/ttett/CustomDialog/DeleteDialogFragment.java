package com.example.ttett.CustomDialog;

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
import android.widget.TextView;

import com.example.ttett.R;
import com.example.ttett.Service.EmailService;
import com.example.ttett.Service.MailService;
import com.example.ttett.bean.MessageEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class DeleteDialogFragment extends DialogFragment implements View.OnClickListener{
    private TextView TvConfirm,TvCancel;
    private String TAG = "DeleteDialogFragment";
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
        View view = LayoutInflater.from(getContext()).inflate(R.layout.delete_dialog,null);
        TvConfirm = view.findViewById(R.id.delete_dialog_confirm);
        TvCancel = view.findViewById(R.id.delete_dialog_cancel);
        try{
            id_item = getArguments().getIntegerArrayList("id_item");
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
            case R.id.delete_dialog_confirm:
                deleteMessage();
                getActivity().finish();
                break;
                case R.id.delete_dialog_cancel:
                    dismiss();
                    break;
                    default:
         }
    }

    private void deleteMessage(){
        MailService mailService = new MailService(getContext());
        EmailService emailService = new EmailService(getContext());
        if(id_item!=null){
            for(int id:id_item){
                mailService.deleteMessage(id);
                EventBus.getDefault().postSticky(new MessageEvent("update_message"));
            }
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
