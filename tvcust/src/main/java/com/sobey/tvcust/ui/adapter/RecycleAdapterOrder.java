package com.sobey.tvcust.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sobey.common.utils.TimeUtil;
import com.sobey.tvcust.R;
import com.sobey.tvcust.common.AppData;
import com.sobey.tvcust.common.OrderStatusHelper;
import com.sobey.tvcust.entity.Order;
import com.sobey.tvcust.entity.OrderCategory;
import com.sobey.tvcust.entity.User;
import com.sobey.tvcust.ui.activity.OrderAllocateActivity;
import com.sobey.tvcust.ui.activity.OrderDetailActivity;

import java.util.Date;
import java.util.List;


public class RecycleAdapterOrder extends RecyclerView.Adapter<RecycleAdapterOrder.Holder> {

    private Context context;
    private int src;
    private List<Order> results;
    private User user;

    public List<Order> getResults() {
        return results;
    }

    public RecycleAdapterOrder(Context context, int src, List<Order> results) {
        this.context = context;
        this.src = src;
        this.results = results;

        this.user = AppData.App.getUser();
    }

    @Override
    public RecycleAdapterOrder.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(src, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecycleAdapterOrder.Holder holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) listener.onItemClick(holder);
            }
        });
        holder.text_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                results.remove(holder.getLayoutPosition());
                notifyItemRemoved(holder.getLayoutPosition());
            }
        });
        holder.text_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, OrderDetailActivity.class);
                intent.putExtra("id",results.get(holder.getLayoutPosition()).getId());
                context.startActivity(intent);
            }
        });
        holder.text_allocate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, OrderAllocateActivity.class);
                context.startActivity(intent);
            }
        });

        Order order = results.get(holder.getLayoutPosition());
        OrderCategory category = order.getCategory();
//        holder.img_pic.setImageResource();
        holder.text_num.setText("订单编号：" + order.getOrderNumber());
        holder.text_time.setText(TimeUtil.getTimeFor("yyyy-MM-dd", new Date(order.getCreateTime())));
        holder.text_question.setText((category.getType() == 0 ? "软件问题：" : "硬件问题：") + category.getCategoryName());
        holder.text_status.setText(OrderStatusHelper.getStatusStr(user.getRoleType(), order));
        holder.img_pic.setImageResource(OrderStatusHelper.getStatusImgSrc(order));
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        public ImageView img_pic;
        public TextView text_num;
        public TextView text_time;
        public TextView text_question;
        public TextView text_status;

        public TextView text_cancle;
        public TextView text_go;
        public TextView text_allocate;

        public Holder(View itemView) {
            super(itemView);
            img_pic = (ImageView) itemView.findViewById(R.id.img_order_pic);
            text_num = (TextView) itemView.findViewById(R.id.text_order_num);
            text_time = (TextView) itemView.findViewById(R.id.text_order_time);
            text_question = (TextView) itemView.findViewById(R.id.text_order_question);
            text_status = (TextView) itemView.findViewById(R.id.text_order_status);

            text_cancle = (TextView) itemView.findViewById(R.id.text_order_cancle);
            text_go = (TextView) itemView.findViewById(R.id.text_order_go);
            text_allocate = (TextView) itemView.findViewById(R.id.text_order_allocate);
        }
    }

    private OnRecycleItemClickListener listener;

    public void setOnItemClickListener(OnRecycleItemClickListener listener) {
        this.listener = listener;
    }
}
