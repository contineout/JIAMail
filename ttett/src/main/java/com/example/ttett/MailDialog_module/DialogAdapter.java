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
import com.example.ttett.R;
import com.example.ttett.util.CircleTextImage.CircleTextImage;
import com.example.ttett.util.charsort.SortUtils;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DialogAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private LayoutInflater mLayoutInflater;
    private List<Contact> mContacts ;
    private Context mContext;

    public DialogAdapter(Context context, List<Contact> contacts){
        this.mContext = context;
        this.mContacts = contacts;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(mContext == null){
            mContext = parent.getContext();
        }
        if (viewType == SortUtils.ITEM_TYPE.ITEM_TYPE_CHARACTER.ordinal()) {
            return new CharacterHolder(mLayoutInflater.inflate(R.layout.character_item,parent,false));
        } else {
            return new ContactViewHolder(mLayoutInflater.inflate(R.layout.dialog_rv_item, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof CharacterHolder) {
            ((CharacterHolder) holder).mTextView.setText(mContacts.get(position).getName());
        } else if (holder instanceof ContactViewHolder) {
            Contact contact = mContacts.get(position);
            ((ContactViewHolder) holder).dialog_name.setText(contact.getName());
            ((ContactViewHolder) holder).dialog_mail.setText(contact.getEmail());
            ((ContactViewHolder) holder).dialog_icon.setText4CircleImage(contact.getName().substring(0,1));
            try{
                ((ContactViewHolder) holder).dialog_icon.setCircleColor(contact.getAvatar_color());
            }catch (Exception ignored){
            }
            ((ContactViewHolder) holder).dialog_date.setText(contact.getLast_date());
            ((ContactViewHolder) holder).dialog_item.setOnClickListener(new View.OnClickListener() {
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
        }
    }

    @Override
    public int getItemViewType(int position) {
        return mContacts.get(position).getmType();
    }

    @Override
    public int getItemCount() {
        return mContacts.size();
    }

    public static class CharacterHolder extends RecyclerView.ViewHolder {
        TextView mTextView;

        CharacterHolder(@NonNull View view) {
            super(view);
            mTextView =  view.findViewById(R.id.character);
        }
    }


    public static class ContactViewHolder extends RecyclerView.ViewHolder{
        TextView dialog_name,dialog_date,dialog_mail;
        CircleTextImage dialog_icon;
        LinearLayout dialog_item;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);

            dialog_icon = itemView.findViewById(R.id.dialog_Iv);
            dialog_item = itemView.findViewById(R.id.dialog_item);
            dialog_name = itemView.findViewById(R.id.dialog_name);
            dialog_date = itemView.findViewById(R.id.dialog_date);
            dialog_mail = itemView.findViewById(R.id.dialog_mail);
        }
    }

    public void setContact(List<Contact> contacts) {
        this.mContacts = contacts;
        notifyDataSetChanged();
    }

    public int getScrollPosition(String character) {
        if (SortUtils.characterList.contains(character)) {
            for (int i = 0; i < mContacts.size(); i++) {
                if (mContacts.get(i).getName().equals(character)) {
                    return i;
                }
            }
        }
        return -1; // -1不会滑动
    }
}
