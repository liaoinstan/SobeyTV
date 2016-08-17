package com.sobey.common.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.PowerManager;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sobey.common.R;


/**
 * 按住说话录制控件
 */
public class InsVoiceRecorderView extends RelativeLayout {
    protected Context context;
    protected LayoutInflater inflater;
    protected Drawable[] micImages;
    protected InsVoiceRecorder voiceRecorder;

    protected PowerManager.WakeLock wakeLock;
    protected ImageView micImage;
    protected TextView recordingHint;

    protected boolean isNeedHide = true;   //是否需要录完后隐藏录制界面，为false则一直显示不隐藏

    protected Handler micImageHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            // 切换msg切换图片
            int index = msg.what;
            if (msg.what > micImages.length - 1) {
                index = micImages.length - 1;
            }
            micImage.setImageDrawable(micImages[index]);
        }
    };

    public InsVoiceRecorderView(Context context) {
        super(context);
        init(context);
    }

    public InsVoiceRecorderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public InsVoiceRecorderView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.ins_widget_voice_recorder, this);

        micImage = (ImageView) findViewById(R.id.mic_image);
        recordingHint = (TextView) findViewById(R.id.recording_hint);

        voiceRecorder = new InsVoiceRecorder(micImageHandler);

        // 动画资源文件,用于录制语音时
        micImages = new Drawable[]{getResources().getDrawable(R.drawable.ins_record_animate_01),
                getResources().getDrawable(R.drawable.ins_record_animate_02),
                getResources().getDrawable(R.drawable.ins_record_animate_03),
                getResources().getDrawable(R.drawable.ins_record_animate_04),
                getResources().getDrawable(R.drawable.ins_record_animate_05),
                getResources().getDrawable(R.drawable.ins_record_animate_06),
                getResources().getDrawable(R.drawable.ins_record_animate_07),
                getResources().getDrawable(R.drawable.ins_record_animate_08),
                getResources().getDrawable(R.drawable.ins_record_animate_09),
                getResources().getDrawable(R.drawable.ins_record_animate_10),
                getResources().getDrawable(R.drawable.ins_record_animate_11),
                getResources().getDrawable(R.drawable.ins_record_animate_12),
                getResources().getDrawable(R.drawable.ins_record_animate_13),
                getResources().getDrawable(R.drawable.ins_record_animate_14),};

        wakeLock = ((PowerManager) context.getSystemService(Context.POWER_SERVICE)).newWakeLock(
                PowerManager.SCREEN_DIM_WAKE_LOCK, "demo");
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (isNeedHide) {
            this.setVisibility(INVISIBLE);
        } else {
            this.setVisibility(VISIBLE);
        }
        showNoHint();
    }

    /**
     * 长按说话按钮touch事件
     *
     * @param v
     * @param event
     */
    public boolean onPressToSpeakBtnTouch(View v, MotionEvent event, InsVoiceRecorderCallback recorderCallback) {
//        final int action = event.getAction();
        final int action = MotionEventCompat.getActionMasked(event);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                Log.e("voice", "ACTION_DOWN");
                try {
//                if (EaseChatRowVoicePlayClickListener.isPlaying)
//                    EaseChatRowVoicePlayClickListener.currentPlayListener.stopPlayVoice();
                    showMoveUpToCancelHint();
                    v.setPressed(true);
                    startRecording();
                } catch (Exception e) {
                    v.setPressed(false);
                }
                return true;
            case MotionEvent.ACTION_MOVE:
                Log.e("voice", "ACTION_MOVE");
                if (event.getY() < 0) {
                    showReleaseToCancelHint();
                } else {
                    showMoveUpToCancelHint();
                }
                return true;
            case MotionEvent.ACTION_UP:
                Log.e("voice", "ACTION_UP");
                micImage.setImageDrawable(micImages[0]);
                showNoHint();
                v.setPressed(false);
                if (event.getY() < 0) {
                    // discard the recorded audio.
                    discardRecording();
                } else {
                    // stop recording and send voice file
                    try {
                        int length = stopRecoding();
                        if (length > 0) {
                            if (recorderCallback != null) {
                                recorderCallback.onVoiceRecordComplete(getVoiceFilePath(), length);
                            }
                        } else if (length >= 0 && length < 1) {
                            Toast.makeText(context, "录音时间太短", Toast.LENGTH_SHORT).show();
                        } else if (length == -1) {
                            Toast.makeText(context, "什么声音也没有", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "无录音权限", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(context, "录音失败", Toast.LENGTH_SHORT).show();
                    }
                }
                return true;
            case MotionEvent.ACTION_POINTER_DOWN:
                Log.e("voice", "ACTION_POINTER_DOWN");
                return true;
            case MotionEvent.ACTION_POINTER_UP:
                Log.e("voice", "ACTION_POINTER_UP");
                return true;
            default:
                discardRecording();
                return false;
        }
    }

    public interface InsVoiceRecorderCallback {
        /**
         * 录音完毕
         *
         * @param voiceFilePath   录音完毕后的文件路径
         * @param voiceTimeLength 录音时长
         */
        void onVoiceRecordComplete(String voiceFilePath, int voiceTimeLength);
    }

    public void startRecording() {
        if (!isExitsSdcard()) {
            Toast.makeText(context, "您的手机没有SD卡", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            wakeLock.acquire();
            if (isNeedHide) this.setVisibility(View.VISIBLE);
            recordingHint.setText("手指上滑，取消发送");
            recordingHint.setBackgroundColor(Color.TRANSPARENT);
            voiceRecorder.startRecording(context);
        } catch (Exception e) {
            e.printStackTrace();
            if (wakeLock.isHeld())
                wakeLock.release();
            if (voiceRecorder != null)
                voiceRecorder.discardRecording();
            if (isNeedHide) this.setVisibility(View.INVISIBLE);
            Toast.makeText(context, "录音失败，请重试", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    public void showNoHint() {
        recordingHint.setText("");
        recordingHint.setBackgroundColor(Color.TRANSPARENT);
    }

    public void showReleaseToCancelHint() {
        recordingHint.setText("松开手指，取消发送");
        recordingHint.setBackgroundResource(R.drawable.ins_recording_text_hint_bg);
    }

    public void showMoveUpToCancelHint() {
        recordingHint.setText("手指上滑，取消发送");
        recordingHint.setBackgroundColor(Color.TRANSPARENT);
    }

    public void discardRecording() {
        if (wakeLock.isHeld())
            wakeLock.release();
        try {
            // 停止录音
            if (voiceRecorder.isRecording()) {
                voiceRecorder.discardRecording();
                if (isNeedHide) this.setVisibility(View.INVISIBLE);
            }
        } catch (Exception e) {
        }
    }

    public int stopRecoding() {
        if (isNeedHide) this.setVisibility(View.INVISIBLE);
        if (wakeLock.isHeld())
            wakeLock.release();
        return voiceRecorder.stopRecoding();
    }

    public String getVoiceFilePath() {
        return voiceRecorder.getVoiceFilePath();
    }

    public String getVoiceFileName() {
        return voiceRecorder.getVoiceFileName();
    }

    public boolean isRecording() {
        return voiceRecorder.isRecording();
    }


    //////////////////////


    /**
     * 检测Sdcard是否存在
     *
     * @return
     */
    public static boolean isExitsSdcard() {
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
            return true;
        else
            return false;
    }

    public void setNeedHide(boolean isNeedHide) {
        this.isNeedHide = isNeedHide;
        if (this.isNeedHide) {
            this.setVisibility(INVISIBLE);
        } else {
            this.setVisibility(VISIBLE);
        }
    }

}
