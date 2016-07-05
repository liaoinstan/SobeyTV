package com.sobey.tvcust.ui.dialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.sobey.tvcust.R;


/**
 * @Function: 自定义对话框
 * @Date: 2013-10-28
 * @Time: 下午12:37:43
 * @author Tom.Cai
 */
public class DialogSure extends Dialog{

    private Context context;
    private TextView text_cancle;
    private TextView text_ok;

    public DialogSure(Context context) {
        super(context,R.style.MyDialog);
        this.context = context;
        setLoadingDialog();
    }

    private void setLoadingDialog() {
    	LayoutInflater inflater = LayoutInflater.from(getContext());
        View v = inflater.inflate(R.layout.dialog_sure, null);// 得到加载view

        text_cancle = (TextView) v.findViewById(R.id.dialog_cancel);
        text_ok = (TextView) v.findViewById(R.id.dialog_ok);
        text_cancle.setOnClickListener(listener);
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
        WindowManager wm = (WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE);;
        wm.getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;
        /////////设置高宽
        lp.width = (int) (screenWidth * 0.85); // 宽度
//        lp.height = (int) (lp.width*0.65); // 高度
        dialogWindow.setAttributes(lp);
    }

    public void setOnCancleListener(View.OnClickListener listener){
        text_cancle.setOnClickListener(listener);
    }

    public void setOnOkListener(View.OnClickListener listener){
        text_ok.setOnClickListener(listener);
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss();
        }
    };
}
