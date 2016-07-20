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
import com.sobey.tvcust.common.CommonNet;
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
import com.sobey.tvcust.utils.PermissionsUtil;

import org.greenrobot.eventbus.EventBus;
import org.xutils.http.RequestParams;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 维修申报和追加描述页面
 * type为0 是维修申报 type为1 是追加描述
 */
public class ReqDescribeActicity extends BaseAppCompatActicity implements View.OnClickListener, CropHelper.CropInterface {

    private CropHelper cropHelper = new CropHelper(this);

    private View lay_reqfix_selectuser;
    private View lay_reqfix_selectcoper;
    private DialogLoading loadingDialog;
    private DialogPopupPhoto popup;
    private DialogRecord recordDialog;
    private BundleView2 bundleView;
    private MyPlayer player = new MyPlayer();

    private CircularProgressButton btn_go;
    private TextView text_user_name;
    private TextView text_coper_name;
    private EditText edit_reqfix_detail;

    private ArrayList<Integer> ids;
    private int assisterId;
    private int orderId;
    private int categoryId;
    private int userId;
    private int flag;
    private User user;
    private boolean isAccept;
    private boolean isCopy;
    private boolean newflag;

    private boolean hasVideo = false;

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
        setContentView(R.layout.activity_reqdescribe);
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
        if (getIntent().hasExtra("categoryId")) {
            categoryId = getIntent().getIntExtra("categoryId", 0);
        }
        if (getIntent().hasExtra("userId")) {
            userId = getIntent().getIntExtra("userId", 0);
        }
        if (getIntent().hasExtra("flag")) {
            flag = getIntent().getIntExtra("flag", 0);
        }
        if (getIntent().hasExtra("isAccept")) {
            isAccept = getIntent().getBooleanExtra("isAccept", false);
        }
        if (getIntent().hasExtra("isCopy")) {
            isCopy = getIntent().getBooleanExtra("isCopy", false);
        }
        if (getIntent().hasExtra("newflag")) {
            newflag = getIntent().getBooleanExtra("newflag", false);
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
        lay_reqfix_selectuser = findViewById(R.id.lay_reqfix_selectuser);
        lay_reqfix_selectcoper = findViewById(R.id.lay_reqfix_selectcoper);
        bundleView = (BundleView2) findViewById(R.id.bundle_reqfix);
        btn_go = (CircularProgressButton) findViewById(R.id.btn_go);
        edit_reqfix_detail = (EditText) findViewById(R.id.edit_reqfix_detail);
        text_user_name = (TextView) findViewById(R.id.text_user_name);
        text_coper_name = (TextView) findViewById(R.id.text_coper_name);

        findViewById(R.id.img_reqfix_photo).setOnClickListener(this);
        findViewById(R.id.img_reqfix_vidio).setOnClickListener(this);
        findViewById(R.id.img_reqfix_voice).setOnClickListener(this);

        //如果已经分配过援助对象则不显示分配按钮
        if (isAccept) {
            lay_reqfix_selectuser.setVisibility(View.VISIBLE);
        } else {
            lay_reqfix_selectuser.setVisibility(View.GONE);
        }
        if (isCopy) {
            lay_reqfix_selectcoper.setVisibility(View.VISIBLE);
        } else {
            lay_reqfix_selectcoper.setVisibility(View.GONE);
        }

        //4.x设备必须隐藏CardView
        if (lay_reqfix_selectuser.getVisibility() == View.GONE && lay_reqfix_selectcoper.getVisibility() == View.GONE) {
            findViewById(R.id.lay_reqfix_card).setVisibility(View.GONE);
        }else {
            findViewById(R.id.lay_reqfix_card).setVisibility(View.VISIBLE);
        }
    }

