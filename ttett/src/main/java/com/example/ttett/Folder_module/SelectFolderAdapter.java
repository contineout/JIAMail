package com.example.ttett.Folder_module;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.ttett.Entity.Folder;
import com.example.ttett.R;
import com.example.ttett.bean.MessageEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SelectFolderAdapter extends RecyclerView.Adapter<SelectFolderAdapter.FolderViewHolder> {

    private List<Folder> mFolders ;
    private Context mContext;
    private List<Integer> id_item;

    public SelectFolderAdapter(Context context, List<Folder> folders,List<Integer> id_item){
        this.mContext = context;
        this.mFolders = folders;
        this.id_item = id_item;
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
        final Folder folder = mFolders.get(position);
        holder.folder_name.setText(folder.getFolder_name());
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FolderService folderService = new FolderService(mContext);
                folderService.updateFolder(id_item,folder.getFolder_id());
                EventBus.getDefault().post(new MessageEvent("dismiss"));
                EventBus.getDefault().post(new MessageEvent("update_message"));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mFolders.size();
    }
    static class FolderViewHolder extends RecyclerView.ViewHolder{
        TextView folder_name;
        RelativeLayout item;

        public FolderViewHolder(@NonNull View itemView) {
            super(itemView);

            folder_name = itemView.findViewById(R.id.folder_name);
            item = itemView.findViewById(R.id.select_folder_item);
        }
    }
}
