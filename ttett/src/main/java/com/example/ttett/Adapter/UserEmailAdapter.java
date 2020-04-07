package com.example.ttett.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.ttett.Entity.Email;
import com.example.ttett.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

//xkdmegyobzcdhggd
//1272179741@qq.com
//"d8405717ca1664a2" xl335665873@sina.com
//    ykqxthnonvynbbeh 1023851233@qq.com
public class UserEmailAdapter extends RecyclerView.Adapter<UserEmailAdapter.ViewHolder> {
    private List<Email> mEmails;
    private Context mContext;

    static class ViewHolder extends RecyclerView.ViewHolder{
        private RelativeLayout email_item ;
        private TextView address;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            address = itemView.findViewById(R.id.email_address);
        }
    }

    public UserEmailAdapter(Context context, List<Email> emails){
        mEmails = emails;
        mContext = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_mail_rv_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Email email = mEmails.get(position);
        holder.address.setText(email.getAddress());
    }

    @Override
    public int getItemCount() {
        return mEmails.size();
    }


}
