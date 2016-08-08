package com.sobey.tvcust.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sobey.common.utils.TimeUtil;
import com.sobey.tvcust.R;
import com.sobey.tvcust.common.AppData;
import com.sobey.tvcust.entity.MsgSys;
import com.sobey.tvcust.entity.Order;
import com.sobey.tvcust.entity.User;
import com.sobey.tvcust.interfaces.OnRecycleItemClickListener;

import java.util.Date;
import java.util.List;


public class RecycleAdapterMsgSys extends RecyclerView.Adapter<RecycleAdapterMsgSys.Holder> {

    private Context context;
    private int src;
    private List<MsgSys> results;
    private User user;

    public List<MsgSys> getResults() {
        return results;
    }

    public RecycleAdapterMsgSys(Context context, int src, List<MsgSys> results) {
        this.context = context;
        this.src = src;
        this.results = results;
        this.user = AppData.App.getUser();
    }

    @Override
    public RecycleAdapterMsgSys.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
          return new Holder(LayoutInflater.from(parent.getContext()).inflate(src, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecycleAdapterMsgSys.Holder holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener!=null) listener.onItemClick(holder);
            }
        });

        MsgSys msg = results.get(holder.getLayoutPosition());

        holder.text_title.setText(msg.getTitle());
        holder.text_content.setText(msg.getBrief());
        holder.text_time.setText(TimeUtil.getTimeFor("yyyy-MM-dd  HH:mm", new Date(msg.getCreateTime())));
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        public TextView text_title;
        public TextView text_content;
        public TextView text_time;

        public Holder(View itemView) {
            super(itemView);
            text_title = (TextView) itemView.findViewById(R.id.text_ordersys_title);
            text_content = (TextView) itemView.findViewById(R.id.text_ordersys_content);
            text_time = (TextView) itemView.findViewById(R.id.text_ordersys_time);
        }
    }

    private OnRecycleItemClickListener listener;
    public void setOnItemClickListener(OnRecycleItemClickListener listener){
        this.listener = listener;
    }
}
