package com.sobey.tvcust.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.sobey.tvcust.R;
import com.sobey.tvcust.common.OrderStatusHelper;
import com.sobey.tvcust.entity.Order;
import com.sobey.tvcust.entity.User;

/**
 * @author Tom.Cai
 * @Function: 自定义对话框
 * @Date: 2013-10-28
 * @Time: 下午12:37:43
 */
public class DialogPopupDescribe extends Dialog {
    private Context context;

    private TextView text_finish;
    private TextView text_next;
    private TextView text_bank;
    private TextView text_touser;
    private TextView text_describe;
    private TextView text_bug;
    private TextView text_cancel;
    private TextView text_valipass;
    private TextView text_valirefuse;
    private TextView text_eva;
    private View lay_finish;
    private View lay_next;
    private View lay_bank;
    private View lay_touser;
    private View lay_describe;
    private View lay_bug;
    private View lay_cancel;
    private View lay_valipass;
    private View lay_valirefuse;
    private View lay_eva;

    public DialogPopupDescribe(Context context) {
        super(context, R.style.PopupDialog);
        this.context = context;
        setMsgDialog();
    }

    private void setMsgDialog() {
        View mView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_choose_describe, null);

        text_finish = (TextView) mView.findViewById(R.id.text_dialog_describe_finish);
        text_valipass = (TextView) mView.findViewById(R.id.text_dialog_describe_vali_pass);
        text_valirefuse = (TextView) mView.findViewById(R.id.text_dialog_describe_vali_refuse);
        text_next = (TextView) mView.findViewById(R.id.text_dialog_describe_next);
        text_bank = (TextView) mView.findViewById(R.id.text_dialog_describe_bank);
        text_touser = (TextView) mView.findViewById(R.id.text_dialog_describe_touser);
        text_describe = (TextView) mView.findViewById(R.id.text_dialog_describe_describe);
        text_bug = (TextView) mView.findViewById(R.id.text_dialog_describe_bug);
        text_cancel = (TextView) mView.findViewById(R.id.text_dialog_describe_cancel);
        text_eva = (TextView) mView.findViewById(R.id.text_dialog_describe_eva);

        lay_finish = mView.findViewById(R.id.lay_dialog_describe_finish);
        lay_valipass = mView.findViewById(R.id.lay_dialog_describe_vali_pass);
        lay_valirefuse = mView.findViewById(R.id.lay_dialog_describe_vali_refuse);
        lay_next = mView.findViewById(R.id.lay_dialog_describe_next);
        lay_bank = mView.findViewById(R.id.lay_dialog_describe_bank);
        lay_touser = mView.findViewById(R.id.lay_dialog_describe_touser);
        lay_describe = mView.findViewById(R.id.lay_dialog_describe_describe);
        lay_bug = mView.findViewById(R.id.lay_dialog_describe_bug);
        lay_eva = mView.findViewById(R.id.lay_dialog_describe_eva);


        text_finish.setOnClickListener(listener);
        text_next.setOnClickListener(listener);
        text_bank.setOnClickListener(listener);
        text_touser.setOnClickListener(listener);
        text_describe.setOnClickListener(listener);
        text_bug.setOnClickListener(listener);
        text_cancel.setOnClickListener(listener);
        text_eva.setOnClickListener(listener);

        this.setCanceledOnTouchOutside(true);    //点击外部关闭

        Window win = this.getWindow();
        win.setGravity(Gravity.BOTTOM);    //从下方弹出
        win.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        win.setAttributes(lp);

