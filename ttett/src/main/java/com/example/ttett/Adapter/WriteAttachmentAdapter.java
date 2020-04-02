package com.example.ttett.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ttett.Entity.Attachment;
import com.example.ttett.R;
import com.example.ttett.util.SelectIcon;
import com.example.ttett.util.SizeTran;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class WriteAttachmentAdapter extends RecyclerView.Adapter<WriteAttachmentAdapter.FolderViewHolder> {

    private List<Attachment> mAttachments ;
    private Context mContext;

    public WriteAttachmentAdapter(Context context, List<Attachment> Attachments){
        this.mContext = context;
        this.mAttachments = Attachments;
    }
    @NonNull
    @Override
    public WriteAttachmentAdapter.FolderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.write_attachment_rv_item,parent,false);
        return new FolderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WriteAttachmentAdapter.FolderViewHolder holder, int position) {
        Attachment attachment = mAttachments.get(position);
        SelectIcon selectIcon = new SelectIcon();
        holder.name.setText(attachment.getName());
        SizeTran sizeTran = new SizeTran();
        sizeTran.start(holder.size,attachment.getSize());
        selectIcon.Attachment(holder.icon,attachment.getType());
    }

    @Override
    public int getItemCount() {
        return mAttachments.size();
    }

    static class FolderViewHolder extends RecyclerView.ViewHolder{
        TextView name,size;
        ImageView icon;

        public FolderViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.attachment_name);
            size = itemView.findViewById(R.id.attachment_size);
            icon = itemView.findViewById(R.id.attachment_Iv);
        }
    }
}
