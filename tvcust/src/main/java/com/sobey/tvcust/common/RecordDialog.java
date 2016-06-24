package com.sobey.tvcust.common;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sobey.common.view.InsVoiceRecorderView;
import com.sobey.tvcust.R;


/**
 * @Function: 自定义对话框
 * @Date: 2013-10-28
 * @Time: 下午12:37:43
 * @author Tom.Cai
 */
public class RecordDialog extends Dialog {

    private TextView btn_start;
    private InsVoiceRecorderView recorder;

    public RecordDialog(Context context) {
        super(context,R.style.LoadingDialog);
        setLoadingDialog();
    }

    private void setLoadingDialog() {
    	LayoutInflater inflater = LayoutInflater.from(getContext());
        View v = inflater.inflate(R.layout.dialog_record_layout, null);// 得到加载view


        View layout  = v.findViewById(R.id.dialog_view);// 加载布局
        recorder = (InsVoiceRecorderView) v.findViewById(R.id.recorder);
        btn_start = (TextView) v.findViewById(R.id.btn_start);// 提示文字

        recorder.setNeedHide(false);

        /**
         * <item name="android:windowCloseOnTouchOutside">false</item>
         * 可以在style文件中进行配置，最低app level 11（当前8）
         */
        this.setCanceledOnTouchOutside(false);
        this.setContentView(layout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局
        
        super.setContentView(v);

        btn_start.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return recorder.onPressToSpeakBtnTouch(v, event, new InsVoiceRecorderView.InsVoiceRecorderCallback() {

                    @Override
                    public void onVoiceRecordComplete(String voiceFilePath, int voiceTimeLength) {
                        // 发送语音消息
                        //sendVoiceMessage(voiceFilePath, voiceTimeLength);
//                        Toast.makeText(getContext(),"complete:"+voiceFilePath+"  length:"+voiceTimeLength, Toast.LENGTH_SHORT).show();
//                        Log.e("liao","complete:"+voiceFilePath+"  length:"+voiceTimeLength);
                        if (onRecordListener!=null) onRecordListener.onRecordFinish(voiceFilePath,voiceTimeLength);
                        hide();
                    }
                });
            }
        });
    }


    private OnRecordListener onRecordListener;
    public void setOnRecordListener(OnRecordListener onRecordListener) {
        this.onRecordListener = onRecordListener;
    }
    public interface OnRecordListener{
        void onRecordFinish(String voiceFilePath, int voiceTimeLength);
    }
}