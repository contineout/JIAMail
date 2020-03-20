package com.example.ttett.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ttett.Entity.Email;
import com.example.ttett.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class EmailAdapter extends RecyclerView.Adapter<EmailAdapter.ViewHolder> {
    private List<Email> mEmails;
    private Context mContext;

    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.inbox_top_iv);
            textView = itemView.findViewById(R.id.inbox_top_tv);
        }
    }

    public EmailAdapter(Context context, List<Email> emails){
        mEmails = emails;
        mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.inbox_top_menu,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Email email = mEmails.get(position);

    }


    @Override
    public int getItemCount() {
        return mEmails.size();
    }


}
