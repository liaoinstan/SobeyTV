package com.sobey.tvcust.ui.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.sobey.common.utils.ApplicationHelp;
import com.sobey.common.view.MovieRecorderView;
import com.sobey.tvcust.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class VideoRecorderActivity extends BaseAppCompatActicity {
    private MovieRecorderView mRecorderView;
    private Button mShootBtn;
    private boolean isFinish = true;

    private static final int REQUEST_PERMISSION_SETTING =  0xfe12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);

        init();

    }

    private void init(){
        setContentView(R.layout.activity_videorecorder);

        mRecorderView = (MovieRecorderView) findViewById(R.id.movieRecorderView);
        mShootBtn = (Button) findViewById(R.id.shoot_button);

        mShootBtn.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    mRecorderView.record(new MovieRecorderView.OnRecordFinishListener() {
                        @Override
                        public void onRecordFinish() {
                            handler.sendEmptyMessage(1);
                        }
                    });
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (mRecorderView.getTimeCount() > 1)
                        handler.sendEmptyMessage(1);
                    else {
                        if (mRecorderView.getmRecordFile() != null)
                            mRecorderView.getmRecordFile().delete();
                        mRecorderView.stop();
                        Toast.makeText(VideoRecorderActivity.this, "视频录制时间太短", Toast.LENGTH_SHORT).show();
                    }
                }
                return true;
            }
        });
    }

    @Subscribe
    public void onEventMainThread(Integer flag) {
        if (MovieRecorderView.EVENT_UPDATE_EXCEPTION.equals(flag)){

            mShootBtn.setEnabled(false);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("提示");
            builder.setMessage("需要您启用摄像头和录音权限");
            builder.setNegativeButton("取消", new Dialog.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.setPositiveButton("前往设置", new Dialog.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String packageName = null;
                    try {
                        final PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), 0);
                        packageName = info.applicationInfo.packageName;
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }
                    // 进入App设置页面
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", packageName, null);
                    intent.setData(uri);
                    startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                }
            });
            final AlertDialog dialog = builder.create();
            //**//在dialog  show方法之前添加如下代码，表示该dialog是一个系统的dialog**
            dialog.getWindow().setType((WindowManager.LayoutParams.TYPE_SYSTEM_ALERT));
            dialog.show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_PERMISSION_SETTING:
//                if (resultCode == RESULT_OK) {
//
//                }
                Log.e("liao",(resultCode== RESULT_OK)+"");
                mShootBtn.setEnabled(true);
                init();
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        isFinish = true;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        isFinish = false;
        mRecorderView.stop();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            finishActivity();
        }
    };

    private void finishActivity() {
        if (isFinish) {
            mRecorderView.stop();
            // 返回到播放页面
            Intent intent = new Intent();
            Log.d("TAG", mRecorderView.getmRecordFile().getAbsolutePath());
            intent.putExtra("path", mRecorderView.getmRecordFile().getAbsolutePath());
            setResult(RESULT_OK, intent);
        }
        // isFinish = false;
        finish();
    }

    /**
     * 录制完成回调
     *
     * @author liuyinjun
     * @date 2015-2-9
     */
    public interface OnShootCompletionListener {
        public void OnShootSuccess(String path, int second);

        public void OnShootFailure();
    }

  /*  //  private Button btnVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       *//* btnVideo = (Button) findViewById(R.id.btn_video);

        btnVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                String url = createFilePath("QDbzxt_video");
                File file = new File(url,"mov_"+getCurrentDate()+".mp4");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file)); // 设置存储路径
                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1); // 设置保存质量
                intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 10); // 设置限制时长
                //intent.putExtra(MediaStore.EXTRA_SIZE_LIMIT,100000L); // 设置限制大小
                startActivityForResult(intent, 0x123);

            }
        });*//*
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

  /*  @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0x123:
                if(resultCode == RESULT_OK) {
                    Toast.makeText(this, "Video saved to:\n" +
                            data.getData(), Toast.LENGTH_LONG).show();
                    // 获取第一帧图片

                }
                break;
        }
    }

    *//**
     * 创建文件夹路径
     * @param folderName　文件夹名称
     * @return 文件夹路径
     *//*
    public static String createFilePath(String folderName) {
        File file = new File(android.os.Environment.getExternalStorageDirectory()+"/"+folderName);
        file.mkdir();
        String path = file.getPath();
        return path;
    }

    *//**
     * 获取当前时间
     * @return 日期的字符串信息
     *//*
    public static String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String strDate = dateFormat.format(new Date());
        return strDate;
    }*/

}
