package com.example.ttett.Contact_module;

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
import com.example.ttett.util.CircleTextImage.CircleTextImage;
import com.example.ttett.util.ToastUtil;
import com.example.ttett.util.charsort.SortUtils;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static com.example.ttett.util.charsort.SortUtils.characterList;

public class ContactsAdapter extends RecyclerSwipeAdapter<RecyclerView.ViewHolder> {

    private LayoutInflater mLayoutInflater;
    private List<Contact> mContacts ;
    private Context mContext;

    public ContactsAdapter(Context context,List<Contact> contacts){
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
            return new ContactsAdapter.CharacterHolder(mLayoutInflater.inflate(R.layout.character_item,parent,false));
        } else {
            return new ContactsAdapter.ContactViewHolder(mLayoutInflater.inflate(R.layout.contact_rv_item, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof CharacterHolder) {
            ((CharacterHolder) holder).mTextView.setText(mContacts.get(position).getName());
        } else if (holder instanceof ContactViewHolder) {
            final Contact contact = mContacts.get(position);
            ((ContactViewHolder) holder).contact_item.setShowMode(SwipeLayout.ShowMode.PullOut);
            ((ContactViewHolder) holder).contact_name.setText(contact.getName());
            ((ContactViewHolder) holder).contact_email.setText(contact.getEmail());
            ((ContactViewHolder) holder).contact_icon.setText4CircleImage(contact.getName().substring(0,1));
            ((ContactViewHolder) holder).contact_icon.setCircleColor(contact.getAvatar_color());
            ((ContactViewHolder) holder).contact_item.addDrag(SwipeLayout.DragEdge.Right, ((ContactViewHolder) holder).contact_item.findViewById(R.id.bottom_wrapper));
            ((ContactViewHolder) holder).contact_item.findViewById(R.id.contact_swipe_delete).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ContactService contactService = new ContactService(mContext);
                    if(contactService.deleteContact(contact.getContact_id()));{
                        ToastUtil.showTextToas(mContext,"联系人"+contact.getName()+"删除成功");
                    }
                }
            });
            ((ContactViewHolder) holder).contact_item.findViewById(R.id.contact_swipe_send).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtil.showTextToas(mContext,"发送");
                }
            });

            ((ContactViewHolder) holder).contact_item.getSurfaceView().setOnClickListener(new View.OnClickListener() {
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
        }
    }

    /**
     * 增加数据
     */
    public void addData(int position) {
        mContacts.add(0,mContacts.get(mContacts.size()-1));
        notifyItemInserted(position);
    }

    /**
     * 移除数据
     */
    public void removeData(int position) {
        notifyItemRemoved(position);
        mContacts.remove(position);
    }

    @Override
    public int getItemViewType(int position) {
        return mContacts.get(position).getmType();
    }

    @Override
    public int getItemCount() {
        return mContacts.size();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return position;
    }

    public static class CharacterHolder extends RecyclerView.ViewHolder {
        TextView mTextView;

        CharacterHolder(@NonNull View view) {
            super(view);
            mTextView =  view.findViewById(R.id.character);
        }
    }


    static class ContactViewHolder extends RecyclerView.ViewHolder{
        TextView contact_name,contact_email;
        CircleTextImage contact_icon;
        SwipeLayout contact_item;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);

            contact_item = itemView.findViewById(R.id.contact_item);
            contact_name = itemView.findViewById(R.id.contact_name);
            contact_email = itemView.findViewById(R.id.contact_email);
            contact_icon = itemView.findViewById(R.id.contacts_Iv);
        }
    }

    public void setContact(List<Contact> contacts) {
        this.mContacts = contacts;
        notifyDataSetChanged();
    }

    public int getScrollPosition(String character) {
        if (characterList.contains(character)) {
            for (int i = 0; i < mContacts.size(); i++) {
                if (mContacts.get(i).getName().equals(character)) {
                    return i;
                }
            }
        }
        return -1; // -1不会滑动
    }
}
