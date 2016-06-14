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

/**
 * @Function: 自定义对话框
 * @Date: 2013-10-28
 * @Time: 下午12:37:43
 * @author Tom.Cai
 */
public class DialogPopupPhoto extends Dialog {
    private TextView text_cancel, text_photo, text_camera;
    private Context context;

    public DialogPopupPhoto(Context context){
    	super(context, R.style.PopupDialog);
    	this.context = context;
    	setMsgDialog();
    }
    
    private void setMsgDialog() {
        View mView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_choose_photo, null);
        text_cancel = (TextView) mView.findViewById(R.id.text_pop_identy_cancel);
        text_photo = (TextView) mView.findViewById(R.id.text_pop_identy_photo);
        text_camera = (TextView) mView.findViewById(R.id.text_pop_identy_camera);
        text_cancel.setOnClickListener(listener);
        text_photo.setOnClickListener(listener);
        text_camera.setOnClickListener(listener);

        
        this.setCanceledOnTouchOutside(true);	//点击外部关闭
        
        Window win = this.getWindow();
        win.setGravity(Gravity.BOTTOM);	//从下方弹出
		win.getDecorView().setPadding(0, 0, 0, 0);
		WindowManager.LayoutParams lp = win.getAttributes();
		lp.width = WindowManager.LayoutParams.MATCH_PARENT;
		lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
		win.setAttributes(lp);
        
        super.setContentView(mView);
    }
    
    @Override
    public void show() {
    	super.show();
    }
     
    /**
     * 取消键监听器
     * @param listener
     */ 
    public void setOnCancelListener(View.OnClickListener listener){
    	text_cancel.setOnClickListener(listener); 
    } 
    /**
     * 相册键监听器
     * @param listener
     */ 
    public void setOnPhotoListener(View.OnClickListener listener){
    	text_photo.setOnClickListener(listener); 
    }
    /**
     * 相机键监听器
     * @param listener
     */ 
    public void setOnCameraListener(View.OnClickListener listener){
    	text_camera.setOnClickListener(listener); 
    }
    
    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DialogPopupPhoto.this.dismiss();
        }
    };
}
