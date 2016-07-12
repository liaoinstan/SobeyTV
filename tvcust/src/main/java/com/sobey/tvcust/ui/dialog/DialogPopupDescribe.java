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
    private TextView text_toheadtech;
    private TextView text_cancel;
    private TextView text_valipass;
    private TextView text_valirefuse;
    private View lay_finish;
    private View lay_next;
    private View lay_bank;
    private View lay_touser;
    private View lay_describe;
    private View lay_toheadtech;
    private View lay_cancel;
    private View lay_valipass;
    private View lay_valirefuse;

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
        text_toheadtech = (TextView) mView.findViewById(R.id.text_dialog_describe_toheadtech);
        text_cancel = (TextView) mView.findViewById(R.id.text_dialog_describe_cancel);

        lay_finish = mView.findViewById(R.id.lay_dialog_describe_finish);
        lay_valipass = mView.findViewById(R.id.lay_dialog_describe_vali_pass);
        lay_valirefuse = mView.findViewById(R.id.lay_dialog_describe_vali_refuse);
        lay_next = mView.findViewById(R.id.lay_dialog_describe_next);
        lay_bank = mView.findViewById(R.id.lay_dialog_describe_bank);
        lay_touser = mView.findViewById(R.id.lay_dialog_describe_touser);
        lay_describe = mView.findViewById(R.id.lay_dialog_describe_describe);
        lay_toheadtech = mView.findViewById(R.id.lay_dialog_describe_toheadtech);


        text_finish.setOnClickListener(listener);
        text_next.setOnClickListener(listener);
        text_bank.setOnClickListener(listener);
        text_touser.setOnClickListener(listener);
        text_describe.setOnClickListener(listener);
        text_cancel.setOnClickListener(listener);

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
        switch (type) {
            //技术人员
            case User.ROLE_FILIALETECH:
                lay_finish.setVisibility(View.VISIBLE);
                lay_valipass.setVisibility(View.GONE);
                lay_valirefuse.setVisibility(View.GONE);
                lay_next.setVisibility(View.VISIBLE);
                lay_bank.setVisibility(View.VISIBLE);
                lay_touser.setVisibility(View.GONE);
                lay_describe.setVisibility(View.GONE);
                lay_toheadtech.setVisibility(View.GONE);
                break;
            //总部技术
            case User.ROLE_HEADCOMTECH:
                lay_finish.setVisibility(View.VISIBLE);
                lay_valipass.setVisibility(View.GONE);
                lay_valirefuse.setVisibility(View.GONE);
                lay_next.setVisibility(View.VISIBLE);
                lay_bank.setVisibility(View.VISIBLE);
                lay_touser.setVisibility(View.VISIBLE);
                lay_describe.setVisibility(View.GONE);
                lay_toheadtech.setVisibility(View.GONE);
                break;
            //总部研发
            case User.ROLE_INVENT:
                lay_finish.setVisibility(View.GONE);
                lay_valipass.setVisibility(View.GONE);
                lay_valirefuse.setVisibility(View.GONE);
                lay_next.setVisibility(View.GONE);
                lay_bank.setVisibility(View.VISIBLE);
                lay_touser.setVisibility(View.GONE);
                lay_describe.setVisibility(View.GONE);
                lay_toheadtech.setVisibility(View.GONE);
                break;
            //客服
            case User.ROLE_CUSTOMER:
                lay_finish.setVisibility(View.GONE);
                lay_valipass.setVisibility(View.GONE);
                lay_valirefuse.setVisibility(View.GONE);
                lay_next.setVisibility(View.GONE);
                lay_bank.setVisibility(View.GONE);
                lay_touser.setVisibility(View.GONE);
                lay_describe.setVisibility(View.GONE);
                lay_toheadtech.setVisibility(View.GONE);
                break;
            //用户
            case User.ROLE_COMMOM:
                lay_finish.setVisibility(View.GONE);
                lay_valipass.setVisibility(View.VISIBLE);
                lay_valirefuse.setVisibility(View.VISIBLE);
                lay_next.setVisibility(View.GONE);
                lay_bank.setVisibility(View.GONE);
                lay_touser.setVisibility(View.GONE);
                lay_describe.setVisibility(View.VISIBLE);
                lay_toheadtech.setVisibility(View.VISIBLE);
                break;
            default:
                lay_finish.setVisibility(View.GONE);
                lay_valipass.setVisibility(View.GONE);
                lay_valirefuse.setVisibility(View.GONE);
                lay_next.setVisibility(View.GONE);
                lay_bank.setVisibility(View.GONE);
                lay_touser.setVisibility(View.GONE);
                lay_describe.setVisibility(View.GONE);
                lay_toheadtech.setVisibility(View.GONE);
                break;
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

    public void setOnHeadTechListener(View.OnClickListener listener) {
        text_toheadtech.setOnClickListener(listener);
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
