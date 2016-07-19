package com.sobey.tvcust.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sobey.common.utils.TimeUtil;
import com.sobey.tvcust.R;
import com.sobey.tvcust.entity.Sign;
import com.sobey.tvcust.entity.TestEntity;
import com.sobey.tvcust.interfaces.OnRecycleItemClickListener;

import java.util.Date;
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
        Sign sign = results.get(holder.getLayoutPosition());

        holder.text_gign_item_grades.setText("获得"+sign.getGrades()+"积分");
        holder.text_gign_item_time.setText(TimeUtil.getTimeFor("yyyy-MM-dd  HH:mm",new Date(sign.getTime())));
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        private TextView text_gign_item_grades;
        private TextView text_gign_item_time;

        public Holder(View itemView) {
            super(itemView);
            text_gign_item_grades = (TextView) itemView.findViewById(R.id.text_gign_item_grades);
            text_gign_item_time = (TextView) itemView.findViewById(R.id.text_gign_item_time);
        }
    }

    private OnRecycleItemClickListener listener;
    public void setOnItemClickListener(OnRecycleItemClickListener listener){
        this.listener = listener;
    }
}
