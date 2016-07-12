package com.sobey.tvcust.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dd.CircularProgressButton;
import com.google.gson.Gson;
import com.sobey.common.common.CommonNet;
import com.sobey.common.common.MyPlayer;
import com.sobey.common.helper.CropHelper;
import com.sobey.common.utils.StrUtils;
import com.sobey.common.view.BundleView2;
import com.sobey.tvcust.R;
import com.sobey.tvcust.common.AppConstant;
import com.sobey.tvcust.common.AppData;
import com.sobey.tvcust.common.AppVali;
import com.sobey.tvcust.entity.CommonEntity;
import com.sobey.tvcust.entity.User;
import com.sobey.tvcust.ui.dialog.DialogLoading;
import com.sobey.tvcust.ui.dialog.DialogPopupPhoto;
import com.sobey.tvcust.ui.dialog.DialogRecord;

import org.greenrobot.eventbus.EventBus;
import org.xutils.http.RequestParams;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * type为0 是完成任务 type为1 是验收成功 type 2 验收拒绝 type 3 跨级追加
 */
public class ReqDescribeOnlyActicity extends BaseAppCompatActicity implements View.OnClickListener, CropHelper.CropInterface {

    private CropHelper cropHelper = new CropHelper(this);

    private DialogLoading loadingDialog;
    private DialogPopupPhoto popup;
    private DialogRecord recordDialog;
    private BundleView2 bundleView;
    private MyPlayer player = new MyPlayer();

    private CircularProgressButton btn_go;
    private EditText edit_reqfix_detail;

    private int orderId;
    private int type;
    private User user;

