package com.example.ttett.CustomDialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
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

import com.example.ttett.Dao.MailDao;
import com.example.ttett.Entity.EmailMessage;
import com.example.ttett.R;
import com.example.ttett.bean.MessageEvent;

import org.greenrobot.eventbus.EventBus;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class SaveMessageDialogFragment extends DialogFragment implements View.OnClickListener {
    private TextView TvSave,TvDelete;
    private String TAG = "SaveMessageDialogFragment";
    private MailDao mailDao;


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
        View view = LayoutInflater.from(getContext()).inflate(R.layout.save_message_dialog,null);
        TvSave = view.findViewById(R.id.message_save);
        TvDelete = view.findViewById(R.id.message_delete);

        TvSave.setOnClickListener(this);
        TvDelete.setOnClickListener(this);
        builder.setView(view);
        return builder.create();
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;

        switch (v.getId()){
            case R.id.message_delete:
                dismiss();
                getActivity().finish();
                break;
            case R.id.message_save:
                EmailMessage emailMessage = getArguments().getParcelable("message");
                mailDao = new MailDao(getContext());
                mailDao.InsertMessages(emailMessage);
                EventBus.getDefault().post(new MessageEvent("NewSendedMessage"));
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
        params.gravity = Gravity.BOTTOM;
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        win.setAttributes(params);
    }
}
