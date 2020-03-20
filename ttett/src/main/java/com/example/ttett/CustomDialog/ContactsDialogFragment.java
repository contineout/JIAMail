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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class ContactsDialogFragment extends DialogFragment implements View.OnClickListener{
    public static final String FOLDER_NAME = "folder_name";
    private TextView TvImport,TvNew;
    public static final int REQUEST = 1;
    private Email email;
    private String TAG = "ContactsDialogFragment";


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

        TvImport.setOnClickListener(this);
        TvNew.setOnClickListener(this);
        builder.setView(view);
        return builder.create();
    }
    public void tiao(){

    }


    @Override
    public void onClick(View v) {
        Intent intent = null;

        switch (v.getId()){
            case R.id.contacts_import:
                dismiss();
                break;
            case R.id.contacts_new:
                int user_id = getArguments().getInt("user_id");
                Log.d(TAG,"dddd"+user_id);
                intent = new Intent(getContext(),ContactsActivity.class);
                intent.putExtra("user_id",user_id);
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

//    public static ContactsDialogFragment newInstance(int user_id){
//        ContactsDialogFragment contactsDialogFragment = new ContactsDialogFragment();
//        Bundle bundle = new Bundle();
//        bundle.putInt("user_id",user_id);
//        contactsDialogFragment.setArguments(bundle);
//        return  contactsDialogFragment;
//    }
}
