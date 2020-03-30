package com.example.ttett.CustomDialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.ttett.ContactsActivity;
import com.example.ttett.Entity.Email;
import com.example.ttett.R;
import com.example.ttett.Service.ContactService;
import com.example.ttett.bean.MessageEvent;

import org.greenrobot.eventbus.EventBus;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class ContactsDialogFragment extends DialogFragment implements View.OnClickListener{
    private TextView TvImport,TvNew;
    private String TAG = "ContactsDialogFragment";
    private ContactService contactService ;
    private Email email;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        return super.onCreateView(inflater,container,savedInstanceState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.add_contacts_dialog,null);
        TvImport = view.findViewById(R.id.contacts_import);
        TvNew = view.findViewById(R.id.contacts_new);
        assert getArguments() != null;
        try{
            email = getArguments().getParcelable("email");
            Log.d(TAG,"email_id" + email.getAddress());
        }catch (NullPointerException e){

        }

        TvImport.setOnClickListener(this);
        TvNew.setOnClickListener(this);
        builder.setView(view);
        return builder.create();
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;

        int email_id = email.getEmail_id();
        Log.d(TAG,"email_id" + email_id);
        switch (v.getId()){
            case R.id.contacts_import:
                contactService = new ContactService(getContext());
                contactService.insertAllMailContact(email_id);
                EventBus.getDefault().post(new MessageEvent("add_contact",email_id));
                dismiss();
                break;
            case R.id.contacts_new:
                intent = new Intent(getContext(),ContactsActivity.class);
                intent.putExtra("email_id",email_id);
                startActivity(intent);
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
