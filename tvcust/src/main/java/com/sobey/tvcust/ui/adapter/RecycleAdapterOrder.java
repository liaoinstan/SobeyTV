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
import com.sobey.tvcust.ui.activity.AssistActivity;
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
                if (okListener != null) okListener.onOkClick(holder);
            }
        });
        holder.text_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, OrderDetailActivity.class);
                intent.putExtra("id", results.get(holder.getLayoutPosition()).getId());
                context.startActivity(intent);
            }
        });
        holder.text_allocate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, OrderAllocateActivity.class);
                Order order = results.get(holder.getLayoutPosition());
                intent.putExtra("orderId", order.getId());
                intent.putExtra("userId", order.getUserId());
                intent.putExtra("categoryId", order.getCategory().getId());
                context.startActivity(intent);
            }
        });
        holder.text_assist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AssistActivity.class);
                Order order = results.get(holder.getLayoutPosition());
                intent.putExtra("type", "0");
                intent.putExtra("userId", order.getUserId());
                intent.putExtra("orderId", order.getId());
                context.startActivity(intent);
            }
        });

        Order order = results.get(holder.getLayoutPosition());
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
        if (user.getRoleType() == User.ROLE_CUSTOMER && order.getTscId() == null && order.getStatus() == Order.ORDER_UNDEAL) {
            //当前登录是客服 && 订单尚未分配 && 订单是未处理状态
            holder.text_allocate.setVisibility(View.VISIBLE);
        } else {
            holder.text_allocate.setVisibility(View.GONE);
        }

        holder.text_assist.setVisibility(View.GONE);
//        //技术人员 or 总部技术人员 可以申请援助
//        if (user.getRoleType() == User.ROLE_FILIALETECH || user.getRoleType() == User.ROLE_HEADCOMTECH) {
//            //技术尚未接受订单
//            if (order.getTechCheck() == null || order.getTechCheck() == 0) {
//                holder.text_assist.setVisibility(View.GONE);
//            }
//            //技术已经接受订单
//            else {
////                //技术 申请 总技术
////                if (user.getRoleType() == User.ROLE_FILIALETECH) {
////                    if (order.getHeadTechId() == null) {
////                        //尚未申请给总技术
////                        holder.text_assist.setVisibility(View.VISIBLE);
////                    } else {
////                        //已申请给总技术
////                        holder.text_assist.setVisibility(View.GONE);
////                    }
////                }//总技术 申请 总研发
////                else if (user.getRoleType() == User.ROLE_HEADCOMTECH) {
////                    if (order.getDecelopId() == null) {
////                        //尚未申请总研发
////                        holder.text_assist.setVisibility(View.VISIBLE);
////                    } else {
////                        //已申请总研发
////                        holder.text_assist.setVisibility(View.GONE);
////                    }
////                }
//                holder.text_assist.setVisibility(View.VISIBLE);
//            }
//
//        } else {
//            holder.text_assist.setVisibility(View.GONE);
//        }
        //用户可以取消订单
        if (user.getRoleType() == User.ROLE_COMMOM) {
            holder.text_cancle.setVisibility(View.VISIBLE);
        } else {
            holder.text_cancle.setVisibility(View.GONE);
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
        public TextView text_go;
        public TextView text_allocate;
        public TextView text_assist;

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
            text_assist = (TextView) itemView.findViewById(R.id.text_order_assist);
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
        void onOkClick(Holder holder);
    }
}