    private static final int RESULT_SELECTUSER = 0xf103;
    private static final int RESULT_SELECTCOPER = 0xf104;
    private static final int RESULT_VIDEO_RECORDER = 0xf101;

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
        setContentView(R.layout.activity_reqdescribeonly);
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
        if (player != null) player.onDestory();
        if (loadingDialog != null) loadingDialog.dismiss();
        if (popup != null) popup.dismiss();
        if (recordDialog != null) recordDialog.dismiss();
    }

    private void initBase() {
        if (getIntent().hasExtra("title")){
            getSupportActionBar().setTitle(getIntent().getStringExtra("title"));
        }
        if (getIntent().hasExtra("orderId")) {
            orderId = getIntent().getIntExtra("orderId", 0);
        }
        if (getIntent().hasExtra("type")) {
            type = getIntent().getIntExtra("type", 0);
        }
        user = AppData.App.getUser();

        loadingDialog = new DialogLoading(this, "正在上传");
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
        bundleView = (BundleView2) findViewById(R.id.bundle_reqfix);
        btn_go = (CircularProgressButton) findViewById(R.id.btn_go);
        edit_reqfix_detail = (EditText) findViewById(R.id.edit_reqfix_detail);

        findViewById(R.id.img_reqfix_photo).setOnClickListener(this);
        findViewById(R.id.img_reqfix_vidio).setOnClickListener(this);
        findViewById(R.id.img_reqfix_voice).setOnClickListener(this);
    }

    private void initCtrl() {
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
                Intent intent = new Intent(ReqDescribeOnlyActicity.this, PhotoActivity.class);
                intent.putExtra("url", path);
                startActivity(intent);
            }

            @Override
            public void onVideoShowClick(String path) {
                Intent intent = new Intent(ReqDescribeOnlyActicity.this, VideoActivity.class);
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
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.img_reqfix_photo:
                popup.show();
                break;
            case R.id.img_reqfix_vidio:
                intent.setClass(ReqDescribeOnlyActicity.this, VideoRecorderActivity.class);
                startActivityForResult(intent, RESULT_VIDEO_RECORDER);
                break;
            case R.id.img_reqfix_voice:
                recordDialog.show();
                break;
            case R.id.btn_go:

                photoPaths = bundleView.getPhotoPaths();
                videoPaths = bundleView.getVideoPaths();
                voicePaths = bundleView.getVoicePaths();

                netUpload();

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        cropHelper.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case RESULT_VIDEO_RECORDER:
                if (resultCode == RESULT_OK) {
                    // 成功
                    String pathvideo = data.getStringExtra("path");
                    Log.e("liao", pathvideo);
                    //Toast.makeText(this, "存储路径为:" + pathvideo, Toast.LENGTH_SHORT).show();
                    // 通过路径获取第一帧的缩略图并显示
                    //Bitmap bitmap = VideoUtils.createVideoThumbnail(pathvideo);
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
        bundleView.addPhoto(path);
    }


    //存储图片、视频、音频的路径数组，path是本地路径，url是上传后返回的网络路径
    private String[] photoPaths;
    private String[] videoPaths;
    private String[] voicePaths;
    private String[] photoUrls;
    private String[] videoUrls;
    private String[] voiceUrls;

    private void netUpload() {
        String detail = edit_reqfix_detail.getText().toString();

        String msg = null;
        msg = AppVali.reqfix_addDescribe(detail);
        if (msg != null) {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        } else {

            ArrayList<String> paths = new ArrayList<>();
            Collections.addAll(paths, photoPaths);
            Collections.addAll(paths, videoPaths);
            Collections.addAll(paths, voicePaths);

            uploadFile(paths);
        }
    }

    private void afterUpload() {
        for (String path : urls) {
            Log.e("liao", path);
        }

        if (!StrUtils.isEmpty(photoPaths)) {
            photoUrls = urls.subList(0, photoPaths.length).toArray(new String[]{});
        }
        if (!StrUtils.isEmpty(videoPaths)) {
            int offset = photoUrls == null ? 0 : photoUrls.length;
            videoUrls = urls.subList(offset, offset + videoPaths.length).toArray(new String[]{});
        }
        if (!StrUtils.isEmpty(voicePaths)) {
            int offset = (photoUrls == null ? 0 : photoUrls.length) + (videoPaths == null ? 0 : videoPaths.length);
            voiceUrls = urls.subList(offset, offset + voicePaths.length).toArray(new String[]{});
        }
        netAddDescribe();
    }

    ////////////////////////////////////////////////
    //////////////////批量上传
    ////////////////////////////////////////////////

    private List<String> urls = new ArrayList<>();
    private int index = 0;

    private void uploadFile(final List<String> paths) {
        if (index > paths.size() - 1) {
            afterUpload();
            return;
        }
        String path = paths.get(index);
        RequestParams params = new RequestParams(AppData.Url.upload);
        params.setMultipart(true);
        params.addHeader("token", AppData.App.getToken());
        params.addBodyParameter("files", new File(path));
        CommonNet.samplepost(params, CommonEntity.class, new CommonNet.SampleNetHander() {
            @Override
            public void netGo(int code, Object pojo, String text, Object obj) {
                if (pojo == null) netSetError(code, text);
                else {
                    Log.e("upload", text);
                    CommonEntity commonEntity = (CommonEntity) pojo;
                    String url = commonEntity.getFilePath();
                    //上传完毕
                    urls.add(url);
                    index++;
                    uploadFile(paths);
                }
            }

            @Override
            public void netSetError(int code, String text) {
                Toast.makeText(ReqDescribeOnlyActicity.this, text, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void netEnd(int code) {
            }

            @Override
            public void netStart(int code) {
            }
        });
    }

    private void netAddDescribe() {
        String detail = edit_reqfix_detail.getText().toString();

        btn_go.setProgress(50);
        RequestParams params = new RequestParams(AppData.Url.addOrderDecribe);
        params.addHeader("token", AppData.App.getToken());
        params.addBodyParameter("orderId", orderId + "");
        if (type==0) {
            params.addBodyParameter("flag", "1");
        }else if (type==1 || type==2){
            params.addBodyParameter("flag", "0");
        }else if (type==3){
            params.addBodyParameter("flag", "2");
        }
        params.addBodyParameter("content", detail);
        if (!StrUtils.isEmpty(photoUrls)) {
            params.addBodyParameter("images", new Gson().toJson(photoUrls));
        }
        if (!StrUtils.isEmpty(videoUrls)) {
            params.addBodyParameter("videos", new Gson().toJson(videoUrls));
        }
        if (!StrUtils.isEmpty(voiceUrls)) {
            params.addBodyParameter("voices", new Gson().toJson(voiceUrls));
        }
        CommonNet.samplepost(params, CommonEntity.class, new CommonNet.SampleNetHander() {
            @Override
            public void netGo(int code, Object pojo, String text, Object obj) {
                if (type==0) {
                    EventBus.getDefault().post(AppConstant.EVENT_UPDATE_ORDERDESCRIBE);
                    EventBus.getDefault().post(AppConstant.EVENT_UPDATE_ORDERLIST);
                    netFinishOrder();
                }else if (type==1 || type==2){
                    EventBus.getDefault().post(AppConstant.EVENT_UPDATE_ORDERDESCRIBE);
                    EventBus.getDefault().post(AppConstant.EVENT_UPDATE_ORDERLIST);
                    netValiOrder();
                }else if (type==3){
                    EventBus.getDefault().post(AppConstant.EVENT_UPDATE_ORDERDESCRIBE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    }, 800);
                }
            }

            @Override
            public void netSetError(int code, String text) {
                Toast.makeText(ReqDescribeOnlyActicity.this, text, Toast.LENGTH_SHORT).show();
                btn_go.setProgress(-1);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        btn_go.setProgress(0);
                    }
                }, 800);
            }
        });
    }

    private void netFinishOrder() {
        RequestParams params = new RequestParams(AppData.Url.verifiOrder);
        params.addHeader("token", AppData.App.getToken());
        params.addBodyParameter("orderId", orderId + "");
        CommonNet.samplepost(params, CommonEntity.class, new CommonNet.SampleNetHander() {
            @Override
            public void netGo(int code, Object pojo, String text, Object obj) {
                Toast.makeText(ReqDescribeOnlyActicity.this, text, Toast.LENGTH_SHORT).show();
                btn_go.setProgress(100);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(ReqDescribeOnlyActicity.this, OrderProgActivity.class);
                        intent.putExtra("orderId", orderId);
                        startActivity(intent);
                        finish();
                    }
                }, 800);
            }

            @Override
            public void netSetError(int code, String text) {
                Toast.makeText(ReqDescribeOnlyActicity.this, text, Toast.LENGTH_SHORT).show();
                btn_go.setProgress(-1);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        btn_go.setProgress(0);
                    }
                }, 800);
            }
        });
    }

    //用户验收
    private void netValiOrder() {
        RequestParams params = new RequestParams(AppData.Url.statusToAppraise);
        params.addHeader("token", AppData.App.getToken());
        params.addBodyParameter("orderId", orderId + "");
        if (type==1){
            //验收通过
            params.addBodyParameter("isaccept", 0+"");
        }else if(type==2){
            //验收拒绝
            params.addBodyParameter("isaccept", 1+"");
        }
        CommonNet.samplepost(params, CommonEntity.class, new CommonNet.SampleNetHander() {
            @Override
            public void netGo(int code, Object pojo, String text, Object obj) {
                Toast.makeText(ReqDescribeOnlyActicity.this, text, Toast.LENGTH_SHORT).show();
                btn_go.setProgress(100);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(ReqDescribeOnlyActicity.this, OrderProgActivity.class);
                        intent.putExtra("orderId", orderId);
                        startActivity(intent);
                        finish();
                    }
                }, 800);
            }

            @Override
            public void netSetError(int code, String text) {
                Toast.makeText(ReqDescribeOnlyActicity.this, text, Toast.LENGTH_SHORT).show();
                btn_go.setProgress(-1);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        btn_go.setProgress(0);
                    }
                }, 800);
            }
        });
    }
}
