package com.sobey.tvcust.common;

import com.sobey.tvcust.R;
import com.sobey.tvcust.entity.Order;
import com.sobey.tvcust.entity.User;

/**
 * Created by Administrator on 2016/7/4 0004.
 */
public class OrderStatusHelper {
    public static String getStatusStr(int userType, Order order) {
        //用户
        switch (order.getStatus()) {
            case Order.ORDER_UNDEAL:
                //待处理
                return "等待客服待处理";
            case Order.ORDER_INDEAL:
                //处理中
                return "已分配维修人员，正在维修";
            case Order.ORDER_UNVALI:
                //带验证
                return "等待确认";
            case Order.ORDER_UNEVA:
                //待评价
                return "待评价";
            case Order.ORDER_FINSH:
                //已完成
                return "已解决";
            default:
                return "";
        }
    }

    public static int getStatusImgSrc(Order order){
        switch (order.getStatus()) {
            case Order.ORDER_UNDEAL:
                //待处理
                return R.drawable.icon_order_serv;
            case Order.ORDER_INDEAL:
                //处理中
                return R.drawable.icon_order_run;
            case Order.ORDER_UNVALI:
                //带验证
                return R.drawable.icon_order_cpu;
            case Order.ORDER_UNEVA:
                //待评价
                return R.drawable.icon_order_cpu;
            case Order.ORDER_FINSH:
                //已完成
                return R.drawable.icon_finish;
            default:
                return R.drawable.icon_order_fix;
        }
    }
}
