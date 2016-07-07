package com.sobey.tvcust.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sobey.tvcust.R;
import com.sobey.tvcust.entity.User;
import com.sobey.tvcust.interfaces.OnRecycleItemClickListener;

import java.util.List;


public class RecycleAdapterAssist extends RecyclerView.Adapter<RecycleAdapterAssist.Holder> {

    private Context context;
    private int src;
    private List<User> results;
    private int selectItem = -1;
    private int lastSelect;

    public List<User> getResults() {
        return results;
    }

    public RecycleAdapterAssist(Context context, int src, List<User> results) {
        this.context = context;
        this.src = src;
        this.results = results;
    }

    @Override
    public RecycleAdapterAssist.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
          return new Holder(LayoutInflater.from(parent.getContext()).inflate(src, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecycleAdapterAssist.Holder holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener!=null) listener.onItemClick(holder);
                lastSelect = selectItem;
                selectItem = position;
                notifyItemChanged(lastSelect);
                notifyItemChanged(selectItem);
            }
        });

        if (position == selectItem) {
            holder.img_select.setVisibility(View.VISIBLE);
        } else {
            holder.img_select.setVisibility(View.INVISIBLE);
        }

        int pos = holder.getLayoutPosition();

        Glide.with(context).load(results.get(pos).getAvatar()).placeholder(R.drawable.default_bk).crossFade().into(holder.img_header);
        holder.text_name.setText(results.get(pos).getRealName());
    }

    public User getSelectUser(){
        if (selectItem>=0) {
            return results.get(selectItem);
        }else {
            return null;
        }
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    protected class Holder extends RecyclerView.ViewHolder {
        private View img_select;
        private ImageView img_header;
        private TextView text_name;
        public Holder(View itemView) {
            super(itemView);
            img_select = itemView.findViewById(R.id.img_orderallocate_select);
            img_header = (ImageView) itemView.findViewById(R.id.img_orderallocate_header);
            text_name = (TextView) itemView.findViewById(R.id.text_orderallocate_name);
        }
    }

    private OnRecycleItemClickListener listener;
    public void setOnItemClickListener(OnRecycleItemClickListener listener){
        this.listener = listener;
    }
}
