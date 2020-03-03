package com.example.ttett.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ttett.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class InboxAdapter extends RecyclerView.Adapter<InboxAdapter.InboxViewHolder> {

    private Context mContext;

    public InboxAdapter(Context context){
        this.mContext = context;
    }
    @NonNull
    @Override
    public InboxAdapter.InboxViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new InboxViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.inbox_rv_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull InboxAdapter.InboxViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 2;
    }

    static class InboxViewHolder extends RecyclerView.ViewHolder{
        CircleImageView Icon;
        TextView mName,mTime,mSubject,mContent;

        public InboxViewHolder(@NonNull View itemView) {
            super(itemView);

            Icon = itemView.findViewById(R.id.from_icon);
            mName = itemView.findViewById(R.id.from_name);
            mTime = itemView.findViewById(R.id.from_time);
            mSubject = itemView.findViewById(R.id.mail_subject);
            mContent = itemView.findViewById(R.id.mail_content);
        }
    }
}
