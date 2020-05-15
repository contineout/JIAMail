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

import com.example.ttett.Dao.EmailDao;
import com.example.ttett.Entity.EmailMessage;
import com.example.ttett.Folder_module.SelectFolderDialogFragment;
import com.example.ttett.R;
import com.example.ttett.Service.MailService;
import com.example.ttett.WriteLetterActivity;
import com.example.ttett.bean.MessageEvent;

import org.greenrobot.eventbus.EventBus;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;

public class MenuDialogFragment extends DialogFragment implements View.OnClickListener {
    private CardView edit,star,delete,tran;
    private EmailMessage emailMessage;
    private String mFromFrag;


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
        View view = LayoutInflater.from(getContext()).inflate(R.layout.select_email_dialog,null);

        emailMessage = getArguments().getParcelable("message");

        edit = view.findViewById(R.id.edit);
        star = view.findViewById(R.id.star);
        delete = view.findViewById(R.id.delete);
        tran = view.findViewById(R.id.tran);

        edit.setOnClickListener(this);
        star.setOnClickListener(this);
        delete.setOnClickListener(this);
        tran.setOnClickListener(this);
        builder.setView(view);
        return builder.create();
    }

    @Override
    public void onClick(View v) {
        int id = emailMessage.getId();
        Intent intent = null;
        Bundle bundle = null;
        MailService mailService = new MailService(getContext());
        switch (v.getId()){
            case R.id.edit:
                intent = new Intent(getContext(), WriteLetterActivity.class);
                bundle = new Bundle();
                EmailDao dao = new EmailDao(getContext());
                bundle.putParcelable("emailMessage",emailMessage);
                bundle.putParcelable("email",dao.QueryNewEmail(emailMessage.getEmail_id()));
                bundle.putString("flag","edit");
                intent.putExtras(bundle);
                getContext().startActivity(intent);
                dismiss();
                break;
            case R.id.star:
                mailService.updateStar(id);
                EventBus.getDefault().postSticky(new MessageEvent("update_message"));
                dismiss();
                break;
            case R.id.delete:
                if(mailService.queryisDelete(id)==1){
                    DeleteDialogFragment deleteDialogFragment = new DeleteDialogFragment();
                    bundle = new Bundle();
                    bundle.putInt("id", id);
                    deleteDialogFragment.setArguments(bundle);
                    deleteDialogFragment.show(getFragmentManager(),"deleteDialogFragment");
                }else{
                    mailService.updateisDelete(id);
                    EventBus.getDefault().postSticky(new MessageEvent("update_message"));
                }
                dismiss();
                break;
            case R.id.tran:
                SelectFolderDialogFragment dialogFragment = new SelectFolderDialogFragment ();
                bundle = new Bundle();
                bundle.putInt("id",id);
                bundle.putInt("email_id",emailMessage.getEmail_id());
                dialogFragment.setArguments(bundle);
                dialogFragment.show(getFragmentManager(),"SelectFolderDialogFragment");
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
