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

public class SelectFolderAdapter extends RecyclerView.Adapter<SelectFolderAdapter.FolderViewHolder> {

    private List<Folder> mFolders ;
    private Context mContext;

    public SelectFolderAdapter(Context context, List<Folder> folders){
        this.mContext = context;
        this.mFolders = folders;
    }

    @NonNull
    @Override
    public SelectFolderAdapter.FolderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.select_folder_rv_item,parent,false);
        final FolderViewHolder holder = new FolderViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull SelectFolderAdapter.FolderViewHolder holder, int position) {
        Folder folder = mFolders.get(position);
        holder.folder_name.setText(folder.getFolder_name());
    }

    @Override
    public int getItemCount() {
        return mFolders.size();
    }
    static class FolderViewHolder extends RecyclerView.ViewHolder{
        TextView folder_name;

        public FolderViewHolder(@NonNull View itemView) {
            super(itemView);

            folder_name = itemView.findViewById(R.id.folder_name);
        }
    }
}
