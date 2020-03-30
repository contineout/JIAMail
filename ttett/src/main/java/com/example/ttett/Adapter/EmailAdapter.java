package com.example.ttett.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ttett.Entity.Email;
import com.example.ttett.R;
import com.example.ttett.bean.MessageEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
//xkdmegyobzcdhggd
//1272179741@qq.com
//"d8405717ca1664a2" xl335665873@sina.com
//    ykqxthnonvynbbeh 1023851233@qq.com
public class EmailAdapter extends RecyclerView.Adapter<EmailAdapter.ViewHolder> {
    private List<Email> mEmails;
    private Context mContext;

    static class ViewHolder extends RecyclerView.ViewHolder{
        private CardView email_item ;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            email_item = itemView.findViewById(R.id.Email_cv);
        }
    }

    public EmailAdapter(Context context,List<Email> emails){

        mEmails = emails;
        mContext = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.eamil_item,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        holder.email_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Email email = mEmails.get(position);
                MessageEvent messageEvent = new MessageEvent();
                messageEvent.setEmail(email);
                EventBus.getDefault().post(new MessageEvent("Switch_Email",email));
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Email email = mEmails.get(position);
    }

    @Override
    public int getItemCount() {
        return mEmails.size();
    }


}
