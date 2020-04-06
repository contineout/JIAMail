package com.example.ttett.MailDialog_module;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ttett.Entity.Contact;
import com.example.ttett.MailDialogActivity;
import com.example.ttett.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class DialogAdapter extends RecyclerView.Adapter<DialogAdapter.ContactViewHolder> {

    private List<Contact> mContacts ;
    private Context mContext;

    public DialogAdapter(Context context, List<Contact> contacts){
        this.mContext = context;
        this.mContacts = contacts;
    }

    @NonNull
    @Override
    public DialogAdapter.ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dialog_rv_item,parent,false);
        final DialogAdapter.ContactViewHolder holder = new DialogAdapter.ContactViewHolder(view);
        holder.dialog_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Contact contact = mContacts.get(position);
                Intent intent = new Intent(mContext, MailDialogActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("contact",contact);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull DialogAdapter.ContactViewHolder holder, int position) {
        Contact contact = mContacts.get(position);
        holder.dialog_name.setText(contact.getName());
    }

    @Override
    public int getItemCount() {
        return mContacts.size();
    }


    static class ContactViewHolder extends RecyclerView.ViewHolder{
        TextView dialog_name,dialog_date;
        CircleImageView dialog_icon;
        LinearLayout dialog_item;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);

            dialog_icon = itemView.findViewById(R.id.dialog_Iv);
            dialog_item = itemView.findViewById(R.id.dialog_item);
            dialog_name = itemView.findViewById(R.id.dialog_name);
            dialog_date = itemView.findViewById(R.id.dialog_date);
        }
    }

    public void setContact(List<Contact> contacts) {
        this.mContacts = contacts;
        notifyDataSetChanged();
    }
}
