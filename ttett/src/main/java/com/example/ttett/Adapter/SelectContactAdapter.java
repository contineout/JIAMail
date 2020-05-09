package com.example.ttett.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ttett.Entity.Contact;
import com.example.ttett.R;
import com.example.ttett.bean.MessageEvent;
import com.example.ttett.util.CircleTextImage.CircleTextImage;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SelectContactAdapter extends RecyclerView.Adapter<SelectContactAdapter.InboxViewHolder> {
    private List<Contact> contacts ;
    private Context mContext;
    private Map<Integer, Boolean> checkStatus;


    public SelectContactAdapter(Context context, List<Contact> contacts, Map<Integer, Boolean> checkStatus){
        this.mContext = context;
        this.contacts = contacts;
        this.checkStatus = checkStatus;
    }


    @NonNull
    @Override
    public SelectContactAdapter.InboxViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.select_contact_item,parent,false);
        return new InboxViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SelectContactAdapter.InboxViewHolder holder, final int position) {
        final Contact contact = contacts.get(position);
        holder.contact_name.setText(contact.getName());
        holder.contact_email.setText(contact.getEmail());
        holder.contact_icon.setText4CircleImage(contact.getName().substring(0,1));
        holder.contact_icon.setCircleColor(contact.getAvatar_color());
        holder.checkBox.setChecked(checkStatus.get(position));
        holder.checkBox.setOnCheckedChangeListener(null);
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkStatus.put(position,isChecked);
                EventBus.getDefault().post(new MessageEvent("contact_check_status",checkStatus));
            }
        });
        holder.attachment_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkStatus.get(position)){
                    checkStatus.put(position,false);
                    holder.checkBox.setChecked(checkStatus.get(position));

                }else {
                    checkStatus.put(position,true);
                    holder.checkBox.setChecked(checkStatus.get(position));
                }
                EventBus.getDefault().post(new MessageEvent("contact_check_status",checkStatus));
            }
        });
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    static class InboxViewHolder extends RecyclerView.ViewHolder{
        TextView contact_name,contact_email;
        CircleTextImage contact_icon;
        LinearLayout attachment_item;
        CheckBox checkBox;

        public InboxViewHolder(@NonNull View itemView) {
            super(itemView);

            contact_name = itemView.findViewById(R.id.contact_name);
            contact_email = itemView.findViewById(R.id.contact_email);
            contact_icon = itemView.findViewById(R.id.contacts_Iv);
            checkBox = itemView.findViewById(R.id.contact_cb);
            attachment_item = itemView.findViewById(R.id.contact_item);
        }
    }

}
