package com.example.ttett.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ttett.Entity.Mail;
import com.example.ttett.OpenMailActivity;
import com.example.ttett.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class InboxAdapter extends RecyclerView.Adapter<InboxAdapter.InboxViewHolder> {
    private List<Mail> mMailList ;
    private Context mContext;

    public InboxAdapter(Context context,List<Mail> mailList){
        this.mContext = context;
        this.mMailList = mailList;
    }
    @NonNull
    @Override
    public InboxAdapter.InboxViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.inbox_rv_item,parent,false);
        final InboxViewHolder holder = new InboxViewHolder(view);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Mail mail = mMailList.get(position);
                Intent intent = new Intent(mContext, OpenMailActivity.class);
                intent.putExtra("mail",mail);
//                intent.putExtra(OpenMailActivity.MAIL_NAME,mail.getFrom());
//                intent.putExtra(OpenMailActivity.MAIL_SUBJECT,fruit.getImageId());
                mContext.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull InboxAdapter.InboxViewHolder holder, int position) {
        Mail mail = mMailList.get(position);
        holder.mName.setText(mail.getFrom());
        holder.mSubject.setText(mail.getSubject());
        holder.mTime.setText(mail.outDate(String.valueOf(mail.getSendDate())));
    }

    @Override
    public int getItemCount() {
        return mMailList.size();
    }

    static class InboxViewHolder extends RecyclerView.ViewHolder{
        CircleImageView Icon;
        TextView mName,mTime,mSubject,mContent;
        CardView cardView;

        public InboxViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.mail_cv);
            Icon = itemView.findViewById(R.id.from_icon);
            mName = itemView.findViewById(R.id.from_name);
            mTime = itemView.findViewById(R.id.from_time);
            mSubject = itemView.findViewById(R.id.mail_subject);
            mContent = itemView.findViewById(R.id.mail_content);
        }
    }
}