    private void initCtrl() {
        lay_reqfix_selectuser.setOnClickListener(this);
        lay_reqfix_selectcoper.setOnClickListener(this);
        btn_go.setOnClickListener(this);
        btn_go.setIndeterminateProgressMode(true);
        bundleView.setDelEnable(true);
        bundleView.setOnBundleClickListener(new BundleView2.OnBundleClickListener() {
            @Override
            public void onPhotoDelClick(View v) {
            }

            @Override
            public void onVideoDelClick(View v) {
                hasVideo = false;
            }

            @Override
            public void onVoiceDelClick(View v) {
            }

            @Override
            public void onPhotoShowClick(String path) {
                Intent intent = new Intent(ReqDescribeActicity.this, PhotoActivity.class);
                intent.putExtra("url", path);
                startActivity(intent);
            }

            @Override
            public void onVideoShowClick(String path) {
                Intent intent = new Intent(ReqDescribeActicity.this, VideoActivity.class);
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
            case R.id.lay_reqfix_selectuser:
                if (newflag) {
                    intent.setClass(this, OrderAllocateOnlyActivity.class);
                    intent.putExtra("userId", userId);
                    intent.putExtra("orderId", orderId);
                    intent.putExtra("categoryId", categoryId);
                    startActivityForResult(intent, RESULT_SELECTUSER);
                } else {
                    intent.setClass(this, AssistActivity.class);
                    intent.putExtra("userId", userId);
                    startActivityForResult(intent, RESULT_SELECTUSER);
                }
                break;
            case R.id.lay_reqfix_selectcoper:
                intent.setClass(this, CopyActivity.class);
                intent.putExtra("userId", userId);
                startActivityForResult(intent, RESULT_SELECTCOPER);
                break;
            case R.id.img_reqfix_photo:
                if (PermissionsUtil.requsetPhoto(this,findViewById(R.id.showingroup))) {
                    popup.show();
                }
                break;
            case R.id.img_reqfix_vidio:
                if (!hasVideo) {
                    if (PermissionsUtil.requsetVideo(this,findViewById(R.id.showingroup))) {
                        intent.setClass(ReqDescribeActicity.this, VideoRecorderActivity.class);
                        startActivityForResult(intent, RESULT_VIDEO_RECORDER);
                    }
                }else {
                    Toast.makeText(this,"你只能上传一个视频",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.img_reqfix_voice:
                if (PermissionsUtil.requsetVoice(this,findViewById(R.id.showingroup))) {
                    recordDialog.show();
                }
                break;
            case R.id.btn_go:
                photoPaths = bundleView.getPhotoPaths();
                videoPaths = bundleView.getVideoPaths();
                voicePaths = bundleView.getVoicePaths();

                netUpload();

//                netCommit();

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
                    hasVideo = true;
                } else {
                    // 失败
                }
                break;
            case RESULT_SELECTUSER:
                if (resultCode == RESULT_OK) {
                    String name = data.getStringExtra("name");
                    int accepterId = data.getIntExtra("id", 0);
                    text_user_name.setText(name);
                    this.assisterId = accepterId;

                    Log.e("liao", "assisterId:" + assisterId);
                }
                break;
            case RESULT_SELECTCOPER:
                if (resultCode == RESULT_OK) {
                    ids = (ArrayList<Integer>) data.getSerializableExtra("ids");
                    String nameStr = data.getStringExtra("nameStr");
                    text_coper_name.setText(nameStr);

                    for (int id : ids)
                        Log.e("liao", "copersId:" + id);
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
        if (isAccept) {
            msg = AppVali.reqfix_addDescribe_withuser(assisterId, detail);
        } else {
            msg = AppVali.reqfix_addDescribe(detail);
        }
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
                Toast.makeText(ReqDescribeActicity.this, text, Toast.LENGTH_SHORT).show();
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
        params.addBodyParameter("flag", flag + "");
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

                if (isAccept) {
                    netAssistCommit();
                } else {
                    Toast.makeText(ReqDescribeActicity.this, text, Toast.LENGTH_SHORT).show();
                    btn_go.setProgress(100);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            EventBus.getDefault().post(AppConstant.EVENT_UPDATE_ORDERDESCRIBE);
                            EventBus.getDefault().post(AppConstant.EVENT_UPDATE_ORDERLIST);
                            finish();
                        }
                    }, 800);
                }
            }

            @Override
            public void netSetError(int code, String text) {
                Toast.makeText(ReqDescribeActicity.this, text, Toast.LENGTH_SHORT).show();
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

    private void netAssistCommit() {
        RequestParams params;
        if (User.ROLE_HEADCOMTECH == user.getRoleType()) {
            if (newflag) {
                //新需求：总部技术向下申请技术
                params = new RequestParams(AppData.Url.allotorder);
                params.addHeader("token", AppData.App.getToken());
                params.addBodyParameter("orderId", orderId + "");
                params.addBodyParameter("tscId", assisterId + "");
                params.addBodyParameter("roleType", "10");
            } else {
                //总部技术人员申请总部研发
                params = new RequestParams(AppData.Url.commitdeveloper);
                params.addHeader("token", AppData.App.getToken());
                params.addBodyParameter("orderId", orderId + "");
                params.addBodyParameter("developId", assisterId + "");
            }
        } else {
            //技术人员申请总部技术+抄送
            params = new RequestParams(AppData.Url.assistCommit);
            params.addHeader("token", AppData.App.getToken());
            params.addBodyParameter("orderId", orderId + "");
            params.addBodyParameter("headTechId", assisterId + "");
            if (!StrUtils.isEmpty(ids)) {
                params.addBodyParameter("ccs", new Gson().toJson(ids));
            }
        }

        CommonNet.samplepost(params, CommonEntity.class, new CommonNet.SampleNetHander() {
            @Override
            public void netGo(int code, Object pojo, String text, Object obj) {
                Toast.makeText(ReqDescribeActicity.this, text, Toast.LENGTH_SHORT).show();

                btn_go.setProgress(100);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        EventBus.getDefault().post(AppConstant.EVENT_UPDATE_ORDERDESCRIBE);
                        EventBus.getDefault().post(AppConstant.EVENT_UPDATE_ORDERLIST);
                        finish();
                    }
                }, 800);
            }

            @Override
            public void netSetError(int code, String text) {
                Toast.makeText(ReqDescribeActicity.this, text, Toast.LENGTH_SHORT).show();
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
