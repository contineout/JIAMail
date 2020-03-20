package com.example.ttett.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ttett.Entity.Folder;
import com.example.ttett.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.FolderViewHolder> {

    private List<Folder> mFolders ;
    private Context mContext;

    public FolderAdapter(Context context,List<Folder> folders){
        this.mContext = context;
        this.mFolders = folders;
    }

    @NonNull
    @Override
    public FolderAdapter.FolderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.folder_rv_item,parent,false);
        final FolderViewHolder holder = new FolderViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull FolderAdapter.FolderViewHolder holder, int position) {
        Folder folder = mFolders.get(position);
        holder.folder_name.setText(folder.getFolder_name());
        holder.message_count.setText(folder.getMessage_number());
        holder.datetime.setText(folder.getDatetime());
    }

    @Override
    public int getItemCount() {
        return mFolders.size();
    }
    static class FolderViewHolder extends RecyclerView.ViewHolder{
        TextView folder_name,message_count,datetime;

        public FolderViewHolder(@NonNull View itemView) {
            super(itemView);

            folder_name = itemView.findViewById(R.id.folder_name);
            message_count = itemView.findViewById(R.id.folder_message_count);
            datetime = itemView.findViewById(R.id.folder_datetime);
        }
    }
}
