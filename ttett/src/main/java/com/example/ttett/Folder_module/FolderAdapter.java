package com.example.ttett.Folder_module;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.example.ttett.Entity.Email;
import com.example.ttett.Entity.Folder;
import com.example.ttett.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

public class FolderAdapter extends RecyclerSwipeAdapter<FolderAdapter.FolderViewHolder> {

    private List<Folder> mFolders ;
    private Context mContext;
    public Email email;
    private FragmentManager fragmentManager;

    public FolderAdapter(Context context, List<Folder> folders, Email email, FragmentManager fragmentManager){
        this.mContext = context;
        this.mFolders = folders;
        this.email = email;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public FolderAdapter.FolderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.folder_rv_item,parent,false);
        return new FolderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FolderAdapter.FolderViewHolder holder, final int position) {
        final Folder folder = mFolders.get(position);
        Log.d("etFolder_id","d"+folder.getFolder_id());
        FolderService folderService = new FolderService(mContext);
        holder.folder_name.setText(folder.getFolder_name());
        holder.message_count.setText(String.valueOf(folderService.queryFolderMessageCount(folder.getFolder_id(),email.getEmail_id())));
        holder.datetime.setText("最近修改时间: "+folder.getDatetime().substring(5, 16));
        holder.item.addDrag(SwipeLayout.DragEdge.Right, holder.item.findViewById(R.id.bottom_wrapper));
        holder.item.findViewById(R.id.contact_swipe_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FolderDeleteFragment folderDeleteFragment = new FolderDeleteFragment();
                folderDeleteFragment.show(fragmentManager,"folderDeleteFragment");
                Bundle bundle = new Bundle();
                bundle.putInt("id",folder.getFolder_id());
                bundle.putParcelable("email",email);
                folderDeleteFragment.setArguments(bundle);
            }
        });
         holder.item.findViewById(R.id.contact_swipe_update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FolderUpdateFragment folderDialogFragment = new FolderUpdateFragment();
                folderDialogFragment.show(fragmentManager,"folderUpdateFragment");
                Bundle bundle = new Bundle();
                bundle.putInt("id",folder.getFolder_id());
                folderDialogFragment.setArguments(bundle);
            }
        });

        holder.item.getSurfaceView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, OpenFolderActivity.class);
                intent.putExtra("folder",folder);
                intent.putExtra("email",email);
                mContext.startActivity(intent);
            }
        });
    }



    @Override
    public int getItemCount() {
        return mFolders.size();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return 0;
    }

    static class FolderViewHolder extends RecyclerView.ViewHolder{
        TextView folder_name,message_count,datetime;
        SwipeLayout item;

        public FolderViewHolder(@NonNull View itemView) {
            super(itemView);

            folder_name = itemView.findViewById(R.id.folder_name);
            message_count = itemView.findViewById(R.id.folder_message_count);
            datetime = itemView.findViewById(R.id.folder_datetime);
            item = itemView.findViewById(R.id.folder_item);
        }
    }

}
