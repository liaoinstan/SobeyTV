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
    private TextView text_cancel;

    public DialogPopupDescribe(Context context) {
        super(context, R.style.PopupDialog);
        this.context = context;
        setMsgDialog();
    }

    private void setMsgDialog() {
        View mView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_choose_describe, null);

        text_finish = (TextView) mView.findViewById(R.id.text_dialog_describe_finish);
        text_next = (TextView) mView.findViewById(R.id.text_dialog_describe_next);
        text_bank = (TextView) mView.findViewById(R.id.text_dialog_describe_bank);
        text_touser = (TextView) mView.findViewById(R.id.text_dialog_describe_touser);
        text_describe = (TextView) mView.findViewById(R.id.text_dialog_describe_describe);
        text_cancel = (TextView) mView.findViewById(R.id.text_dialog_describe_cancel);
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

    public void setType(int type) {
        switch (type) {
            //技术人员
            case User.ROLE_FILIALETECH:
                text_finish.setVisibility(View.VISIBLE);
                text_next.setVisibility(View.VISIBLE);
                text_bank.setVisibility(View.VISIBLE);
                text_touser.setVisibility(View.GONE);
                text_describe.setVisibility(View.GONE);
                break;
            //总部技术
            case User.ROLE_HEADCOMTECH:
                text_finish.setVisibility(View.VISIBLE);
                text_next.setVisibility(View.VISIBLE);
                text_bank.setVisibility(View.VISIBLE);
                text_touser.setVisibility(View.VISIBLE);
                text_describe.setVisibility(View.GONE);
                break;
            //总部研发
            case User.ROLE_INVENT:
                text_finish.setVisibility(View.GONE);
                text_next.setVisibility(View.GONE);
                text_bank.setVisibility(View.VISIBLE);
                text_touser.setVisibility(View.GONE);
                text_describe.setVisibility(View.GONE);
                break;
            //客服
            case User.ROLE_CUSTOMER:
                text_finish.setVisibility(View.GONE);
                text_next.setVisibility(View.GONE);
                text_bank.setVisibility(View.GONE);
                text_touser.setVisibility(View.GONE);
                text_describe.setVisibility(View.GONE);
                break;
            //用户
            case User.ROLE_COMMOM:
                text_finish.setVisibility(View.GONE);
                text_next.setVisibility(View.GONE);
                text_bank.setVisibility(View.GONE);
                text_touser.setVisibility(View.GONE);
                text_describe.setVisibility(View.VISIBLE);
                break;
            default:
                text_finish.setVisibility(View.GONE);
                text_next.setVisibility(View.GONE);
                text_bank.setVisibility(View.GONE);
                text_touser.setVisibility(View.GONE);
                text_describe.setVisibility(View.GONE);
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
