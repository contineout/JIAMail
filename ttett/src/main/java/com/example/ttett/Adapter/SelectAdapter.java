package com.example.ttett.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ttett.Entity.EmailMessage;
import com.example.ttett.R;
import com.example.ttett.bean.MessageEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.text.TextUtils.TruncateAt.END;

public class SelectAdapter extends RecyclerView.Adapter<SelectAdapter.InboxViewHolder> {
    private List<EmailMessage> mEmailMessages ;
    private Context mContext;
    private Map<Integer, Boolean> checkStatus;


    public SelectAdapter(Context context, List<EmailMessage> emailMessages,Map<Integer, Boolean> checkStatus){
        this.mContext = context;
        this.mEmailMessages = emailMessages;
        this.checkStatus = checkStatus;
    }

    @NonNull
    @Override
    public SelectAdapter.InboxViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.select_mail_item,parent,false);
        InboxViewHolder holder = new InboxViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final SelectAdapter.InboxViewHolder holder, final int position) {
        final EmailMessage message = mEmailMessages.get(position);
        if(message.getIsRead() == 0){
            holder.isReadflag.setVisibility(View.VISIBLE);
        }else {
            holder.isReadflag.setVisibility(View.GONE);
        }

        if(message.getIsStar() == 0){
            holder.isStarflag.setVisibility(View.GONE);
        }else {
            holder.isStarflag.setVisibility(View.VISIBLE);
        }
        holder.mTime.setText(message.getSendDate().substring(5,16));
        String[] from = message.getFrom().split("[<>]");
        holder.mName.setText(from[0]);
        holder.mSubject.setText(message.getSubject());
        holder.mContent.setText(message.getContent());
        holder.mContent.setEllipsize(END);
        holder.mContent.setMaxLines(2);
        holder.mContent.setEms(15);

        holder.checkBox.setChecked(checkStatus.get(position));
        holder.checkBox.setOnCheckedChangeListener(null);
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkStatus.put(position,isChecked);
                EventBus.getDefault().post(new MessageEvent("check_status",checkStatus));
            }
        });
        holder.inbox_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkStatus.get(position)){
                    checkStatus.put(position,false);
                    holder.checkBox.setChecked(checkStatus.get(position));

                }else {
                    checkStatus.put(position,true);
                    holder.checkBox.setChecked(checkStatus.get(position));
                }
                EventBus.getDefault().post(new MessageEvent("check_status",checkStatus));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mEmailMessages.size();
    }

    static class InboxViewHolder extends RecyclerView.ViewHolder{
        CircleImageView Icon,isReadflag;
        TextView mName,mTime,mSubject,mContent;
        CheckBox checkBox;
        LinearLayout inbox_item;
        ImageView isStarflag;

        public InboxViewHolder(@NonNull View itemView) {
            super(itemView);
            isStarflag = itemView.findViewById(R.id.isStarflag);
            isReadflag = itemView.findViewById(R.id.isReadflag);
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
