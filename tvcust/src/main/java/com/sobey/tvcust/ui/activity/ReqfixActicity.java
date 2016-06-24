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

import com.sobey.common.common.MyPlayer;
import com.sobey.common.helper.CropHelper;
import com.sobey.common.utils.VideoUtils;
import com.sobey.common.view.BundleView;
import com.sobey.common.view.InsVoiceRecorderView;
import com.sobey.tvcust.R;
import com.sobey.tvcust.common.RecordDialog;
import com.sobey.tvcust.ui.dialog.DialogPopupPhoto;
import com.sobey.tvcust.ui.dialog.DialogReqfixChoose;

public class ReqfixActicity extends AppCompatActivity implements View.OnClickListener, CropHelper.CropInterface {

    private CropHelper cropHelper = new CropHelper(this);

    private View lay_reqfix_quekind;
    private DialogReqfixChoose chooseDialog;
    private DialogPopupPhoto popup;
    private RecordDialog recordDialog;
    private BundleView bundleView;
    private InsVoiceRecorderView voice_recorder;
    private MyPlayer player = new MyPlayer();

    private View btn_go;

    private String pathphoto;
    private String pathvideo;
    private String pathvoice;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("pathphoto", pathphoto);
        outState.putString("pathvideo", pathvideo);
        outState.putString("pathvoice", pathvoice);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            pathphoto = savedInstanceState.getString("pathphoto");
            pathvideo = savedInstanceState.getString("pathvideo");
            pathvoice = savedInstanceState.getString("pathvoice");
        }
        setContentView(R.layout.activity_reqfix);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initBase();
        initView();
        initCtrl();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        player.onDestory();
    }

    private void initBase() {
        chooseDialog = new DialogReqfixChoose(this);
        chooseDialog.setOnHardListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseDialog.hide();
                Intent intent = new Intent(ReqfixActicity.this, QuestionActivity.class);
                startActivity(intent);
            }
        });
        chooseDialog.setOnSoftListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseDialog.hide();
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
        recordDialog = new RecordDialog(this);
    }

    private void initView() {
        lay_reqfix_quekind = findViewById(R.id.lay_reqfix_quekind);
        bundleView = (BundleView) findViewById(R.id.bundle_reqfix);
        voice_recorder = (InsVoiceRecorderView) findViewById(R.id.voice_recorder);
        btn_go = findViewById(R.id.btn_go);

        findViewById(R.id.img_reqfix_photo).setOnClickListener(this);
        findViewById(R.id.img_reqfix_vidio).setOnClickListener(this);
        findViewById(R.id.img_reqfix_voice).setOnClickListener(this);
    }

    private void initCtrl() {
        lay_reqfix_quekind.setOnClickListener(this);
        btn_go.setOnClickListener(this);
        bundleView.setOnBundleClickListener(new BundleView.OnBundleClickListener() {
            @Override
            public void onPhotoDelClick(View v) {
                pathphoto = "";
            }

            @Override
            public void onVideoDelClick(View v) {
                pathvideo = "";
            }

            @Override
            public void onVoiceDelClick(View v) {
                pathvoice = "";
            }

            @Override
            public void onPhotoShowClick(View v) {
                Intent intent = new Intent(ReqfixActicity.this, PhotoActivity.class);
                intent.putExtra("url", pathphoto);
                startActivity(intent);
            }

            @Override
            public void onVideoShowClick(View v) {
                Intent intent = new Intent(ReqfixActicity.this, VideoActivity.class);
                intent.putExtra("url", pathvideo);
                startActivity(intent);
            }

            @Override
            public void onVoiceShowClick(View v) {
                player.setUrl(pathvoice);
                player.play();
            }
        });
//        bundleView.setVoiceTouchListenner(new View.OnTouchListener(){
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                return voice_recorder.onPressToSpeakBtnTouch(v, event, new InsVoiceRecorderView.InsVoiceRecorderCallback() {
//
//                    @Override
//                    public void onVoiceRecordComplete(String voiceFilePath, int voiceTimeLength) {
//                        // 发送语音消息
//                        //sendVoiceMessage(voiceFilePath, voiceTimeLength);
////                        Toast.makeText(ReqfixActicity.this,"complete:"+voiceFilePath+"  length:"+voiceTimeLength,Toast.LENGTH_SHORT).show();
//                        Log.e("liao","complete:"+voiceFilePath+"  length:"+voiceTimeLength);
//                        pathvoice = voiceFilePath;
//                        bundleView.setVoice();
//                    }
//                });
//            }
//        });
        recordDialog.setOnRecordListener(new RecordDialog.OnRecordListener() {
            @Override
            public void onRecordFinish(String voiceFilePath, int voiceTimeLength) {
                Log.e("liao", "complete:" + voiceFilePath + "  length:" + voiceTimeLength);
                pathvoice = voiceFilePath;
                bundleView.setVoice();
            }
        });
        player.setOnPlayerListener(new MyPlayer.OnPlayerListener() {
            @Override
            public void onStart() {
                Log.e("liao", "start");
//                bundleView.setStartVoiceAnim();
            }

            @Override
            public void onCompleted() {
                Log.e("liao", "stop");
//                bundleView.setStopVoiceAnim();
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
                chooseDialog.show();
                break;
            case R.id.img_reqfix_photo:
                popup.show();
                break;
            case R.id.img_reqfix_vidio:
                Intent intent = new Intent(ReqfixActicity.this, VideoRecorderActivity.class);
                startActivityForResult(intent, CODE_VIDEO_RECORDER);
                break;
            case R.id.img_reqfix_voice:
                recordDialog.show();
                break;
            case R.id.btn_go:
                btn_go.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        return voice_recorder.onPressToSpeakBtnTouch(v, event, new InsVoiceRecorderView.InsVoiceRecorderCallback() {

                            @Override
                            public void onVoiceRecordComplete(String voiceFilePath, int voiceTimeLength) {
                                // 发送语音消息
                                //sendVoiceMessage(voiceFilePath, voiceTimeLength);
//                        Toast.makeText(getContext(),"complete:"+voiceFilePath+"  length:"+voiceTimeLength, Toast.LENGTH_SHORT).show();
                                Log.e("liao", "complete:" + voiceFilePath + "  length:" + voiceTimeLength);
                            }
                        });
                    }
                });
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
                    pathvideo = data.getStringExtra("path");
                    Log.e("liao", pathvideo);
//                    Toast.makeText(this, "存储路径为:" + pathvideo, Toast.LENGTH_SHORT).show();
                    // 通过路径获取第一帧的缩略图并显示
                    Bitmap bitmap = VideoUtils.createVideoThumbnail(pathvideo);
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
        this.pathphoto = path;
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
