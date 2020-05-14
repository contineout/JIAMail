package com.example.ttett.MailDialog_module;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.ttett.Entity.EmailMessage;
import com.example.ttett.R;
import com.example.ttett.util.CircleTextImage.CircleTextImage;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cn.qzb.richeditor.RichEditor;

public class MailDialogAdapter extends RecyclerView.Adapter<MailDialogAdapter.ContactViewHolder> {

    private List<EmailMessage> messages ;
    private Context mContext;

    public MailDialogAdapter(Context context, List<EmailMessage> emailMessages){
        this.mContext = context;
        this.messages = emailMessages;
    }


    @NonNull
    @Override
    public MailDialogAdapter.ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dialog_mail_rv_item,parent,false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MailDialogAdapter.ContactViewHolder holder, final int position) {
        EmailMessage message = messages.get(position);
        final LinearLayoutManager manager = null;

        try {
            if (!message.getFrom().isEmpty() && !message.getTo().isEmpty()) {
                String[] from = message.getFrom().split("[<>]");
                String[] to = message.getTo().split("[<>]");
                holder.tvFromId.setText(from[0]);
                holder.tvFromMail.setText(from[1]);
                holder.tvToId.setText(to[0]);
                holder.tvToMail.setText(to[1]);
                if (from[0].equals("")) {
                    holder.dialog_name.setText(from[1]);
                    holder.dialog_icon .setText4CircleImage(from[1]);
                }else{
                    holder.dialog_name.setText(from[0]);
                    holder.dialog_icon .setText4CircleImage(from[0]);
                }

            }
        }catch (Exception ignored){
        }

        holder.dialog_date.setText(message.getSendDate());
        holder.dialog_content.setText(message.getSubject());
        try{
            holder.dialog_icon.setCircleColor(message.getAvatar_color());
        }catch (Exception ignored){
        }



        holder.tvSubject.setText(message.getSubject());
        holder.tvContent.setHtml(message.getContent());
        holder.tvDate.setText("最近联系: "+message.getSendDate().substring(5, 16));

//        if(visibleStatus.get(position)){

//        }
        holder.dialog_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            holder.dialog_out.setVisibility(View.GONE);
            holder.dialog_in.setVisibility(View.VISIBLE);
            }
        });

        holder.dialog_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.dialog_out.setVisibility(View.VISIBLE);
                holder.dialog_in.setVisibility(View.GONE);
            }
        });
    }


    @Override
    public int getItemCount() {
        return messages.size();
    }

    static class ContactViewHolder extends RecyclerView.ViewHolder{
        RelativeLayout dialog_out;
        LinearLayout dialog_in;
        TextView tvSubject;
        TextView tvFromId;
        TextView tvFromMail;
        TextView tvToId;
        TextView tvToMail;
        TextView tvDate;
        ImageView iv_mail;
        RichEditor tvContent;
        RecyclerView attachmentRv;
        TextView dialog_name,dialog_date,dialog_content;
        CircleTextImage dialog_icon;


        ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            dialog_name = itemView.findViewById(R.id.dialog_name);
            dialog_date = itemView.findViewById(R.id.dialog_date);
            dialog_content = itemView.findViewById(R.id.dialog_content);
            dialog_icon = itemView.findViewById(R.id.dialog_Iv);


            dialog_out = itemView.findViewById(R.id.dialog_out);
            dialog_in = itemView.findViewById(R.id.dialog_in);
            tvSubject = itemView.findViewById(R.id.mail_subject);
            tvFromId = itemView.findViewById(R.id.from_id);
            tvFromMail = itemView.findViewById(R.id.from_mail);
            tvToId = itemView.findViewById(R.id.to_id);
            tvToMail = itemView.findViewById(R.id.to_mail);
            tvDate = itemView.findViewById(R.id.mail_date);
            iv_mail = itemView.findViewById(R.id.mail_button);
            tvContent = itemView.findViewById(R.id.dialogWebView);
            attachmentRv = itemView.findViewById(R.id.mail_attachment_rv);
            dialog_out.setVisibility(View.VISIBLE);
            dialog_in.setVisibility(View.GONE);
            attachmentRv.setVisibility(View.GONE);
        }
    }

    private String getHtmlData(String bodyHTML) {
        String head = "<head>" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, user-scalable=no\"> " +
                "<style>img{max-width: 100%; width:auto; height:auto!important;}</style>" +
                "</head>";
        return "<html>" + head + "<body>" + bodyHTML + "</body></html>";
    }
}
