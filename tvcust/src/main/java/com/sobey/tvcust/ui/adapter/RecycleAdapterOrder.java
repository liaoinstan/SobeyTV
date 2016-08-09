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
import com.sobey.tvcust.interfaces.OnRecycleItemClickListener;
import com.sobey.tvcust.ui.activity.EvaActivity;
import com.sobey.tvcust.ui.activity.EvaDetailActivity;
import com.sobey.tvcust.ui.activity.OrderAllocateActivity;
import com.sobey.tvcust.ui.activity.OrderDetailActivity;
import com.sobey.tvcust.ui.activity.OrderProgActivity;
import com.sobey.tvcust.ui.activity.ReqDescribeActicity;
import com.sobey.tvcust.ui.activity.ReqDescribeOnlyActicity;

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
        final Order order = results.get(holder.getLayoutPosition());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) listener.onItemClick(holder);
            }
        });
        holder.text_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (okListener != null) okListener.onCancleClick(holder);
            }
        });
        holder.text_cui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (okListener != null) okListener.onCuiClick(holder);
            }
        });
        holder.text_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ReqDescribeOnlyActicity.class);
                intent.putExtra("orderId", order.getId());
                intent.putExtra("type", 0);
                intent.putExtra("title", "完成任务");
                context.startActivity(intent);
            }
        });
        holder.text_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(context, OrderDetailActivity.class);
//                intent.putExtra("id", order.getId());
//                intent.putExtra("order", order);
//                context.startActivity(intent);
                //新改动：这个按钮不再是跳转详情页而是进度页
                Intent intent = new Intent(context, OrderProgActivity.class);
                intent.putExtra("orderId", order.getId());
                context.startActivity(intent);
            }
        });
        holder.text_allocate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, OrderAllocateActivity.class);
                intent.putExtra("orderId", order.getId());
                intent.putExtra("userId", order.getUserId());
                intent.putExtra("categoryId", order.getCategory().getId());
                context.startActivity(intent);
            }
        });

        OrderCategory category = order.getCategory();
        holder.text_num.setText("订单编号：" + order.getOrderNumber());
        holder.text_time.setText(TimeUtil.getTimeFor("yyyy-MM-dd", new Date(order.getCreateTime())));
        holder.text_question.setText((category.getType() == 0 ? "软件问题：" : "硬件问题：") + category.getCategoryName());
        holder.text_status.setText(OrderStatusHelper.getStatusStr(user.getRoleType(), order));
        holder.img_pic.setImageResource(OrderStatusHelper.getStatusImgSrc(order));

        /////////////////////////////////////
        //根据状态和登录用户显示隐藏功能按钮
        /////////////////////////////////////

        //客服可以分配订单
        if (user.getRoleType() == User.ROLE_CUSTOMER) {
            if (order.getServiceCheck() == 1 && (order.getTscId() == null && order.getHeadTechId() == null) && order.getStatus() == Order.ORDER_UNDEAL) {
                //客服查看后 && 订单尚未分配 && 订单是未处理状态
                holder.text_allocate.setVisibility(View.VISIBLE);
            } else {
                holder.text_allocate.setVisibility(View.GONE);
            }
        } else {
            holder.text_allocate.setVisibility(View.GONE);
        }

        //是否需要查看评价和进行评价
        switch (OrderStatusHelper.getNeedEva(order,user.getRoleType())){
            case 0:
                holder.text_eva.setText("查看评价");
                holder.text_eva.setVisibility(View.VISIBLE);
                holder.text_eva.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setClass(context, EvaDetailActivity.class);
                        intent.putExtra("orderId", order.getId());
                        context.startActivity(intent);
                    }
                });
                break;
            case 1:
                holder.text_eva.setText("立即评价");
                holder.text_eva.setVisibility(View.VISIBLE);
                holder.text_eva.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setClass(context, EvaActivity.class);
                        intent.putExtra("orderId", order.getId());
                        context.startActivity(intent);
                    }
                });
                break;
            case 2:
                holder.text_eva.setVisibility(View.GONE);
                break;
        }

        //是否需要完成订单
        if (OrderStatusHelper.getNeedFinish(order,user.getRoleType())){
            holder.text_finish.setVisibility(View.VISIBLE);
        }else {
            holder.text_finish.setVisibility(View.GONE);
        }

        //用户可以取消订单 和 催一催
        if (user.getRoleType() == User.ROLE_COMMOM) {
            if (order.getStatus() == Order.ORDER_UNDEAL) {
                holder.text_cancle.setVisibility(View.VISIBLE);
                holder.text_cui.setVisibility(View.VISIBLE);
            } else {
                holder.text_cancle.setVisibility(View.GONE);
                holder.text_cui.setVisibility(View.GONE);
            }
        } else {
            holder.text_cancle.setVisibility(View.GONE);
            holder.text_cui.setVisibility(View.GONE);
        }
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
        public TextView text_cui;
        public TextView text_go;
        public TextView text_allocate;
        public TextView text_eva;
        public TextView text_finish;

        public Holder(View itemView) {
            super(itemView);
            img_pic = (ImageView) itemView.findViewById(R.id.img_order_pic);
            text_num = (TextView) itemView.findViewById(R.id.text_order_num);
            text_time = (TextView) itemView.findViewById(R.id.text_order_time);
            text_question = (TextView) itemView.findViewById(R.id.text_order_question);
            text_status = (TextView) itemView.findViewById(R.id.text_order_status);

            text_cancle = (TextView) itemView.findViewById(R.id.text_order_cancle);
            text_cui = (TextView) itemView.findViewById(R.id.text_order_cui);
            text_go = (TextView) itemView.findViewById(R.id.text_order_go);
            text_allocate = (TextView) itemView.findViewById(R.id.text_order_allocate);
            text_eva = (TextView) itemView.findViewById(R.id.text_order_eva);
            text_finish = (TextView) itemView.findViewById(R.id.text_order_finish);
        }
    }

    private OnRecycleItemClickListener listener;

    public void setOnItemClickListener(OnRecycleItemClickListener listener) {
        this.listener = listener;
    }

    private OnOkListener okListener;

    public void setOkListener(OnOkListener okListener) {
        this.okListener = okListener;
    }

    public interface OnOkListener {
        void onCancleClick(Holder holder);

        void onCuiClick(Holder holder);
    }
}