        super.setContentView(mView);
    }

    public void setType(int type, Order order) {
        if (OrderStatusHelper.getNeedFinish(order, type)) {
            lay_finish.setVisibility(View.VISIBLE);
        } else {
            lay_finish.setVisibility(View.GONE);
        }
        if (OrderStatusHelper.getNeedEva(order, type) == 1) {
            lay_eva.setVisibility(View.VISIBLE);
        } else {
            lay_eva.setVisibility(View.GONE);
        }
        switch (type) {
            //技术人员
            case User.ROLE_FILIALETECH:

                lay_valipass.setVisibility(View.GONE);
                lay_valirefuse.setVisibility(View.GONE);
                //只有处理中的订单能反馈和申请
                if (order.getStatus().equals(Order.ORDER_INDEAL)) {
                    lay_next.setVisibility(View.VISIBLE);
                    lay_bank.setVisibility(View.VISIBLE);
                } else {
                    lay_next.setVisibility(View.GONE);
                    lay_bank.setVisibility(View.GONE);
                }
                lay_touser.setVisibility(View.GONE);
                lay_describe.setVisibility(View.GONE);
                lay_bug.setVisibility(View.GONE);
                if (order.getHeadTechId() == null || order.getHeadTechId() == 0) {
                    text_next.setText("申请总部技术协助");
                } else {
                    text_next.setText("反馈总部技术");
                }
                text_bank.setText("反馈用户");
                break;
            //总部技术
            case User.ROLE_HEADCOMTECH:
                lay_valipass.setVisibility(View.GONE);
                lay_valirefuse.setVisibility(View.GONE);
                //只有处理中的订单能反馈和申请
                if (order.getStatus().equals(Order.ORDER_INDEAL)) {
                    lay_next.setVisibility(View.VISIBLE);
                    lay_bank.setVisibility(View.VISIBLE);
                    if (order.getTscId() == null || order.getTscId() == 0) {
                        //没有申请过tsc
                        text_bank.setText("申请分公司技术协助");
                    } else {
                        text_bank.setText("反馈分公司技术");
                    }
                    if (order.getIsHeadTech().equals(1)) {
                        //如果是直接分配给总部技术
                        lay_touser.setVisibility(View.VISIBLE);
                    } else {
                        lay_touser.setVisibility(View.GONE);
                    }
                } else {
                    lay_next.setVisibility(View.GONE);
                    lay_bank.setVisibility(View.GONE);
                    lay_touser.setVisibility(View.GONE);
                }
                lay_describe.setVisibility(View.GONE);
                lay_bug.setVisibility(View.GONE);
                if (order.getDecelopId() == null || order.getDecelopId() == 0) {
                    text_next.setText("申请总部研发协助");
                } else {
                    text_next.setText("反馈总部研发");
                }
                break;
            //总部研发
            case User.ROLE_INVENT:
                lay_valipass.setVisibility(View.GONE);
                lay_valirefuse.setVisibility(View.GONE);
                lay_next.setVisibility(View.GONE);
                //只有处理中的订单能反馈和申请
                if (order.getStatus().equals(Order.ORDER_INDEAL)) {
                    lay_bank.setVisibility(View.VISIBLE);
                } else {
                    lay_bank.setVisibility(View.GONE);
                }
                lay_touser.setVisibility(View.GONE);
                lay_describe.setVisibility(View.GONE);
                if (order.getIsFeedback() == 1) {
                    lay_bug.setVisibility(View.VISIBLE);
                } else {
                    lay_bug.setVisibility(View.GONE);
                }
                text_bank.setText("反馈总部技术");
                break;
            //客服
            case User.ROLE_CUSTOMER:
                lay_valipass.setVisibility(View.GONE);
                lay_valirefuse.setVisibility(View.GONE);
                lay_next.setVisibility(View.GONE);
                lay_bank.setVisibility(View.GONE);
                lay_touser.setVisibility(View.GONE);
                lay_describe.setVisibility(View.GONE);
                lay_bug.setVisibility(View.GONE);
                break;
            //用户
            case User.ROLE_COMMOM:
                if (order.getStatus().equals(Order.ORDER_UNVALI)) {
                    lay_valipass.setVisibility(View.VISIBLE);
                    lay_valirefuse.setVisibility(View.VISIBLE);
                } else {
                    lay_valipass.setVisibility(View.GONE);
                    lay_valirefuse.setVisibility(View.GONE);
                }
                lay_next.setVisibility(View.GONE);
                lay_bank.setVisibility(View.GONE);
                lay_touser.setVisibility(View.GONE);
                if (order.getStatus() == Order.ORDER_UNEVA || order.getStatus() == Order.ORDER_FINSH || order.getStatus() == Order.ORDER_UNVALI) {
                    lay_describe.setVisibility(View.GONE);
                } else {
                    lay_describe.setVisibility(View.VISIBLE);
                }
                lay_bug.setVisibility(View.GONE);
                break;
            default:
                lay_finish.setVisibility(View.GONE);
                lay_valipass.setVisibility(View.GONE);
                lay_valirefuse.setVisibility(View.GONE);
                lay_next.setVisibility(View.GONE);
                lay_bank.setVisibility(View.GONE);
                lay_touser.setVisibility(View.GONE);
                lay_describe.setVisibility(View.GONE);
                lay_eva.setVisibility(View.GONE);
                lay_bug.setVisibility(View.GONE);
                break;
        }
    }

    public boolean isAllHide() {
        boolean v1 = lay_finish.getVisibility() == View.VISIBLE;
        boolean v2 = lay_valipass.getVisibility() == View.VISIBLE;
        boolean v3 = lay_valirefuse.getVisibility() == View.VISIBLE;
        boolean v4 = lay_next.getVisibility() == View.VISIBLE;
        boolean v5 = lay_bank.getVisibility() == View.VISIBLE;
        boolean v6 = lay_touser.getVisibility() == View.VISIBLE;
        boolean v7 = lay_describe.getVisibility() == View.VISIBLE;
        boolean v8 = lay_eva.getVisibility() == View.VISIBLE;
        if (!v1 && !v2 && !v3 && !v4 && !v5 && !v6 && !v7 && !v8) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void show() {
        super.show();
    }

    public void setOnFinishListener(View.OnClickListener listener) {
        text_finish.setOnClickListener(listener);
    }

    public void setOnValiPassListener(View.OnClickListener listener) {
        text_valipass.setOnClickListener(listener);
    }

    public void setOnValiRefuseListener(View.OnClickListener listener) {
        text_valirefuse.setOnClickListener(listener);
    }

    public void setOnNextListener(View.OnClickListener listener) {
        text_next.setOnClickListener(listener);
    }

    public void setOnBankListener(View.OnClickListener listener) {
        text_bank.setOnClickListener(listener);
    }

    public void setOnUserListener(View.OnClickListener listener) {
        text_touser.setOnClickListener(listener);
    }

    public void setOnDescribeListener(View.OnClickListener listener) {
        text_describe.setOnClickListener(listener);
    }

    public void setOnBugListener(View.OnClickListener listener) {
        text_bug.setOnClickListener(listener);
    }

    public void setOnEvaListener(View.OnClickListener listener) {
        text_eva.setOnClickListener(listener);
    }

    public void setOnCancelListener(View.OnClickListener listener) {
        text_cancel.setOnClickListener(listener);
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DialogPopupDescribe.this.dismiss();
        }
    };
}
