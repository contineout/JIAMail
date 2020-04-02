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

import com.example.ttett.Entity.Attachment;
import com.example.ttett.R;
import com.example.ttett.bean.MessageEvent;
import com.example.ttett.util.SelectIcon;
import com.example.ttett.util.SizeTran;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SelectAttachmentAdapter extends RecyclerView.Adapter<SelectAttachmentAdapter.InboxViewHolder> {
    private List<Attachment> mAttachments ;
    private Context mContext;
    private Map<Integer, Boolean> checkStatus;


    public SelectAttachmentAdapter(Context context, List<Attachment> attachments, Map<Integer, Boolean> checkStatus){
        this.mContext = context;
        this.mAttachments = attachments;
        this.checkStatus = checkStatus;
    }


    @NonNull
    @Override
    public SelectAttachmentAdapter.InboxViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.select_attachment_item,parent,false);
        return new InboxViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SelectAttachmentAdapter.InboxViewHolder holder, final int position) {
        final Attachment attachment = mAttachments.get(position);
        SelectIcon selectIcon = new SelectIcon();
        holder.name.setText(attachment.getName());
        holder.date.setText(attachment.getSaveDate());
        selectIcon.Attachment(holder.icon,attachment.getType());
        SizeTran sizeTran = new SizeTran();
        sizeTran.start(holder.size,attachment.getSize());

        holder.checkBox.setChecked(checkStatus.get(position));
        holder.checkBox.setOnCheckedChangeListener(null);
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkStatus.put(position,isChecked);
                EventBus.getDefault().post(new MessageEvent("attachment_check_status",checkStatus));
            }
        });
        holder.attachment_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkStatus.get(position)){
                    checkStatus.put(position,false);
                    holder.checkBox.setChecked(checkStatus.get(position));

                }else {
                    checkStatus.put(position,true);
                    holder.checkBox.setChecked(checkStatus.get(position));
                }
                EventBus.getDefault().post(new MessageEvent("attachment_check_status",checkStatus));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mAttachments.size();
    }

    static class InboxViewHolder extends RecyclerView.ViewHolder{
        TextView name,size,date;
        ImageView icon;
        CheckBox checkBox;
        LinearLayout attachment_item;

        public InboxViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.attachment_name);
            size = itemView.findViewById(R.id.attachment_size);
            date = itemView.findViewById(R.id.attachment_datetime);
            icon = itemView.findViewById(R.id.attachment_Iv);
            checkBox = itemView.findViewById(R.id.attachment_cb);
            attachment_item = itemView.findViewById(R.id.attachment_item);
        }
    }

}
