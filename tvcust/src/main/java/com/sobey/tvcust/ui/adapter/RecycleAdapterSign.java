package com.sobey.tvcust.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sobey.tvcust.R;
import com.sobey.tvcust.entity.TestEntity;
import com.sobey.tvcust.ui.activity.OrderDetailActivity;

import java.util.List;


public class RecycleAdapterSign extends RecyclerView.Adapter<RecycleAdapterSign.Holder> {

    private Context context;
    private int src;
    private List<TestEntity> results;

    public List<TestEntity> getResults() {
        return results;
    }

    public RecycleAdapterSign(Context context, int src, List<TestEntity> results) {
        this.context = context;
        this.src = src;
        this.results = results;
    }

    @Override
    public RecycleAdapterSign.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
          return new Holder(LayoutInflater.from(parent.getContext()).inflate(src, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecycleAdapterSign.Holder holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener!=null) listener.onItemClick(holder);
            }
        });
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        public Holder(View itemView) {
            super(itemView);
        }
    }

    private OnRecycleItemClickListener listener;
    public void setOnItemClickListener(OnRecycleItemClickListener listener){
        this.listener = listener;
    }
}
