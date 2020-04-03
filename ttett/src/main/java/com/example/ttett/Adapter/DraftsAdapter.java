package com.example.ttett.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ttett.Entity.EmailMessage;
import com.example.ttett.R;
import com.example.ttett.WriteLetterActivity;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DraftsAdapter extends RecyclerView.Adapter<DraftsAdapter.InboxViewHolder> {
    private List<EmailMessage> mEmailMessages ;
    private Context mContext;

    public DraftsAdapter(Context context, List<EmailMessage> emailMessages){
        this.mContext = context;
        this.mEmailMessages = emailMessages;
    }
    @NonNull
    @Override
    public DraftsAdapter.InboxViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.drafts_rv_item,parent,false);
        final InboxViewHolder holder = new InboxViewHolder(view);
        holder.drafts_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                EmailMessage emailMessage = mEmailMessages.get(position);
                Intent intent = new Intent(mContext, WriteLetterActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("emailMessage",emailMessage);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull DraftsAdapter.InboxViewHolder holder, int position) {
        EmailMessage message = mEmailMessages.get(position);
            if(message.getSubject().equals("")){
                holder.mSubject.setText("(无主题)");
            }else {
                holder.mSubject.setText(message.getSubject());
            }
            if(message.getTo().equals("")){
                holder.mName.setText("(无收件人)");
            }else{
                holder.mName.setText(message.getTo());
            }
            if(message.getIsAttachment() == 1){
                holder.isAttachment.setVisibility(View.VISIBLE);
            }else{
                holder.isAttachment.setVisibility(View.GONE);
            }
            holder.mTime.setText(message.getSendDate().substring(5,16));
            holder.mContent.setText(message.getContent());
        }

    @Override
    public int getItemCount() {
        return mEmailMessages.size();
    }

    static class InboxViewHolder extends RecyclerView.ViewHolder{
        TextView mName,mTime,mSubject,mContent;
        LinearLayout drafts_item;
        ImageView isAttachment;

        public InboxViewHolder(@NonNull View itemView) {
            super(itemView);
            drafts_item = itemView.findViewById(R.id.draft_item);
            mName = itemView.findViewById(R.id.from_name);
            mTime = itemView.findViewById(R.id.from_time);
            mSubject = itemView.findViewById(R.id.mail_subject);
            mContent = itemView.findViewById(R.id.mail_content);
            isAttachment = itemView.findViewById(R.id.attachment_1);
        }
    }
}
