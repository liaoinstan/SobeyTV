package com.sobey.tvcust.ui.dialog;
import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.sobey.tvcust.R;

/**
 * @Function: 自定义对话框
 * @Date: 2013-10-28
 * @Time: 下午12:37:43
 * @author Tom.Cai
 */
public class DialogReqfixChoose extends Dialog {
    private TextView text_hard, text_soft;

    public DialogReqfixChoose(Context context){
    	super(context, R.style.MsgDialog);
    	setMsgDialog();
    }
    
    private void setMsgDialog() {
    	View mView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_reqfix_choose, null);
        text_hard = (TextView) mView.findViewById(R.id.text_dialog_reqfix_hardware);
        text_soft = (TextView) mView.findViewById(R.id.text_dialog_reqfix_software);
        if(text_hard !=null) text_hard.setOnClickListener(listener);
        if(text_soft !=null) text_soft.setOnClickListener(listener);

        this.setCanceledOnTouchOutside(true);
        super.setContentView(mView);
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
        lp.width = (int) (screenWidth * 0.65); // 宽度
//        lp.height = (int) (lp.width*0.65); // 高度
        dialogWindow.setAttributes(lp);
    }
     
     @Override
    public void setContentView(int layoutResID) {
    }
 
    @Override
    public void setContentView(View view, LayoutParams params) {
    }
 
    @Override
    public void setContentView(View view) {
    }
 
    /**
     * 确定键监听器
     * @param listener
     */ 
    public void setOnHardListener(View.OnClickListener listener){
        text_hard.setOnClickListener(listener);
    } 
    /**
     * 取消键监听器
     * @param listener
     */ 
    public void setOnSoftListener(View.OnClickListener listener){
        text_soft.setOnClickListener(listener);
    }
    
    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DialogReqfixChoose.this.dismiss();
        }
    };
}
