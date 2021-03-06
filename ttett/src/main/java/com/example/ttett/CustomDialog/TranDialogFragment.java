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

import com.example.ttett.Dao.EmailDao;
import com.example.ttett.Entity.EmailMessage;
import com.example.ttett.R;
import com.example.ttett.WriteLetterActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class TranDialogFragment extends DialogFragment implements View.OnClickListener {
    private TextView TvTran,TvReply;


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
        View view = LayoutInflater.from(getContext()).inflate(R.layout.tran_message_dialog,null);
        TvTran = view.findViewById(R.id.message_tran);
        TvReply = view.findViewById(R.id.message_reply);

        TvTran.setOnClickListener(this);
        TvReply.setOnClickListener(this);
        builder.setView(view);
        return builder.create();
    }

    @Override
    public void onClick(View v) {
        EmailMessage emailMessage = getArguments().getParcelable("message");
        EmailDao dao = new EmailDao(getContext());
        Intent intent = null;
        Bundle bundle = null;
        switch (v.getId()){
            case R.id.message_tran:
                intent = new Intent(getContext(), WriteLetterActivity.class);
                bundle = new Bundle();

                bundle.putParcelable("emailMessage",emailMessage);
                bundle.putParcelable("email",dao.QueryNewEmail(emailMessage.getEmail_id()));
                bundle.putString("flag","tran");
                intent.putExtras(bundle);
                getContext().startActivity(intent);
                dismiss();
                break;
            case R.id.message_reply:
                intent = new Intent(getContext(), WriteLetterActivity.class);
                bundle = new Bundle();
                bundle.putParcelable("emailMessage",emailMessage);
                bundle.putParcelable("email",dao.QueryNewEmail(emailMessage.getEmail_id()));
                bundle.putString("flag","reply");
                intent.putExtras(bundle);
                getContext().startActivity(intent);
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
