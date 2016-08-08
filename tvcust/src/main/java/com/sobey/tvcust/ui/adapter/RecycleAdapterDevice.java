package com.sobey.tvcust.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sobey.tvcust.R;
import com.sobey.tvcust.entity.Device;
import com.sobey.tvcust.entity.SBDevice;
import com.sobey.tvcust.interfaces.OnRecycleItemClickListener;

import java.util.List;


public class RecycleAdapterDevice extends RecyclerView.Adapter<RecycleAdapterDevice.Holder> {

    private Context context;
    private int src;
    private List<Device> results;

    public List<Device> getResults() {
        return results;
    }

    public RecycleAdapterDevice(Context context, int src, List<Device> results) {
        this.context = context;
        this.src = src;
        this.results = results;
    }

    @Override
    public RecycleAdapterDevice.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
          return new Holder(LayoutInflater.from(parent.getContext()).inflate(src, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecycleAdapterDevice.Holder holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener!=null) listener.onItemClick(holder);
            }
        });

        Device device = results.get(holder.getLayoutPosition());

        holder.text_name.setText(device.getHostName());
        holder.text_ip.setText(device.getInterIPAddress());
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        public TextView text_name;
        public TextView text_ip;

        public Holder(View itemView) {
            super(itemView);
            text_name = (TextView) itemView.findViewById(R.id.text_item_device_name);
            text_ip = (TextView) itemView.findViewById(R.id.text_item_device_ip);
        }
    }

    private OnRecycleItemClickListener listener;
    public void setOnItemClickListener(OnRecycleItemClickListener listener){
        this.listener = listener;
    }
}
