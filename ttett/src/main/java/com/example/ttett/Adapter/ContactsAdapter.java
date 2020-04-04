package com.example.ttett.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.example.ttett.Entity.Contact;
import com.example.ttett.R;
import com.example.ttett.SimpleContactActivity;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ContactsAdapter extends RecyclerSwipeAdapter<ContactsAdapter.ContactViewHolder> {

    private List<Contact> mContacts ;
    private Context mContext;

    public ContactsAdapter(Context context,List<Contact> contacts){
        this.mContext = context;
        this.mContacts = contacts;
    }

    @NonNull
    @Override
    public ContactsAdapter.ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_rv_item,parent,false);
        final ContactsAdapter.ContactViewHolder holder = new ContactsAdapter.ContactViewHolder(view);
        holder.contact_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Contact contact = mContacts.get(position);
                Intent intent = new Intent(mContext, SimpleContactActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("contact",contact);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ContactsAdapter.ContactViewHolder holder, int position) {
        Contact contact = mContacts.get(position);
        holder.contact_item.setShowMode(SwipeLayout.ShowMode.LayDown);
        holder.contact_name.setText(contact.getName());
        holder.contact_email.setText(contact.getEmail());

    }

    @Override
    public int getItemCount() {
        return mContacts.size();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return position;
    }

    static class ContactViewHolder extends RecyclerView.ViewHolder{
        TextView contact_name,contact_email;
        SwipeLayout contact_item;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);

            contact_item = itemView.findViewById(R.id.contact_item);
            contact_name = itemView.findViewById(R.id.contact_name);
            contact_email = itemView.findViewById(R.id.contact_email);
        }
    }

    public void setContact(List<Contact> contacts) {
        this.mContacts = contacts;
        notifyDataSetChanged();
    }
}
