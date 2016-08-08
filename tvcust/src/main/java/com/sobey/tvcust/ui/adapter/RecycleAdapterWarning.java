package com.sobey.tvcust.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sobey.common.utils.TimeUtil;
import com.sobey.tvcust.R;
import com.sobey.tvcust.entity.Warning;
import com.sobey.tvcust.interfaces.OnRecycleItemClickListener;

import java.util.Date;
import java.util.List;


public class RecycleAdapterWarning extends RecyclerView.Adapter<RecycleAdapterWarning.Holder> {

    private Context context;
    private int src;
    private List<Warning> results;

    public List<Warning> getResults() {
        return results;
    }

    public RecycleAdapterWarning(Context context, int src, List<Warning> results) {
        this.context = context;
        this.src = src;
        this.results = results;
    }

    @Override
    public RecycleAdapterWarning.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
          return new Holder(LayoutInflater.from(parent.getContext()).inflate(src, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecycleAdapterWarning.Holder holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener!=null) listener.onItemClick(holder);
            }
        });

        Warning warning = results.get(holder.getLayoutPosition());

        holder.text_name.setText(warning.getHostName());
        holder.text_ip.setText(warning.getInterIPAddress());
        holder.text_info.setText(warning.getReason());
        holder.text_time.setText(TimeUtil.getTimeFor("yyyy.MM.dd HH:mm", new Date(warning.getTime())));
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        public TextView text_name;
        public TextView text_ip;
        public TextView text_info;
        public TextView text_time;

        public Holder(View itemView) {
            super(itemView);
            text_name = (TextView) itemView.findViewById(R.id.text_item_device_name);
            text_ip = (TextView) itemView.findViewById(R.id.text_item_device_ip);
            text_info = (TextView) itemView.findViewById(R.id.text_item_home_qw_info);
            text_time = (TextView) itemView.findViewById(R.id.text_item_home_qw_time);
        }
    }

    private OnRecycleItemClickListener listener;
    public void setOnItemClickListener(OnRecycleItemClickListener listener){
        this.listener = listener;
    }
}
