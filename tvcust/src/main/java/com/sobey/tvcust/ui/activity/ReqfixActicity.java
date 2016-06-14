package com.sobey.tvcust.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.hyphenate.easeui.widget.EaseVoiceRecorderView;
import com.sobey.common.helper.CropHelper;
import com.sobey.common.utils.VideoUtils;
import com.sobey.common.view.BundleView;
import com.sobey.tvcust.R;
import com.sobey.tvcust.ui.dialog.DialogPopupPhoto;
import com.sobey.tvcust.ui.dialog.DialogReqfixChoose;

public class ReqfixActicity extends AppCompatActivity implements View.OnClickListener, CropHelper.CropInterface {

    private CropHelper cropHelper = new CropHelper(this);

    private View lay_reqfix_quekind;
    private View lay_reqfix_location;
    private DialogReqfixChoose dialog;
    private DialogPopupPhoto popup;
    private BundleView bundleView;
    private EaseVoiceRecorderView voice_recorder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reqfix);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initBase();
        initView();
        initCtrl();
    }

    private final int TAKE_PHOTO = 101;

    private void initBase() {
        dialog = new DialogReqfixChoose(this);
        dialog.setOnHardListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.hide();
                Intent intent = new Intent(ReqfixActicity.this, QuestionActivity.class);
                startActivity(intent);
            }
        });
        dialog.setOnSoftListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.hide();
                Intent intent = new Intent(ReqfixActicity.this, QuestionActivity.class);
                startActivity(intent);
            }
        });
        popup = new DialogPopupPhoto(this);
        popup.setOnCameraListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.hide();
                cropHelper.startCamera();
            }
        });
        popup.setOnPhotoListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.hide();
                cropHelper.startPhoto();
            }
        });
    }

    private void initView() {
        lay_reqfix_quekind = findViewById(R.id.lay_reqfix_quekind);
        lay_reqfix_location = findViewById(R.id.lay_reqfix_location);
        bundleView = (BundleView) findViewById(R.id.bundle_reqfix);
        voice_recorder = (EaseVoiceRecorderView) findViewById(R.id.voice_recorder);
    }

    private void initCtrl() {
        lay_reqfix_quekind.setOnClickListener(this);
        lay_reqfix_location.setOnClickListener(this);
        bundleView.setOnBundleClickListener(new BundleView.OnBundleClickListener() {
            @Override
            public void onPhotoClick(View v) {
                Log.e("liao","onPhotoClick");
                popup.show();
            }

            @Override
            public void onVideoClick(View v) {
                Intent intent = new Intent(ReqfixActicity.this, VideoRecorderActivity.class);
                startActivityForResult(intent, CODE_VIDEO_RECORDER);
            }

            @Override
            public void onVoiceClick(View v) {
                bundleView.setVoice();
            }
        });
        bundleView.setVoiceTouchListenner(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return voice_recorder.onPressToSpeakBtnTouch(v, event, new EaseVoiceRecorderView.EaseVoiceRecorderCallback() {

                    @Override
                    public void onVoiceRecordComplete(String voiceFilePath, int voiceTimeLength) {
                        // 发送语音消息
                        //sendVoiceMessage(voiceFilePath, voiceTimeLength);
                        Toast.makeText(ReqfixActicity.this,"complete:"+voiceFilePath+"  length:"+voiceTimeLength,Toast.LENGTH_SHORT).show();
                        Log.e("liao","complete:"+voiceFilePath+"  length:"+voiceTimeLength);
                    }
                });
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lay_reqfix_quekind:
                dialog.show();
                break;
            case R.id.lay_reqfix_location:
                break;
        }
    }

    private final int CODE_VIDEO_RECORDER = 0xf101;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        cropHelper.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case CODE_VIDEO_RECORDER:
                if (resultCode == RESULT_OK) {
                    // 成功
                    String path = data.getStringExtra("path");
                    Toast.makeText(this, "存储路径为:" + path, Toast.LENGTH_SHORT).show();
                    // 通过路径获取第一帧的缩略图并显示
                    Bitmap bitmap = VideoUtils.createVideoThumbnail(path);
                    bundleView.setVideo(bitmap);
                } else {
                    // 失败
                }
                break;
        }
    }

    @Override
    public void cropResult(String path) {
        Log.e("liao", path);
//        Toast.makeText(this, path, Toast.LENGTH_SHORT).show();

//        try {
//            Bitmap bitmap = BitmapUtil.revitionImageSize(path);
////            path = BitmapUtil.saveBitmap(this, bitmap, FileUtils.getFileName(path));
//            bundleView.setPhoto(bitmap);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        Bitmap bitmap = BitmapFactory.decodeFile(path);
        bundleView.setPhoto(bitmap);
    }
}
