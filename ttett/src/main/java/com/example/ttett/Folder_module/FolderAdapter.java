package com.example.ttett.Folder_module;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ttett.Entity.Email;
import com.example.ttett.Entity.Folder;
import com.example.ttett.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.FolderViewHolder> {

    private List<Folder> mFolders ;
    private Context mContext;
    public Email email;
    private Folder folder;

    public FolderAdapter(Context context,List<Folder> folders,Email email){
        this.mContext = context;
        this.mFolders = folders;
        this.email = email;
    }

    @NonNull
    @Override
    public FolderAdapter.FolderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.folder_rv_item,parent,false);
        final FolderViewHolder holder = new FolderViewHolder(view);
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                folder = mFolders.get(position);
                Intent intent = new Intent(mContext, OpenFolderActivity.class);
                intent.putExtra("folder",folder);
                intent.putExtra("email",email);
//                EventBus.getDefault().postSticky(new MessageEvent("Open_folder",folder,email));
                mContext.startActivity(intent);

            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull FolderAdapter.FolderViewHolder holder, final int position) {
        folder = mFolders.get(position);
        Log.d("etFolder_id","d"+folder.getFolder_id());
        FolderService folderService = new FolderService(mContext);
        holder.folder_name.setText(folder.getFolder_name());
        holder.message_count.setText(String.valueOf(folderService.queryFolderMessageCount(folder.getFolder_id(),email.getEmail_id())));
        holder.datetime.setText("最近修改时间: "+folder.getDatetime().substring(5, 16));

    }

    @Override
    public int getItemCount() {
        return mFolders.size();
    }
    static class FolderViewHolder extends RecyclerView.ViewHolder{
        TextView folder_name,message_count,datetime;
        LinearLayout item;

        public FolderViewHolder(@NonNull View itemView) {
            super(itemView);

            folder_name = itemView.findViewById(R.id.folder_name);
            message_count = itemView.findViewById(R.id.folder_message_count);
            datetime = itemView.findViewById(R.id.folder_datetime);
            item = itemView.findViewById(R.id.folder_item);
        }
    }

}
