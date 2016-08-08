package com.sobey.tvcust.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sobey.common.utils.TimeUtil;
import com.sobey.tvcust.R;
import com.sobey.tvcust.common.AppData;
import com.sobey.tvcust.entity.MsgOrder;
import com.sobey.tvcust.entity.MsgSys;
import com.sobey.tvcust.entity.Order;
import com.sobey.tvcust.entity.User;
import com.sobey.tvcust.interfaces.OnRecycleItemClickListener;
import com.sobey.tvcust.ui.activity.OrderDetailActivity;
import com.sobey.tvcust.ui.activity.OrderProgActivity;
import com.sobey.tvcust.ui.activity.WebActivity;

import java.util.Date;
import java.util.List;


public class RecycleAdapterMsgOrder extends RecyclerView.Adapter<RecycleAdapterMsgOrder.Holder> {

    private Context context;
    private int src;
    private List<MsgOrder> results;
    private User user;

    public List<MsgOrder> getResults() {
        return results;
    }

    public RecycleAdapterMsgOrder(Context context, int src, List<MsgOrder> results) {
        this.context = context;
        this.src = src;
        this.results = results;
        this.user = AppData.App.getUser();
    }

    @Override
    public RecycleAdapterMsgOrder.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
          return new Holder(LayoutInflater.from(parent.getContext()).inflate(src, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecycleAdapterMsgOrder.Holder holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener!=null) listener.onItemClick(holder);
            }
        });
        final MsgOrder msg = results.get(holder.getLayoutPosition());
        holder.lay_godetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppData.App.getUser().getRoleType()==User.ROLE_COMMOM){
                    Intent intent = new Intent(context, OrderProgActivity.class);
                    intent.putExtra("orderId", msg.getOrderId());
                    context.startActivity(intent);
                }else {
                    Intent intent = new Intent(context, OrderDetailActivity.class);
                    intent.putExtra("id", msg.getOrderId());
                    context.startActivity(intent);
                }
            }
        });

        holder.text_num.setText("订单编号："+msg.getOrderNumber());
        holder.text_prog.setText("订单进度："+msg.getInfoMsg());
        holder.text_time.setText("更新时间："+TimeUtil.getTimeFor("yyyy-MM-dd  HH:mm", new Date(msg.getCreateTime())));
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        public TextView text_num;
        public TextView text_prog;
        public TextView text_time;
        public View lay_godetail;

        public Holder(View itemView) {
            super(itemView);
            text_num = (TextView) itemView.findViewById(R.id.text_ordermsg_num);
            text_prog = (TextView) itemView.findViewById(R.id.text_ordermsg_prog);
            text_time = (TextView) itemView.findViewById(R.id.text_ordermsg_time);
            lay_godetail = itemView.findViewById(R.id.lay_ordermsg_godetail);
        }
    }

    private OnRecycleItemClickListener listener;
    public void setOnItemClickListener(OnRecycleItemClickListener listener){
        this.listener = listener;
    }
}
