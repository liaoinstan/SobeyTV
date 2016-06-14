package com.sobey.tvcust.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sobey.tvcust.R;
import com.sobey.tvcust.entity.TestEntity;

import java.util.List;


public class RecycleAdapterOrderTrack extends RecyclerView.Adapter<RecycleAdapterOrderTrack.Holder> {
    private Context context;
    private int src;
    private List<TestEntity> results;

    public RecycleAdapterOrderTrack(Context context, int src, List<TestEntity> results) {
        this.context = context;
        this.src = src;
        this.results = results;
    }

    @Override
    public RecycleAdapterOrderTrack.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
          return new Holder(LayoutInflater.from(parent.getContext()).inflate(src, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecycleAdapterOrderTrack.Holder holder, int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener!=null) listener.onItemClick(holder);
            }
        });

        //设置小球和线
        if (results.size()==1){
            //只有一项
            holder.line_top.setVisibility(View.INVISIBLE);
            holder.img_oval.setImageResource(R.drawable.shape_oval_track_hot);
            holder.line_bottom.setVisibility(View.INVISIBLE);
        }else {
            if (position == 0) {
                //第一项
                holder.line_top.setVisibility(View.INVISIBLE);
                holder.img_oval.setImageResource(R.drawable.shape_oval_track_hot);
                holder.line_bottom.setVisibility(View.VISIBLE);
            }else if (position == results.size() - 1) {
                //最后一项
                holder.line_top.setVisibility(View.VISIBLE);
                holder.img_oval.setImageResource(R.drawable.shape_oval_track);
                holder.line_bottom.setVisibility(View.INVISIBLE);
            } else {
                //中间项
                holder.line_top.setVisibility(View.VISIBLE);
                holder.img_oval.setImageResource(R.drawable.shape_oval_track);
                holder.line_bottom.setVisibility(View.VISIBLE);
            }
        }

        //设置内容
        holder.text_info.setText(results.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        private View line_top;
        private View line_bottom;
        private ImageView img_oval;
        private TextView text_info;
        private TextView text_time;

        public Holder(View itemView) {
            super(itemView);
            line_top =  itemView.findViewById(R.id.v_item_track_line_top);
            line_bottom =  itemView.findViewById(R.id.v_item_track_line_bottom);
            img_oval = (ImageView) itemView.findViewById(R.id.img_item_track_oval);
            text_info = (TextView) itemView.findViewById(R.id.text_item_track_info);
            text_time = (TextView) itemView.findViewById(R.id.text_item_track_time);
        }
    }

    private OnRecycleItemClickListener listener;
    public void setOnItemClickListener(OnRecycleItemClickListener listener){
        this.listener = listener;
    }
}
