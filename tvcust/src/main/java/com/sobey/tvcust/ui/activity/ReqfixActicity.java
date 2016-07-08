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
import com.sobey.tvcust.ui.dialog.DialogReqfixChoose;

import org.greenrobot.eventbus.EventBus;
import org.xutils.http.RequestParams;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ReqfixActicity extends BaseAppCompatActicity implements View.OnClickListener, CropHelper.CropInterface {

    private CropHelper cropHelper = new CropHelper(this);

    private View lay_reqfix_quekind;
    private View lay_reqfix_selectuser;
    private DialogLoading loadingDialog;
    private DialogReqfixChoose chooseDialog;
    private DialogPopupPhoto popup;
    private DialogRecord recordDialog;
    private BundleView2 bundleView;
    private MyPlayer player = new MyPlayer();

    private CircularProgressButton btn_go;
    private TextView text_question_name;
    private TextView text_user_name;
    private EditText edit_reqfix_detail;

    private int categoryId;
    private int userId;
    private User user;

    private static final int RESULT_QUESTION = 0xf102;
    private static final int RESULT_SELECTUSER = 0xf103;
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
        if (player != null) player.onDestory();
        if (loadingDialog != null) loadingDialog.dismiss();
        if (popup != null) popup.dismiss();
        if (chooseDialog != null) chooseDialog.dismiss();
        if (recordDialog != null) recordDialog.dismiss();
    }

    private void initBase() {
        user = AppData.App.getUser();

        loadingDialog = new DialogLoading(this, "正在上传");
        chooseDialog = new DialogReqfixChoose(this);
        chooseDialog.setOnHardListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseDialog.hide();
                Intent intent = new Intent(ReqfixActicity.this, QuestionActivity.class);
                intent.putExtra("type", "1");
                startActivityForResult(intent, RESULT_QUESTION);
            }
        });
        chooseDialog.setOnSoftListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseDialog.hide();
                Intent intent = new Intent(ReqfixActicity.this, QuestionActivity.class);
                intent.putExtra("type", "0");
                startActivityForResult(intent, RESULT_QUESTION);
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
        lay_reqfix_selectuser = findViewById(R.id.lay_reqfix_selectuser);
        bundleView = (BundleView2) findViewById(R.id.bundle_reqfix);
        btn_go = (CircularProgressButton) findViewById(R.id.btn_go);
        edit_reqfix_detail = (EditText) findViewById(R.id.edit_reqfix_detail);
        text_question_name = (TextView) findViewById(R.id.text_question_name);
        text_user_name = (TextView) findViewById(R.id.text_user_name);

        findViewById(R.id.img_reqfix_photo).setOnClickListener(this);
        findViewById(R.id.img_reqfix_vidio).setOnClickListener(this);
        findViewById(R.id.img_reqfix_voice).setOnClickListener(this);

        //如果是客户可以为用户代理申报
        if (user.getRoleType() == User.ROLE_CUSTOMER) {
            lay_reqfix_selectuser.setVisibility(View.VISIBLE);
        } else {
            lay_reqfix_selectuser.setVisibility(View.GONE);
        }
    }

    private void initCtrl() {
        lay_reqfix_quekind.setOnClickListener(this);
        lay_reqfix_selectuser.setOnClickListener(this);
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
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.lay_reqfix_quekind:
                chooseDialog.show();
                break;
            case R.id.lay_reqfix_selectuser:
                intent.setClass(this, SelectUserActivity.class);
                startActivityForResult(intent, RESULT_SELECTUSER);
                break;
            case R.id.img_reqfix_photo:
                popup.show();
                break;
            case R.id.img_reqfix_vidio:
                intent.setClass(ReqfixActicity.this, VideoRecorderActivity.class);
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
                } else {
                    // 失败
                }
                break;
            case RESULT_QUESTION:
                if (resultCode == RESULT_OK) {
                    String name = data.getStringExtra("name");
                    int categoryId = data.getIntExtra("id", 0);
                    text_question_name.setText(name);
                    this.categoryId = categoryId;
                }
                break;
            case RESULT_SELECTUSER:
                if (resultCode == RESULT_OK) {
                    String name = data.getStringExtra("name");
                    int userId = data.getIntExtra("id", 0);
                    text_user_name.setText(name);
                    this.userId = userId;
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
        if (user.getRoleType() == User.ROLE_COMMOM) {
            //用户不用验证代办用户id
            msg = AppVali.reqfix_commit(categoryId, detail);
        } else if (user.getRoleType() == User.ROLE_CUSTOMER) {
            //客户必须验证代办用户id
            msg = AppVali.reqfix_commit_withuser(categoryId, userId, detail);
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

        netCommit();
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
                Toast.makeText(ReqfixActicity.this, text, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void netEnd(int code) {
            }

            @Override
            public void netStart(int code) {
            }
        });
    }

    private void netCommit() {
        String detail = edit_reqfix_detail.getText().toString();

        btn_go.setProgress(50);
        RequestParams params = new RequestParams(AppData.Url.reqfix);
        params.addHeader("token", AppData.App.getToken());
        params.addBodyParameter("categoryId", categoryId + "");
        params.addBodyParameter("userId", userId + "");
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

                Toast.makeText(ReqfixActicity.this, text, Toast.LENGTH_SHORT).show();

                btn_go.setProgress(100);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        EventBus.getDefault().post(AppConstant.EVENT_UPDATE_ORDERLIST);
                        finish();
                    }
                }, 800);
            }

            @Override
            public void netSetError(int code, String text) {
                Toast.makeText(ReqfixActicity.this, text, Toast.LENGTH_SHORT).show();
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
