package com.example.ttett.Contact_module;

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

import com.example.ttett.Entity.Contact;
import com.example.ttett.R;
import com.example.ttett.bean.MessageEvent;
import com.example.ttett.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class DeleteContactFragment extends DialogFragment implements View.OnClickListener{
    private TextView TvConfirm,TvCancel,TvWarn;
    private String TAG = "DeleteDialogFragment";
    private int id,position;
    private Contact contact;
    private ContactsAdapter adapter;



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
        View view = LayoutInflater.from(getContext()).inflate(R.layout.delete_contact_dialog,null);
        TvConfirm = view.findViewById(R.id.delete_dialog_confirm);
        TvCancel = view.findViewById(R.id.delete_dialog_cancel);
        TvWarn = view.findViewById(R.id.warning);
        try{
           contact  = getArguments().getParcelable("contact");
            position  = getArguments().getInt("position");
        }catch (Exception e){
        }
        TvWarn.setText("你确定永久删除该"+contact.getName()+"?");
        TvConfirm.setOnClickListener(this);
        TvCancel.setOnClickListener(this);
        builder.setView(view);
        return builder.create();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.delete_dialog_confirm:
                deleteContact();
                EventBus.getDefault().post(new MessageEvent("deleteContact"));
                dismiss();
                break;
                case R.id.delete_dialog_cancel:
                    dismiss();
                    break;
                    default:
         }
    }

    private void deleteContact(){
        ContactService contactService = new ContactService(getContext());
        if(contactService.deleteContact(contact.getContact_id()));{
            if(contactService.deleteContact(contact.getContact_id())){
                ToastUtil.showTextToas(getContext(),contact.getName()+"已经删除");
                adapter.removeData(position);
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
