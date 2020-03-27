package com.example.ttett.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ttett.Entity.EmailMessage;
import com.example.ttett.R;
import com.example.ttett.bean.MessageEvent;

import org.greenrobot.eventbus.EventBus;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.text.TextUtils.TruncateAt.END;

public class SelectAdapter extends RecyclerView.Adapter<SelectAdapter.InboxViewHolder> {
    private List<EmailMessage> mEmailMessages ;
    private Context mContext;

    public SelectAdapter(Context context, List<EmailMessage> emailMessages){
        this.mContext = context;
        this.mEmailMessages = emailMessages;
    }


    @NonNull
    @Override
    public SelectAdapter.InboxViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.select_mail_item,parent,false);
        final InboxViewHolder holder = new InboxViewHolder(view);
        holder.inbox_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.checkBox.setChecked(true);
                EventBus.getDefault().post(new MessageEvent("check"));
                int position = holder.getAdapterPosition();

            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull SelectAdapter.InboxViewHolder holder, int position) {
        EmailMessage message = mEmailMessages.get(position);

        DateFormat format = new SimpleDateFormat("MM-dd HH:mm");
        Date date;
        String simpleDate;
        try {
            if(message.getSendDate()!=null){
                date = format.parse(message.getSendDate());
                simpleDate = date.toString();
                holder.mTime.setText(simpleDate);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        String[] from = message.getFrom().split("[<>]");
        holder.mName.setText(from[0]);
        holder.mSubject.setText(message.getSubject());
        holder.mContent.setText(message.getContent());
        holder.mContent.setEllipsize(END);
        holder.mContent.setMaxLines(2);
        holder.mContent.setEms(15);

    }

    @Override
    public int getItemCount() {
        return mEmailMessages.size();
    }

    static class InboxViewHolder extends RecyclerView.ViewHolder{
        CircleImageView Icon;
        TextView mName,mTime,mSubject,mContent;
        CheckBox checkBox;
        LinearLayout inbox_item;

        public InboxViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.message_cb);
            inbox_item = itemView.findViewById(R.id.inbox_item);
            Icon = itemView.findViewById(R.id.from_icon);
            mName = itemView.findViewById(R.id.from_name);
            mTime = itemView.findViewById(R.id.from_time);
            mSubject = itemView.findViewById(R.id.mail_subject);
            mContent = itemView.findViewById(R.id.mail_content);
        }
    }
}
