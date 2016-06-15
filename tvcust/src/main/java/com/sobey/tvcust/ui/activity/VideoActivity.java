package com.sobey.tvcust.ui.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.MediaController;
import android.widget.VideoView;

import com.sobey.tvcust.R;

public class VideoActivity extends AppCompatActivity {

    private VideoView videoView;
    private Dialog dialog;
    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        String url = getIntent().getStringExtra("url");
        Log.e("liao",url);
        uri = Uri.parse(url);
//        uri = Uri.parse("http://7xnfyf.com1.z0.glb.clouddn.com/myvideo_h.mp4");
//        Uri uri = Uri.parse("http://7xnfyf.com1.z0.glb.clouddn.com/myvideo.mp4");
//        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.myvideo_h);
        videoView = (VideoView) findViewById(R.id.videoView);
        initCtrl();
    }

    private void initCtrl() {
        //创建进度条
        dialog= ProgressDialog.show(this, "正在加载…", "马上开始");

        videoView.setMediaController(new MediaController(this));
        videoView.setVideoURI(uri);
        videoView.start();
        videoView.requestFocus();
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                dialog.dismiss();
            }
        });
    }
}
