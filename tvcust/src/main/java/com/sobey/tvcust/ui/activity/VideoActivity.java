package com.sobey.tvcust.ui.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

import com.sobey.tvcust.R;
import com.sobey.tvcust.ui.dialog.DialogLoading;
import com.sobey.tvcust.ui.dialog.DialogSure;

public class VideoActivity extends BaseAppCompatActivity {

    private VideoView videoView;
    //    private Dialog dialog;
    private DialogSure dialogSure;
    private DialogLoading dialogLoading;
    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

//        uri = Uri.parse("http://7xnfyf.com1.z0.glb.clouddn.com/myvideo_h.mp4");
//        Uri uri = Uri.parse("http://7xnfyf.com1.z0.glb.clouddn.com/myvideo.mp4");
//        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.myvideo_h);

        initBase();
        initView();
        initCtrl();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dialogSure != null) dialogSure.dismiss();
        if (dialogLoading != null) dialogLoading.dismiss();
        if (videoView!=null) videoView.stopPlayback();
    }

    private void initBase() {
        String url = getIntent().getStringExtra("url");
        Log.e("liao", url);
        uri = Uri.parse(url);

//        dialog= ProgressDialog.show(this, "正在加载…", "马上开始");
        dialogLoading = new DialogLoading(this, "正在缓冲");
        dialogSure = new DialogSure(this, "无法播放此视频，请检查网络是否正常", "退出", "重试");
        dialogSure.setOnCancleListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        dialogSure.setOnOkListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogSure.dismiss();
                initCtrl();
            }
        });
    }

    private void initView() {
        videoView = (VideoView) findViewById(R.id.videoView);
    }

    private void initCtrl() {

        dialogLoading.show();

        videoView.setMediaController(new MediaController(this));
        videoView.setVideoURI(uri);
        videoView.start();
        videoView.requestFocus();
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                dialogLoading.hide();
            }
        });
        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                dialogLoading.hide();
                dialogSure.show();
                return true;
            }
        });
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                finish();
            }
        });
    }
}
