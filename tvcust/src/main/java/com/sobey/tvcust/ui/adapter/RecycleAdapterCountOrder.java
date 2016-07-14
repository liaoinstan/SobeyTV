package com.sobey.tvcust.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sobey.tvcust.R;
import com.sobey.tvcust.entity.CountEntity;
import com.sobey.tvcust.entity.TestEntity;
import com.sobey.tvcust.interfaces.OnRecycleItemClickListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class RecycleAdapterCountOrder extends RecyclerView.Adapter<RecycleAdapterCountOrder.Holder> {

    private Context context;
    private int src;
    private List<CountEntity> results;

    public List<CountEntity> getResults() {
        return results;
    }

    public RecycleAdapterCountOrder(Context context, int src, List<CountEntity> results) {
        this.context = context;
        this.src = src;
        this.results = results;
    }

    @Override
    public RecycleAdapterCountOrder.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
          return new Holder(LayoutInflater.from(parent.getContext()).inflate(src, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecycleAdapterCountOrder.Holder holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener!=null) listener.onItemClick(holder);
            }
        });
        CountEntity countEntity = results.get(holder.getLayoutPosition());
        holder.text_count_name.setText(countEntity.getName());
        holder.text_count_value.setText(countEntity.getValue()+"");
        holder.img_count_dot.setImageDrawable(new ColorDrawable(countEntity.getColor()));
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        public TextView text_count_name;
        public TextView text_count_value;
        public CircleImageView img_count_dot;
        public Holder(View itemView) {
            super(itemView);
            text_count_name = (TextView) itemView.findViewById(R.id.text_count_name);
            text_count_value = (TextView) itemView.findViewById(R.id.text_count_value);
            img_count_dot = (CircleImageView) itemView.findViewById(R.id.img_count_dot);
        }
    }

    private OnRecycleItemClickListener listener;
    public void setOnItemClickListener(OnRecycleItemClickListener listener){
        this.listener = listener;
    }
}
