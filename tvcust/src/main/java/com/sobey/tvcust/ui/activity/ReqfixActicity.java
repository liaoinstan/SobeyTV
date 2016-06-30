package com.sobey.tvcust.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.dd.CircularProgressButton;
import com.sobey.common.common.MyPlayer;
import com.sobey.common.helper.CropHelper;
import com.sobey.common.view.BundleView2;
import com.sobey.tvcust.R;
import com.sobey.tvcust.ui.dialog.DialogRecord;
import com.sobey.tvcust.ui.dialog.DialogPopupPhoto;
import com.sobey.tvcust.ui.dialog.DialogReqfixChoose;

public class ReqfixActicity extends AppCompatActivity implements View.OnClickListener, CropHelper.CropInterface {

    private CropHelper cropHelper = new CropHelper(this);

    private View lay_reqfix_quekind;
    private DialogReqfixChoose chooseDialog;
    private DialogPopupPhoto popup;
    private DialogRecord recordDialog;
    private BundleView2 bundleView;
    private MyPlayer player = new MyPlayer();

    private CircularProgressButton btn_go;

//    private String pathphoto;
//    private String pathvideo;
//    private String pathvoice;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        outState.putString("pathphoto", pathphoto);
//        outState.putString("pathvideo", pathvideo);
//        outState.putString("pathvoice", pathvoice);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
//            pathphoto = savedInstanceState.getString("pathphoto");
//            pathvideo = savedInstanceState.getString("pathvideo");
//            pathvoice = savedInstanceState.getString("pathvoice");
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
        if (player!=null) player.onDestory();
        if (popup!=null) popup.dismiss();
        if (chooseDialog!=null) chooseDialog.dismiss();
        if (recordDialog!=null) recordDialog.dismiss();
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
        recordDialog = new DialogRecord(this);
    }

    private void initView() {
        lay_reqfix_quekind = findViewById(R.id.lay_reqfix_quekind);
        bundleView = (BundleView2) findViewById(R.id.bundle_reqfix);
        btn_go = (CircularProgressButton) findViewById(R.id.btn_go);

        findViewById(R.id.img_reqfix_photo).setOnClickListener(this);
        findViewById(R.id.img_reqfix_vidio).setOnClickListener(this);
        findViewById(R.id.img_reqfix_voice).setOnClickListener(this);
    }

    private void initCtrl() {
        lay_reqfix_quekind.setOnClickListener(this);
        btn_go.setOnClickListener(this);
        btn_go.setIndeterminateProgressMode(true);
        bundleView.setDelEnable(true);
        bundleView.setOnBundleClickListener(new BundleView2.OnBundleClickListener() {
            @Override
            public void onPhotoDelClick(View v) {
//                pathphoto = "";
            }

            @Override
            public void onVideoDelClick(View v) {
//                pathvideo = "";
            }

            @Override
            public void onVoiceDelClick(View v) {
//                pathvoice = "";
            }

            @Override
            public void onPhotoShowClick(String path) {
                Intent intent = new Intent(ReqfixActicity.this, PhotoActivity.class);
                intent.putExtra("url", path);
                startActivity(intent);
            }

            @Override
            public void onVideoShowClick(String path) {
                Intent intent = new Intent(ReqfixActicity.this, VideoActivity.class);
                intent.putExtra("url", path);
                startActivity(intent);
            }

            @Override
            public void onVoiceShowClick(String path) {
                player.setUrl(path);
                player.play();
            }
        });
        recordDialog.setOnRecordListener(new DialogRecord.OnRecordListener() {
            @Override
            public void onRecordFinish(String voiceFilePath, int voiceTimeLength) {
                Log.e("liao", "complete:" + voiceFilePath + "  length:" + voiceTimeLength);
//                pathvoice = voiceFilePath;
                bundleView.addVoice(voiceFilePath);
            }
        });
        player.setOnPlayerListener(new MyPlayer.OnPlayerListener() {
            @Override
            public void onStart() {
                Log.e("liao", "start");
            }

            @Override
            public void onCompleted() {
                Log.e("liao", "stop");
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
                btn_go.setProgress(50);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        btn_go.setProgress(100);
                    }
                }, 2000);

                String[] photoPaths = bundleView.getPhotoPaths();
                String[] videoPaths = bundleView.getVideoPaths();
                String[] voicePaths = bundleView.getVoicePaths();
                Log.e("liao","photo");
                for (String path:photoPaths) {
                    Log.e("liao",path);
                }
                Log.e("liao","video");
                for (String path:videoPaths) {
                    Log.e("liao", path);
                }
                Log.e("liao","voice");
                for (String path:voicePaths) {
                    Log.e("liao", path);
                }
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
                    String pathvideo = data.getStringExtra("path");
                    Log.e("liao", pathvideo);
//                    Toast.makeText(this, "存储路径为:" + pathvideo, Toast.LENGTH_SHORT).show();
                    // 通过路径获取第一帧的缩略图并显示
//                    Bitmap bitmap = VideoUtils.createVideoThumbnail(pathvideo);
                    bundleView.addVideo(pathvideo);
                } else {
                    // 失败
                }
                break;
        }
    }

    @Override
    public void cropResult(String path) {
        Log.e("liao", path);
//        this.pathphoto = path;

//        Bitmap bitmap = BitmapFactory.decodeFile(path);
        bundleView.addPhoto(path);
    }
}
