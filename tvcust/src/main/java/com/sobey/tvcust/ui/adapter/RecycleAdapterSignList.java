package com.sobey.tvcust.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sobey.tvcust.entity.Sign;
import com.sobey.tvcust.entity.TestEntity;
import com.sobey.tvcust.interfaces.OnRecycleItemClickListener;

import java.util.List;


public class RecycleAdapterSignList extends RecyclerView.Adapter<RecycleAdapterSignList.Holder> {

    private Context context;
    private int src;
    private List<Sign> results;

    public List<Sign> getResults() {
        return results;
    }

    public RecycleAdapterSignList(Context context, int src, List<Sign> results) {
        this.context = context;
        this.src = src;
        this.results = results;
    }

    @Override
    public RecycleAdapterSignList.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
          return new Holder(LayoutInflater.from(parent.getContext()).inflate(src, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecycleAdapterSignList.Holder holder, final int position) {
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
