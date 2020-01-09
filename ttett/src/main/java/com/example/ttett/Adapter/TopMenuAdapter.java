package com.example.ttett.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ttett.R;
import com.example.ttett.bean.Topmenu;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TopMenuAdapter extends RecyclerView.Adapter<TopMenuAdapter.ViewHolder> {
    private List<Topmenu> mTopmenus;
    private Context mContext;
    private OnClickItemListener mListener;
    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.inbox_top_iv);
            textView = itemView.findViewById(R.id.inbox_top_tv);
        }
    }
    public TopMenuAdapter(Context context,List<Topmenu> topmenus,OnClickItemListener listener){
        mTopmenus = topmenus;
        mContext = context;
        mListener = listener;
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
        Topmenu topmenu = mTopmenus.get(position);
        holder.imageView.setImageResource(topmenu.getImageId());
        holder.textView.setText(topmenu.getText());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onClick(position);
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemCount() {
        return mTopmenus.size();
    }

    public interface OnClickItemListener{
        void onClick(int position);
    }
}
