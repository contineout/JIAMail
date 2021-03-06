package com.example.ttett.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.ttett.Entity.Email;
import com.example.ttett.R;
import com.example.ttett.bean.MessageEvent;
import com.example.ttett.util.CircleTextImage.CircleTextImage;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
public class EmailAdapter extends RecyclerView.Adapter<EmailAdapter.ViewHolder> {
    private List<Email> mEmails;
    private Context mContext;

    static class ViewHolder extends RecyclerView.ViewHolder{
        private RelativeLayout email_item ;
        CircleTextImage icon;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            email_item = itemView.findViewById(R.id.Email_cv);
            icon = itemView.findViewById(R.id.email_icon);
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
                EventBus.getDefault().postSticky(new MessageEvent("Switch_Email",email));
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Email email = mEmails.get(position);
        holder.icon.setText4CircleImage(email.getName().substring(0,1));
        holder.icon.setCircleColor(email.getAvatar_color());
    }

    @Override
    public int getItemCount() {
        return mEmails.size();
    }


}
