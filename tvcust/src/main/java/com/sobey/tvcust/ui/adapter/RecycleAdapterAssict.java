package com.sobey.tvcust.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sobey.tvcust.R;
import com.sobey.tvcust.entity.TestEntity;

import java.util.List;


public class RecycleAdapterAssict extends RecyclerView.Adapter<RecycleAdapterAssict.Holder> {

    private Context context;
    private int src;
    private List<TestEntity> results;
    private int selectItem = -1;
    private int lastSelect;

    public List<TestEntity> getResults() {
        return results;
    }

    public RecycleAdapterAssict(Context context, int src, List<TestEntity> results) {
        this.context = context;
        this.src = src;
        this.results = results;
    }

    @Override
    public RecycleAdapterAssict.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
          return new Holder(LayoutInflater.from(parent.getContext()).inflate(src, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecycleAdapterAssict.Holder holder, final int position) {
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
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    protected class Holder extends RecyclerView.ViewHolder {
        private View img_select;
        public Holder(View itemView) {
            super(itemView);
            img_select = itemView.findViewById(R.id.img_orderallocate_select);
        }
    }

    private OnRecycleItemClickListener listener;
    public void setOnItemClickListener(OnRecycleItemClickListener listener){
        this.listener = listener;
    }
}
