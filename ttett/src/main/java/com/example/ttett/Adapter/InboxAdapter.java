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

import com.example.ttett.Entity.Email;
import com.example.ttett.Entity.EmailMessage;
import com.example.ttett.OpenMailActivity;
import com.example.ttett.R;
import com.example.ttett.bean.MessageEvent;
import com.example.ttett.selectAcitvity.SelectMailActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class InboxAdapter extends RecyclerView.Adapter<InboxAdapter.InboxViewHolder> {
    private List<EmailMessage> mEmailMessages ;
    private Context mContext;
    private String mFromFrag;
    private Email mEmail;
    public static String inboxFragment = "inboxFragment";
    public static String sendedFragment = "sendedFragment";
    public static String deleteFragment = "deleteFragment";

    public InboxAdapter(Context context, List<EmailMessage> emailMessages, String fromFrag, Email email){
        this.mContext = context;
        this.mEmailMessages = emailMessages;
        this.mFromFrag = fromFrag;
        this.mEmail = email;
    }
    @NonNull
    @Override
    public InboxAdapter.InboxViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.inbox_rv_item,parent,false);
        final InboxViewHolder holder = new InboxViewHolder(view);
        holder.inbox_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                EmailMessage emailMessage = mEmailMessages.get(position);
                Intent intent = new Intent(mContext, OpenMailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("emailMessage",emailMessage);
                bundle.putString("from_Frag",mFromFrag);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });

        holder.inbox_item.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(mContext, SelectMailActivity.class);
                EventBus.getDefault().postSticky(new MessageEvent("selectMessage",mEmail,mFromFrag,mEmailMessages));
                mContext.startActivity(intent);
                return true;
            }
        });

//        LongClickUtils.setLongClick(new Handler(), view, 1000, new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                Intent intent = new Intent(mContext, SelectMailActivity.class);
//                Bundle bundle = new Bundle();
//                ArrayList<EmailMessage> Messages = (ArrayList<EmailMessage>) mEmailMessages;
//                bundle.putParcelableArrayList("emailMessages",Messages);
//                bundle.putString("from_Frag",mFromFrag);
//                bundle.putParcelable("email",mEmail);
//                intent.putExtras(bundle);
//                mContext.startActivity(intent);
//                return true;
//            }
//        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull InboxAdapter.InboxViewHolder holder, int position) {
        EmailMessage message = mEmailMessages.get(position);
        if (message.getIsRead() == 0) {
            holder.isReadflag.setVisibility(View.VISIBLE);
        } else {
            holder.isReadflag.setVisibility(View.GONE);
        }
        if (message.getIsStar() == 0) {
            holder.isStarflag.setVisibility(View.GONE);
        } else {
            holder.isStarflag.setVisibility(View.VISIBLE);
        }
        if (message.getIsAttachment() == 1) {
            holder.isAttachment.setVisibility(View.VISIBLE);
        } else {
            holder.isAttachment.setVisibility(View.GONE);
        }
        holder.mTime.setText(message.getSendDate().substring(5, 16));
        holder.mSubject.setText(message.getSubject());
        holder.mContent.setText(message.getContent());
        if (mFromFrag.equals(sendedFragment)) {
            String to = message.getTo();
            holder.mName.setText("TO:" + " " + to);
        } else {
            String[] from = message.getFrom().split("[<>]");
            holder.mName.setText(from[0]);
        }
    }

    @Override
    public int getItemCount() {
        return mEmailMessages.size();
    }

    static class InboxViewHolder extends RecyclerView.ViewHolder{
        CircleImageView Icon,isReadflag;
        TextView mName,mTime,mSubject,mContent;
        LinearLayout inbox_item;
        ImageView isStarflag,isAttachment;

        public InboxViewHolder(@NonNull View itemView) {
            super(itemView);
            isStarflag = itemView.findViewById(R.id.isStarflag);
            isReadflag = itemView.findViewById(R.id.isReadflag);
            inbox_item = itemView.findViewById(R.id.inbox_item);
            Icon = itemView.findViewById(R.id.from_icon);
            mName = itemView.findViewById(R.id.from_name);
            mTime = itemView.findViewById(R.id.from_time);
            mSubject = itemView.findViewById(R.id.mail_subject);
            mContent = itemView.findViewById(R.id.mail_content);
            isAttachment = itemView.findViewById(R.id.attachment_1);
        }
    }
}
