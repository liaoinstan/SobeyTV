package com.sobey.tvcust.common;

import android.view.View;

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
            case Order.ORDER_UNDEAL: {
                //待处理
                if (userType == User.ROLE_COMMOM) {
                    return "等待客服待处理";
                } else if (userType == User.ROLE_CUSTOMER) {
                    if (order.getServiceCheck() != 1) {
                        return "等待查看任务";
                    } else {
                        if (order.getTscId() != null && order.getHeadTechId() != null) {
                            return "等待技术处理";
                        } else {
                            return "等待客服分配维修人员";
                        }
                    }
                } else if (userType == User.ROLE_FILIALETECH) {
                    if (order.getTechCheck() != 1) {
                        return "等待查看任务";
                    } else {
                        return "等待技术处理";
                    }
                } else if (userType == User.ROLE_HEADCOMTECH) {
                    if (order.getHeadTechCheck() != 1) {
                        return "等待查看任务";
                    } else {
                        return "等待技术处理";
                    }
                } else if (userType == User.ROLE_INVENT) {
                    if (order.getDevelopCheck() != 1) {
                        return "等待查看任务";
                    } else {
                        return "等待技术处理";
                    }
                } else {
                    //其他被抄送的角色
                    return "正在进行中";
                }
            }
            case Order.ORDER_INDEAL:
                //处理中
                return "正在维修";
            case Order.ORDER_UNVALI:
                //带验证
                return "技术已处理";
            case Order.ORDER_UNEVA:
                //待评价
                return "待评价";
            case Order.ORDER_FINSH:
                //已完成
                return "已解决";
            default:
                return "";
        }

//        switch (userType){
//            case User.ROLE_COMMOM:
//                break;
//            case User.ROLE_CUSTOMER:
//                break;
//            case User.ROLE_FILIALETECH:
//                break;
//            case User.ROLE_HEADCOMTECH:
//                break;
//            case User.ROLE_INVENT:
//                break;
//        }
    }

    public static int getStatusImgSrc(Order order) {
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

    /**
     * 根据订单状态和角色区分是否需要评价
     * 返回 0 不需要  1 需要   2 无法评价（即不能评价也不能查看评价）
     *
     * @param order
     * @param roleType
     * @return
     */
    public static int getNeedEva(Order order, int roleType) {
        if (order.getStatus() == Order.ORDER_UNEVA || order.getStatus() == Order.ORDER_FINSH) {
            switch (roleType) {
                case User.ROLE_COMMOM:
                    if (order.getStatus() == Order.ORDER_UNEVA) {
                        //未评论
                        return 1;
                    } else {
                        return 0;
                    }
                case User.ROLE_CUSTOMER:
                    if (order.getIsServiceComment() != 1) {
                        //未评论
                        return 1;
                    } else {
                        return 0;
                    }
                case User.ROLE_FILIALETECH:
                    if (order.getIsTSCComment() != 1) {
                        //未评论
                        return 1;
                    } else {
                        return 0;
                    }
                case User.ROLE_HEADCOMTECH:
                    if (order.getIsHeadTechComment() != 1) {
                        //未评论
                        return 1;
                    } else {
                        return 0;
                    }
                case User.ROLE_INVENT:
                    if (order.getIsHeadDevelopComment() != 1) {
                        //未评论
                        return 1;
                    } else {
                        return 0;
                    }
                default:
                    return 2;
            }
        } else {
            return 2;
        }
    }

    /**
     * 是否显示接受任务按钮
     * * 返回 0 显示接受任  1 需要操作   2 不显示
     */
    public static int getNeedAcceptBtn(Order order, int roleType) {
        switch (roleType) {
            //技术人员
            case User.ROLE_FILIALETECH:
                if (order.getTscIsAccept() != 1 && (order.getStatus()==Order.ORDER_UNDEAL || order.getStatus()==Order.ORDER_INDEAL)) {
                    //如果是技术人员，且未接受，则显示接受按钮
                    return 0;
                } else {
                    //如果已经接受，则显示操作按钮
                    return 1;
                }
                //总部技术
            case User.ROLE_HEADCOMTECH:
                if (order.getHeadTechIsAccept() != 1 && (order.getStatus()==Order.ORDER_UNDEAL || order.getStatus()==Order.ORDER_INDEAL)) {
                    return 0;
                } else {
                    return 1;
                }
                //总部研发
            case User.ROLE_INVENT:
                if (order.getDevelopIsAccept() != 1 && (order.getStatus()==Order.ORDER_UNDEAL || order.getStatus()==Order.ORDER_INDEAL)) {
                    return 0;
                } else {
                    return 1;
                }
                //客服
            case User.ROLE_CUSTOMER:
                //客服，无法操作
                return 2;
            //用户
            case User.ROLE_COMMOM:
                return 1;
            default:
                //其他被抄送人员，无法操作
                return 2;
        }
    }
    /**
     * 订单是否可以完成
     */
    public static boolean getNeedFinish(Order order, int roleType) {
        //只有tsc和总部技术 在 订单是处理中的情况下可以完成订单
        //tsc 已经接受了订单
        if (User.ROLE_FILIALETECH == roleType && order.getTscIsAccept() == 1) {
            if (order.getStatus().equals(Order.ORDER_UNDEAL) || order.getStatus().equals(Order.ORDER_INDEAL)) {
                return true;
            } else {
                return false;
            }
        }
        //总部技术 已经接受了订单
        else if (User.ROLE_HEADCOMTECH == roleType && order.getHeadTechIsAccept() == 1) {
            if (order.getStatus().equals(Order.ORDER_UNDEAL) || order.getStatus().equals(Order.ORDER_INDEAL)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
//        if (User.ROLE_FILIALETECH == roleType || User.ROLE_HEADCOMTECH == roleType) {
//            if (order.getStatus().equals(Order.ORDER_INDEAL)) {
//                return true;
//            } else {
//                return false;
//            }
//        } else {
//            return false;
//        }
    }

    public static String getRoleNameByType(int roleType) {
        switch (roleType) {
            case User.ROLE_COMMOM:
                return "用户";
            case User.ROLE_CUSTOMER:
                return "客服";
            case User.ROLE_FILIALETECH:
                return "TSC";
            case User.ROLE_HEADCOMTECH:
                return "总部技术";
            case User.ROLE_INVENT:
                return "总部研发";
            default:
                return "其他人员";
        }
    }

    public static String getDescribeName(int roleType, int from, int to) {
        if (from == 0 && to == 0) {
            return "订单描述";
        } else if (from != 0 && to == 0) {
            return getRoleNameByType(from) + "追加描述";
        } else {
            if (roleType == User.ROLE_COMMOM) {
                if (from == User.ROLE_COMMOM) {
                    return "用户" + " 反馈 " + "技术";
                }else {
                    return "技术" + " 反馈 " + "用户";
                }
            } else {
                return getRoleNameByType(from) + " 反馈 " + getRoleNameByType(to);
            }
        }
    }
}
