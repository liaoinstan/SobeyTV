package com.sobey.tvcust.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.sobey.tvcust.R;


/**
 * @author Tom.Cai
 * @Function: 自定义对话框
 * @Date: 2013-10-28
 * @Time: 下午12:37:43
 */
public class DialogNotice extends Dialog {

    private Context context;
    private TextView text_dialog_sure;
    private TextView text_ok;
    private String msg;
    private String sureStr;

    public DialogNotice(Context context) {
        this(context, "确定？", "确定");
    }

    public DialogNotice(Context context, String msg) {
        this(context, msg, "确定");
    }

    public DialogNotice(Context context, String msg, String sureStr) {
        super(context, R.style.MyDialog);
        this.context = context;
        this.msg = msg;
        this.sureStr = sureStr;
        setLoadingDialog();
    }

    private void setLoadingDialog() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View v = inflater.inflate(R.layout.dialog_notice, null);// 得到加载view

        text_dialog_sure = (TextView) v.findViewById(R.id.text_dialog_sure);
        text_ok = (TextView) v.findViewById(R.id.dialog_ok);

        text_dialog_sure.setText(msg);
        text_ok.setText(sureStr);
        text_ok.setOnClickListener(listener);

        this.setCanceledOnTouchOutside(true);
        super.setContentView(v);

    }


    @Override
    public void show() {
        super.show();
        Window dialogWindow = this.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        /////////获取屏幕宽度
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        ;
        wm.getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;
        /////////设置高宽
        lp.width = (int) (screenWidth * 0.85); // 宽度
//        lp.height = (int) (lp.width*0.65); // 高度
        dialogWindow.setAttributes(lp);
    }

    public void setMsg(String msg) {
        text_dialog_sure.setText(msg);
    }

    public void setOnOkListener(View.OnClickListener listener) {
        text_ok.setOnClickListener(listener);
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss();
        }
    };
}
